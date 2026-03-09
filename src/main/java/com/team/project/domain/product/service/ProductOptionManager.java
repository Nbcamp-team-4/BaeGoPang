package com.team.project.domain.product.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.product.entity.Product;
import com.team.project.domain.product.entity.ProductOption;
import com.team.project.domain.product.entity.ProductOptionItem;
import com.team.project.domain.product.exception.ProductOptionItemNotFoundException;
import com.team.project.domain.product.exception.ProductOptionNotFoundException;
import com.team.project.domain.product.repository.ProductOptionItemRepository;
import com.team.project.domain.product.repository.ProductOptionRepository;
import com.team.project.domain.product.service.command.UpdateProductCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOptionManager {

	private final ProductOptionRepository productOptionRepository;
	private final ProductOptionItemRepository productOptionItemRepository;

	public void syncOptionGroups(
		Product product,
		List<UpdateProductCommand.UpdateOptionGroupCommand> requestedOptions
	) {
		List<UpdateProductCommand.UpdateOptionGroupCommand> requestOptions =
			requestedOptions == null ? List.of() : requestedOptions;

		List<ProductOption> existingOptions =
			productOptionRepository.findAllByProductIdAndDeletedAtIsNull(product.getId());

		Map<UUID, ProductOption> existingOptionMap = existingOptions.stream()
			.collect(Collectors.toMap(ProductOption::getId, option -> option));

		Set<UUID> requestedOptionIds = requestOptions.stream()
			.map(UpdateProductCommand.UpdateOptionGroupCommand::getOptionId)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());

		for (ProductOption existingOption : existingOptions) {
			if (!requestedOptionIds.contains(existingOption.getId())) {
				deleteOptionGroup(existingOption, null);
			}
		}

		for (UpdateProductCommand.UpdateOptionGroupCommand requestOption : requestOptions) {
			if (requestOption.getOptionId() == null) {
				ProductOption newOption = ProductOption.create(
					product,
					requestOption.getName(),
					Boolean.TRUE.equals(requestOption.getIsRequired())
				);

				ProductOption savedOption = productOptionRepository.save(newOption);
				syncOptionItems(savedOption, requestOption.getItems());
				continue;
			}

			ProductOption existingOption = existingOptionMap.get(requestOption.getOptionId());
			if (existingOption == null) {
				throw new ProductOptionNotFoundException();
			}

			existingOption.update(
				requestOption.getName(),
				requestOption.getIsRequired()
			);

			syncOptionItems(existingOption, requestOption.getItems());
		}
	}

	public void deleteOptionGroup(ProductOption optionGroup, UUID userId) {
		List<ProductOptionItem> items =
			productOptionItemRepository.findAllByProductOptionIdAndDeletedAtIsNull(optionGroup.getId());

		for (ProductOptionItem item : items) {
			item.delete(userId);
		}

		optionGroup.delete(userId);
	}

	private void syncOptionItems(
		ProductOption productOption,
		List<UpdateProductCommand.UpdateOptionItemCommand> requestedItems
	) {
		List<UpdateProductCommand.UpdateOptionItemCommand> requestItems =
			requestedItems == null ? List.of() : requestedItems;

		List<ProductOptionItem> existingItems =
			productOptionItemRepository.findAllByProductOptionIdAndDeletedAtIsNull(productOption.getId());

		Map<UUID, ProductOptionItem> existingItemMap = existingItems.stream()
			.collect(Collectors.toMap(ProductOptionItem::getId, item -> item));

		Set<UUID> requestedItemIds = requestItems.stream()
			.map(UpdateProductCommand.UpdateOptionItemCommand::getItemId)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());

		for (ProductOptionItem existingItem : existingItems) {
			if (!requestedItemIds.contains(existingItem.getId())) {
				existingItem.delete(null);
			}
		}

		for (UpdateProductCommand.UpdateOptionItemCommand requestItem : requestItems) {
			if (requestItem.getItemId() == null) {
				ProductOptionItem newItem = ProductOptionItem.create(
					productOption,
					requestItem.getName(),
					requestItem.getAdditionalPrice()
				);
				productOptionItemRepository.save(newItem);
				continue;
			}

			ProductOptionItem existingItem = existingItemMap.get(requestItem.getItemId());
			if (existingItem == null) {
				throw new ProductOptionItemNotFoundException();
			}

			existingItem.update(
				requestItem.getName(),
				requestItem.getAdditionalPrice()
			);
		}
	}
}