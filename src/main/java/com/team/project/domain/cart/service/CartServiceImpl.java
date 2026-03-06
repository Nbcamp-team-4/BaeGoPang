package com.team.project.domain.cart.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.team.project.domain.cart.api.request.AddCartItemRequest;
import com.team.project.domain.cart.api.request.UpdateCartItemRequest;
import com.team.project.domain.cart.api.response.AddCartItemResponse;
import com.team.project.domain.cart.api.response.GetCartResponse;
import com.team.project.domain.cart.api.response.UpdateCartItemResponse;
import com.team.project.domain.cart.entity.Cart;
import com.team.project.domain.cart.entity.CartItem;
import com.team.project.domain.cart.entity.CartItemOption;
import com.team.project.domain.cart.exception.CartForbiddenException;
import com.team.project.domain.cart.exception.CartItemNotFoundException;
import com.team.project.domain.cart.exception.CartNotFoundException;
import com.team.project.domain.cart.exception.InvalidCartQuantityException;
import com.team.project.domain.cart.exception.InvalidCartStatusException;
import com.team.project.domain.cart.model.vo.CartStatus;
import com.team.project.domain.cart.repository.CartRepository;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.entity.ProductOption;
import com.team.project.domain.product.entity.ProductOptionItem;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    // FK 확인용(주문과 동일)
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    // 옵션 엔티티를 만든 상태라면, 옵션 검증을 위해 필요(없으면 빼도 됨)
    private final com.team.project.domain.product.repository.ProductOptionRepository productOptionRepository;
    private final com.team.project.domain.product.repository.ProductOptionItemRepository productOptionItemRepository;

    @Override
    @Transactional // 또는 readOnly=true로 바꾸고 싶으면 spring 트랜잭션으로
    public GetCartResponse getCart(UUID userId) {
        Cart cart = cartRepository.findActiveCartDetailByUserId(userId)
                .orElseThrow(CartNotFoundException::new);

        return GetCartResponse.from(cart);
    }

    @Override
    @Transactional
    public AddCartItemResponse addCartItem(AddCartItemRequest request) {

        // 1) 기본 검증(수량)
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new InvalidCartQuantityException();
        }

        // 2) 유저/가게/상품 존재 확인 (실무에서는 전용 NotFoundException 권장)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("STORE_NOT_FOUND"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("PRODUCT_NOT_FOUND"));

        // 3) 유저의 ACTIVE 장바구니 조회(없으면 생성)
        Cart cart = cartRepository.findActiveCartByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(new Cart(user, store)));

        // 4) 장바구니 상태 체크 (ORDERED/ABANDONED면 수정 불가)
        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new InvalidCartStatusException();
        }

        // 5) 다른 가게 상품이면 정책 적용: 기존 장바구니 비우고 store 교체
        boolean cartReset = false;
        if (!cart.getStore().getId().equals(store.getId())) {
            cart.changeStoreAndClear(store);
            cartReset = true;
        }

        // 6) CartItem 생성 후 추가
        CartItem item = new CartItem(product, request.getQuantity());

        // 7) 옵션이 있으면 옵션 엔티티 매핑(간단히 검증만)
        if (request.getOptions() != null) {
            for (AddCartItemRequest.CartItemOptionRequest optReq : request.getOptions()) {

                ProductOption option = productOptionRepository.findById(optReq.getProductOptionId())
                        .orElseThrow(() -> new IllegalArgumentException("PRODUCT_OPTION_NOT_FOUND"));

                ProductOptionItem optionItem = productOptionItemRepository.findById(optReq.getProductOptionItemId())
                        .orElseThrow(() -> new IllegalArgumentException("PRODUCT_OPTION_ITEM_NOT_FOUND"));

                CartItemOption cio = new CartItemOption(option, optionItem);
                item.addOption(cio);
            }
        }

        cart.addItem(item);

        // 8) 저장 (cascade로 item/option까지 저장됨)
        Cart saved = cartRepository.save(cart);

        return AddCartItemResponse.of(
                saved,
                item.getId(),
                product.getId(),
                item.getQuantity(),
                cartReset
        );
    }

    @Override
    @Transactional
    public UpdateCartItemResponse updateCartItem(UUID cartId, UUID itemId, UpdateCartItemRequest request) {

        // 1) 수량 검증
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new InvalidCartQuantityException();
        }

        // 2) cart 상세 조회 (권한 체크 위해 user까지 가져오는 쿼리 사용 권장)
        Cart cart = cartRepository.findCartDetailById(cartId)
                .orElseThrow(CartNotFoundException::new);

        // 3) 본인 장바구니인지 확인
        if (!cart.getUser().getId().equals(request.getUserId())) {
            throw new CartForbiddenException();
        }

        // 4) 상태 체크
        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new InvalidCartStatusException();
        }

        // 5) 수정할 item 찾기
        CartItem target = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(CartItemNotFoundException::new);

        // 6) 수량 변경
        target.changeQuantity(request.getQuantity());

        // 7) 옵션 변경(단순하게: 기존 옵션 삭제 후 새로 세팅)
        if (request.getOptions() != null) {
            target.getOptions().clear();

            for (UpdateCartItemRequest.CartItemOptionRequest optReq : request.getOptions()) {

                ProductOption option = productOptionRepository.findById(optReq.getProductOptionId())
                        .orElseThrow(() -> new IllegalArgumentException("PRODUCT_OPTION_NOT_FOUND"));

                ProductOptionItem optionItem = productOptionItemRepository.findById(optReq.getProductOptionItemId())
                        .orElseThrow(() -> new IllegalArgumentException("PRODUCT_OPTION_ITEM_NOT_FOUND"));

                CartItemOption cio = new CartItemOption(option, optionItem);
                target.addOption(cio);
            }
        }

        return UpdateCartItemResponse.from(target);
    }

    @Override
    @Transactional
    public void deleteCartItem(UUID itemId, UUID userId) {

        // - 유저의 ACTIVE 장바구니를 가져와서 items에서 제거(orphanRemoval로 DB에서도 삭제)
        Cart cart = cartRepository.findActiveCartDetailByUserId(userId)
                .orElseThrow(CartNotFoundException::new);

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new InvalidCartStatusException();
        }

        boolean removed = cart.getItems().removeIf(i -> i.getId().equals(itemId));
        if (!removed) {
            throw new CartItemNotFoundException();
        }

        // 별도 save 호출 없이도 트랜잭션 내 변경감지로 반영됨
    }

    @Override
    @Transactional
    public void clearCart(UUID userId) {

        // 유저의 ACTIVE 장바구니 비우기
        Cart cart = cartRepository.findActiveCartDetailByUserId(userId)
                .orElseThrow(CartNotFoundException::new);

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new InvalidCartStatusException();
        }

        cart.clearItems();
    }
}