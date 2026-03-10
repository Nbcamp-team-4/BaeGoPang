package com.team.project.domain.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;
import com.team.project.domain.ai.service.AiService;
import com.team.project.domain.product.api.request.ProductSearchRequest;
import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.entity.ProductAiLog;
import com.team.project.domain.product.entity.ProductOption;
import com.team.project.domain.product.entity.ProductOptionItem;
import com.team.project.domain.product.exception.ProductNotFoundException;
import com.team.project.domain.product.repository.ProductAiLogRepository;
import com.team.project.domain.product.repository.ProductOptionItemRepository;
import com.team.project.domain.product.repository.ProductOptionRepository;
import com.team.project.domain.product.repository.ProductRepository;
import com.team.project.domain.product.service.command.CreateProductCommand;
import com.team.project.domain.product.service.command.UpdateProductCommand;
import com.team.project.domain.product.service.result.GetProductResult;
import com.team.project.domain.product.service.result.ProductResult;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.exception.StoreNotFoundException;
import com.team.project.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionItemRepository productOptionItemRepository;
    private final StoreRepository storeRepository;
    private final ProductOptionManager productOptionManager;
    private final ProductAiLogRepository productAiLogRepository;
    private final AiService aiService;


    //상품 생성
    @Override
    public ProductResult createProduct(UUID userId, String role, CreateProductCommand command) {
        Store store = storeRepository.findById(command.getStoreId())
            .orElseThrow(StoreNotFoundException::new);

        validateStoreAuthority(store, userId, role);

        String description = command.getDescription();
        String prompt = null;
        String aiResponse = null;

        if (Boolean.TRUE.equals(command.getUseAiDescription())) {
            prompt = """
                다음 메뉴의 상품 설명을 작성해줘.
                메뉴명: %s
                조건:
                - 배달앱 메뉴 설명
                - 한글
                - 50자 이하
                - 설명만 출력
                """.formatted(command.getName());

            ProcessAiRecommendRequest aiRequest = new ProcessAiRecommendRequest(
                prompt,
                store.getId().toString(),
                null
            );

            List<SearchAiRecommendResponse> aiResponses = aiService.recommendMenu(aiRequest);

            if (aiResponses != null && !aiResponses.isEmpty()) {
                aiResponse = aiResponses.get(0).description();
            }

            description = normalizeDescription(aiResponse);
        }

        Product product = Product.create(
            store,
            command.getName(),
            command.getPrice(),
            description,
            command.getUseAiDescription(),
            command.getImageUrl()
        );

        Product savedProduct = productRepository.save(product);

        if (Boolean.TRUE.equals(command.getUseAiDescription()) && aiResponse != null) {
            productAiLogRepository.save(
                ProductAiLog.create(
                    savedProduct.getId(),
                    prompt,
                    aiResponse,
                    "spring-ai-chatclient",
                    null
                )
            );
        }

        return ProductResult.from(savedProduct);
    }

    //상품 업데이트
    @Override
    public ProductResult updateProduct(UUID userId, String role, UpdateProductCommand command) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(command.getProductId())
            .orElseThrow(ProductNotFoundException::new);

        validateProductAuthority(product, userId, role);

        String description = command.getDescription();
        String prompt = null;
        String aiResponse = null;

        if (Boolean.TRUE.equals(command.getUseAiDescription())) {
            prompt = """
                다음 메뉴의 상품 설명을 작성해줘.
                메뉴명: %s
                조건:
                - 배달앱 메뉴 설명
                - 한글
                - 50자 이하
                - 설명만 출력
                """.formatted(command.getName());

            ProcessAiRecommendRequest aiRequest = new ProcessAiRecommendRequest(
                prompt,
                product.getStore().getId().toString(),
                null
            );

            List<SearchAiRecommendResponse> aiResponses = aiService.recommendMenu(aiRequest);

            if (aiResponses != null && !aiResponses.isEmpty()) {
                aiResponse = aiResponses.get(0).description();
            }

            description = normalizeDescription(aiResponse);
        }

        product.update(
            command.getName(),
            command.getPrice(),
            description,
            command.getUseAiDescription(),
            command.getImageUrl()
        );

        productOptionManager.syncOptionGroups(product, command.getOptions());

        if (Boolean.TRUE.equals(command.getUseAiDescription()) && aiResponse != null) {
            productAiLogRepository.save(
                ProductAiLog.create(
                    product.getId(),
                    prompt,
                    aiResponse,
                    "spring-ai-chatclient",
                    null
                )
            );
        }

        return ProductResult.from(product);
    }


    //상품삭제
    @Override
    public void deleteProduct(UUID productId, UUID userId, String role) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        validateProductAuthority(product, userId, role);

        List<ProductOption> optionGroups =
            productOptionRepository.findAllByProductIdAndDeletedAtIsNull(productId);

        for (ProductOption optionGroup : optionGroups) {
            productOptionManager.deleteOptionGroup(optionGroup, userId);
        }

        product.delete(userId);
    }

    //사용자용 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> getProducts(UUID storeId) {
        return productRepository
            .findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalse(storeId)
            .stream()
            .map(ProductResult::from)
            .toList();
    }
    //관리자용 목록 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResult> getProductsForAdmin(UUID userId, String role, ProductSearchRequest request) {
        if (request.getStoreId() == null) {
            throw new StoreNotFoundException();
        }

        Store store = storeRepository.findById(request.getStoreId())
            .orElseThrow(StoreNotFoundException::new);

        validateStoreAuthority(store, userId, role);

        Pageable pageable = PageRequest.of(
            request.getPage(),
            request.getSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Product> productPage;
        String keyword = request.getKeyword();

        if (keyword == null || keyword.isBlank()) {
            productPage = productRepository.findAllByStoreIdAndDeletedAtIsNull(request.getStoreId(), pageable);
        } else {
            productPage = productRepository.findAllByStoreIdAndDeletedAtIsNullAndNameContainingIgnoreCase(
                request.getStoreId(),
                keyword.trim(),
                pageable
            );
        }

        return productPage.map(ProductResult::from);
    }
    //사용자 단건 조회
    @Override
    @Transactional(readOnly = true)
    public GetProductResult getProduct(UUID productId) {
        Product product = getActiveProduct(productId);
        return toGetProductResult(product);
    }
    //관리자 단건 조회
    @Override
    @Transactional(readOnly = true)
    public GetProductResult getProductForAdmin(UUID productId) {
        Product product = getProductIncludingHiddenAndSoldOut(productId);
        return toGetProductResult(product);
    }

    @Override
    public ProductResult markSoldOut(UUID productId, UUID userId, String role) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        validateProductAuthority(product, userId, role);

        product.markSoldOut();
        return ProductResult.from(product);
    }

    @Override
    public ProductResult markAvailable(UUID productId, UUID userId, String role) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        validateProductAuthority(product, userId, role);

        product.markAvailable();
        return ProductResult.from(product);
    }

    @Override
    public ProductResult hideProduct(UUID productId, UUID userId, String role) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        validateProductAuthority(product, userId, role);

        product.hide();
        return ProductResult.from(product);
    }

    @Override
    public ProductResult unhideProduct(UUID productId, UUID userId, String role) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        validateProductAuthority(product, userId, role);

        product.unhide();
        return ProductResult.from(product);
    }

    private void validateStoreAuthority(Store store, UUID userId, String role) {
        if (isManagerOrMaster(role)) {
            return;
        }

        if (!"OWNER".equals(role)) {
            throw new AccessDeniedException("해당 작업에 대한 권한이 없습니다.");
        }

        if (store.getUser() == null || store.getUser().getId() == null) {
            throw new AccessDeniedException("가게 소유자 정보가 없습니다.");
        }

        if (!store.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인 가게의 상품만 등록할 수 있습니다.");
        }
    }

    private void validateProductAuthority(Product product, UUID userId, String role) {
        if (isManagerOrMaster(role)) {
            return;
        }

        if (!"OWNER".equals(role)) {
            throw new AccessDeniedException("해당 작업에 대한 권한이 없습니다.");
        }

        Store store = product.getStore();

        if (store == null || store.getUser() == null || store.getUser().getId() == null) {
            throw new AccessDeniedException("가게 소유자 정보가 없습니다.");
        }

        if (!store.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인 가게의 상품만 수정할 수 있습니다.");
        }
    }

    private boolean isManagerOrMaster(String role) {
        return "MANAGER".equals(role) || "MASTER".equals(role);
    }

    private Product getActiveProduct(UUID productId) {
        return productRepository
            .findByIdAndDeletedAtIsNullAndIsHiddenFalse(productId)
            .orElseThrow(ProductNotFoundException::new);
    }

    private Product getProductIncludingHiddenAndSoldOut(UUID productId) {
        return productRepository
            .findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);
    }

    private GetProductResult toGetProductResult(Product product) {
        List<ProductOption> optionGroups =
            productOptionRepository.findAllByProductIdAndDeletedAtIsNull(product.getId());

        List<GetProductResult.OptionGroup> options = optionGroups.stream()
            .map(option -> {
                List<ProductOptionItem> optionItems =
                    productOptionItemRepository.findAllByProductOptionIdAndDeletedAtIsNull(option.getId());

                List<GetProductResult.OptionItem> items = optionItems.stream()
                    .map(item -> GetProductResult.OptionItem.builder()
                        .itemId(item.getId())
                        .name(item.getName())
                        .additionalPrice(item.getAdditionalPrice())
                        .build())
                    .toList();

                return GetProductResult.OptionGroup.builder()
                    .optionId(option.getId())
                    .name(option.getName())
                    .isRequired(option.isRequired())
                    .items(items)
                    .build();
            })
            .toList();

        return GetProductResult.builder()
            .id(product.getId())
            .storeId(product.getStore().getId())
            .name(product.getName())
            .price(product.getPrice())
            .description(product.getDescription())
            .useAiDescription(product.isUseAiDescription())
            .imageUrl(product.getImageUrl())
            .isSoldOut(product.isSoldOut())
            .isHidden(product.isHidden())
            .options(options)
            .build();
    }

    private String normalizeDescription(String text) {

        if (text == null || text.isBlank()) {
            return "상품 설명이 준비 중입니다.";
        }

        String normalized = text.replace("\n", " ").trim();

        if (normalized.length() > 50) {
            normalized = normalized.substring(0, 50);
        }

        return normalized;
    }
}