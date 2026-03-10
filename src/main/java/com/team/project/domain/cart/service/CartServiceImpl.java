package com.team.project.domain.cart.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import com.team.project.domain.cart.repository.CartItemOptionRepository;
import com.team.project.domain.cart.repository.CartItemRepository;
import com.team.project.domain.cart.repository.CartRepository;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.entity.ProductOption;
import com.team.project.domain.product.entity.ProductOptionItem;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.repository.StoreRepository;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemOptionRepository cartItemOptionRepository;

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final com.team.project.domain.product.repository.ProductOptionRepository productOptionRepository;
    private final com.team.project.domain.product.repository.ProductOptionItemRepository productOptionItemRepository;

    @Override
    @Transactional(readOnly = true)
    public GetCartResponse getCart(UUID userId) {
        Cart cart = cartRepository.findActiveCartDetailByUserId(userId)
                .orElseThrow(CartNotFoundException::new);

        cartRepository.fetchItemOptionsByCartId(cart.getId());

        return GetCartResponse.from(cart);
    }

    @Override
    @Transactional
    public AddCartItemResponse addCartItem(UUID userId, AddCartItemRequest request) {
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new InvalidCartQuantityException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("STORE_NOT_FOUND"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("PRODUCT_NOT_FOUND"));

        Cart cart = cartRepository.findActiveCartByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(new Cart(user, store)));

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new InvalidCartStatusException();
        }

        boolean cartReset = false;
        if (!cart.getStore().getId().equals(store.getId())) {
            cart.changeStoreAndClear(store);
            cartReset = true;
        } else {
            cart = cartRepository.findActiveCartDetailByUserId(user.getId())
                    .orElseThrow(CartNotFoundException::new);
            cartRepository.fetchItemOptionsByCartId(cart.getId());
        }

        List<AddCartItemRequest.CartItemOptionRequest> requestOptions = request.getOptions();
        validateAddCartItemOptions(product, requestOptions);

        CartItem existingItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .filter(cartItem -> isSameOptions(cartItem.getOptions(), requestOptions))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.changeQuantity(existingItem.getQuantity() + request.getQuantity());

            Cart saved = cartRepository.save(cart);

            return AddCartItemResponse.of(
                    saved,
                    existingItem.getId(),
                    product.getId(),
                    existingItem.getQuantity(),
                    cartReset
            );
        }

        CartItem item = new CartItem(product, request.getQuantity());

        if (requestOptions != null) {
            for (AddCartItemRequest.CartItemOptionRequest optReq : requestOptions) {
                ProductOption option = productOptionRepository.findById(optReq.getProductOptionId())
                        .orElseThrow(() -> new IllegalArgumentException("PRODUCT_OPTION_NOT_FOUND"));

                ProductOptionItem optionItem = productOptionItemRepository.findById(optReq.getProductOptionItemId())
                        .orElseThrow(() -> new IllegalArgumentException("PRODUCT_OPTION_ITEM_NOT_FOUND"));

                CartItemOption cio = new CartItemOption(option, optionItem);
                item.addOption(cio);
            }
        }

        cart.addItem(item);
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
    public UpdateCartItemResponse updateCartItem(UUID userId, UUID cartId, UUID itemId, UpdateCartItemRequest request) {
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new InvalidCartQuantityException();
        }

        Cart cart = cartRepository.findCartDetailById(cartId)
                .orElseThrow(CartNotFoundException::new);

        cartRepository.fetchItemOptionsByCartId(cartId);

        if (!cart.getUser().getId().equals(userId)) {
            throw new CartForbiddenException();
        }

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new InvalidCartStatusException();
        }

        CartItem target = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(CartItemNotFoundException::new);

        target.changeQuantity(request.getQuantity());

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
        Cart cart = cartRepository.findActiveCartDetailByUserId(userId)
                .orElseThrow(CartNotFoundException::new);

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new InvalidCartStatusException();
        }

        boolean removed = cart.getItems().removeIf(i -> i.getId().equals(itemId));
        if (!removed) {
            throw new CartItemNotFoundException();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void clearCart(UUID userId) {
        Cart cart = cartRepository.findActiveCartByUserId(userId)
                .orElseThrow(CartNotFoundException::new);

        if (cart.getStatus() != CartStatus.ACTIVE) {
            throw new InvalidCartStatusException();
        }

        UUID cartId = cart.getId();

        cartItemOptionRepository.deleteAllByCartId(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cartRepository.deleteById(cartId);
    }

    private void validateAddCartItemOptions(
            Product product,
            List<AddCartItemRequest.CartItemOptionRequest> requestOptions
    ) {
        if (requestOptions == null || requestOptions.isEmpty()) {
            return;
        }

        for (AddCartItemRequest.CartItemOptionRequest optReq : requestOptions) {
            ProductOption option = productOptionRepository.findById(optReq.getProductOptionId())
                    .orElseThrow(() -> new IllegalArgumentException("PRODUCT_OPTION_NOT_FOUND"));

            ProductOptionItem optionItem = productOptionItemRepository.findById(optReq.getProductOptionItemId())
                    .orElseThrow(() -> new IllegalArgumentException("PRODUCT_OPTION_ITEM_NOT_FOUND"));

            if (!option.getProduct().getId().equals(product.getId())) {
                throw new IllegalArgumentException("PRODUCT_OPTION_NOT_MATCHED_TO_PRODUCT");
            }

            if (!optionItem.getProductOption().getId().equals(option.getId())) {
                throw new IllegalArgumentException("PRODUCT_OPTION_ITEM_NOT_MATCHED_TO_OPTION");
            }
        }
    }

    private boolean isSameOptions(
            List<CartItemOption> existingOptions,
            List<AddCartItemRequest.CartItemOptionRequest> requestOptions
    ) {
        if (existingOptions == null || existingOptions.isEmpty()) {
            return requestOptions == null || requestOptions.isEmpty();
        }

        if (requestOptions == null || requestOptions.isEmpty()) {
            return false;
        }

        if (existingOptions.size() != requestOptions.size()) {
            return false;
        }

        Set<String> existingSet = existingOptions.stream()
                .map(opt -> opt.getProductOption().getId() + ":" + opt.getProductOptionItem().getId())
                .collect(Collectors.toSet());

        Set<String> requestSet = requestOptions.stream()
                .map(opt -> opt.getProductOptionId() + ":" + opt.getProductOptionItemId())
                .collect(Collectors.toSet());

        return existingSet.equals(requestSet);
    }
}