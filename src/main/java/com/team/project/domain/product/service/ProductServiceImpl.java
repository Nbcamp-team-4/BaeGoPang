package com.team.project.domain.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.ai.api.request.ProcessAiRecommendRequest;
import com.team.project.domain.ai.api.response.SearchAiRecommendResponse;
import com.team.project.domain.ai.service.AiService;
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

    @Override
    public ProductResult createProduct(CreateProductCommand command) {
        Store store = storeRepository.findById(command.getStoreId())
            .orElseThrow(StoreNotFoundException::new);

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

            // 1. UUID 타입을 String으로 변환해서 3개의 인수를 채워줍니다.
            // 순서: (String query, String storeId, String category) 로 추정됩니다.
            ProcessAiRecommendRequest aiRequest = new ProcessAiRecommendRequest(
                prompt,
                store.getId().toString(), // UUID를 String으로 변환! 👈 핵심 수정 사항
                null                      // 세 번째 인수는 일단 null로 처리
            );

            // 2. 서비스 호출 (List 응답)
            List<SearchAiRecommendResponse> aiResponses = aiService.recommendMenu(aiRequest);

            // 3. record 타입에서 데이터 추출 (.description() 호출)
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

    @Override
    public ProductResult updateProduct(UpdateProductCommand command) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(command.getProductId())
            .orElseThrow(ProductNotFoundException::new);

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

            // 1. updateProduct에서는 store 대신 product.getStore()를 사용합니다.
            ProcessAiRecommendRequest aiRequest = new ProcessAiRecommendRequest(
                prompt,
                product.getStore().getId().toString(), // product에서 store를 꺼내서 ID를 String으로 변환!
                null
            );

            // 2. 서비스 호출
            List<SearchAiRecommendResponse> aiResponses = aiService.recommendMenu(aiRequest);

            // 3. record 타입 호출 (.description())
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

    @Override
    public void deleteProduct(UUID productId, UUID userId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        List<ProductOption> optionGroups =
            productOptionRepository.findAllByProductIdAndDeletedAtIsNull(productId);

        for (ProductOption optionGroup : optionGroups) {
            productOptionManager.deleteOptionGroup(optionGroup, userId);
        }

        product.delete(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> getProducts(UUID storeId) {
        return productRepository
            .findAllByStoreIdAndDeletedAtIsNullAndIsHiddenFalseAndIsSoldOutFalse(storeId)
            .stream()
            .map(ProductResult::from)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GetProductResult getProduct(UUID productId) {
        Product product = getActiveProduct(productId);
        return toGetProductResult(product);
    }

    @Override
    @Transactional(readOnly = true)
    public GetProductResult getProductForAdmin(UUID productId) {
        Product product = getProductIncludingHiddenAndSoldOut(productId);
        return toGetProductResult(product);
    }

    @Override
    public ProductResult markSoldOut(UUID productId, UUID userId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.markSoldOut();
        return ProductResult.from(product);
    }

    @Override
    public ProductResult markAvailable(UUID productId, UUID userId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.markAvailable();
        return ProductResult.from(product);
    }

    @Override
    public ProductResult hideProduct(UUID productId, UUID userId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.hide();
        return ProductResult.from(product);
    }

    @Override
    public ProductResult unhideProduct(UUID productId, UUID userId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(ProductNotFoundException::new);

        product.unhide();
        return ProductResult.from(product);
    }

    private Product getActiveProduct(UUID productId) {
        return productRepository
            .findByIdAndDeletedAtIsNullAndIsHiddenFalseAndIsSoldOutFalse(productId)
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