package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.Mappers.ProductMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CategoryModel;
import com.sufaltalukder.Models.LanguageModel;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.SubCategoryModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.CategoryRepository;
import com.sufaltalukder.Repositories.LanguageRepository;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.SubCategoryRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class ProductMgmtServiceImpl implements ProductMgmtService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Override
	public ApiResponse<ProductDTO> createProduct(ProductModel productModel) {
		Optional<LanguageModel> isLanguageIdExist = languageRepository.findById(productModel.getLanguageId());

		if (isLanguageIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Language not found.", null);
		}

		Optional<CategoryModel> isCategoryIdExist = categoryRepository.findById(productModel.getCategoryId());

		if (isCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Category not found.", null);
		}

		Optional<SubCategoryModel> isSubCategoryIdExist = subCategoryRepository
				.findById(productModel.getSubCategoryId());

		if (isSubCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "SubCategory not found.", null);
		}

		ProductModel isProductNameExist = productRepository.findByProductName(productModel.getProductName());

		if (isProductNameExist != null) {
			return new ApiResponse<>("exist", "Product name already exist!", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(productModel.getAuthUserId());
		actionLogData.setAuthUserId(productModel.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Product created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		ProductModel savedData = productRepository.save(productModel);
		return new ApiResponse<>("success", "Product created successfully.", ProductMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<List<ProductDTO>> createMultipleProduct(long authUserId, List<ProductModel> productModels) {
		if (productModels == null || productModels.isEmpty()) {
			return new ApiResponse<>("not found", "Product list is empty.", null);
		}

		// Collect all product names from the input list
		List<String> productNames = productModels.stream().map(ProductModel::getProductName).toList();

		// Fetch existing products by names
		List<ProductModel> existingProducts = productRepository.findByProductNameIn(productNames);

		// Collect existing names
		List<String> existingNames = existingProducts.stream().map(ProductModel::getProductName).toList();

		if (!existingNames.isEmpty()) {
			return new ApiResponse<>("exist", "Some product name(s) already exist: " + existingNames, null);
		}

		List<ProductModel> savedMultipleData = productRepository.saveAll(productModels);

		List<ProductDTO> dtos = savedMultipleData.stream().map(ProductMapper::toDTO).toList();

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Multiple product(s) created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Multiple product(s) created successfully.", dtos);
	}

	@Override
	public ApiResponse<ProductDTO> getProduct(long authUserId, long productId) {
		Optional<ProductModel> isProductIdExist = productRepository.findById(productId);

		if (isProductIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "No product found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("Product fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Product fetched successfully.",
				ProductMapper.toDTO(isProductIdExist.get()));
	}

	@Override
	public PaginationApiResponse<List<ProductDTO>> getAllProducts(int pageNo, int pageSize) {
		Page<ProductModel> products = productRepository.findAll(PageRequest.of(pageNo - 1, pageSize));

		if (products.isEmpty()) {
			return new PaginationApiResponse<>("not found", "Product(s) not found.", null, 0, 0, 0);
		}

		List<ProductDTO> dtos = products.stream().map(ProductMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "All products fetched successfully.", dtos,
				products.getNumber() + 1, products.getSize(), products.getTotalElements());
	}

	@Override
	public ApiResponse<ProductDTO> updateProduct(long productId, ProductModel productModel) {
		Optional<ProductModel> isProductIdExist = productRepository.findById(productId);

		if (isProductIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "No product found.", null);
		}

		ProductModel isProductNameExist = productRepository.findByProductName(productModel.getProductName());

		if (isProductNameExist != null && isProductNameExist.getLanguageId() != productId) {
			return new ApiResponse<>("exist", "Product name already exist!", null);
		}

		ProductModel existingProduct = isProductIdExist.get();
		existingProduct.setLanguageId(productModel.getLanguageId());
		existingProduct.setCategoryId(productModel.getCategoryId());
		existingProduct.setSubCategoryId(productModel.getSubCategoryId());
		existingProduct.setProductName(productModel.getProductName());
		existingProduct.setProductBrand(productModel.getProductBrand());
		existingProduct.setProductCode(productModel.getProductCode());
		existingProduct.setProductAvailability(productModel.getProductAvailability());
		existingProduct.setProductPrice(productModel.getProductPrice());
		existingProduct.setProductDetails(productModel.getProductDetails());
		existingProduct.setProductStock(productModel.getProductStock());
		existingProduct.setProductActive(productModel.getProductActive());
		existingProduct.setProductUpdatedAt(ZonedDateTime.now());

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(productModel.getAuthUserId());
		actionLogData.setAuthUserId(productModel.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("Product updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		ProductModel updatedData = productRepository.save(existingProduct);
		return new ApiResponse<>("success", "Product updated successfully.", ProductMapper.toDTO(updatedData));
	}

	@Override
	public ApiResponse<ProductDTO> deleteProduct(long authUserId, long productId) {
		Optional<ProductModel> isProductIdExist = productRepository.findById(productId);

		if (isProductIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "No product found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("Product deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		productRepository.deleteById(productId);
		return new ApiResponse<>("success", "Product deleted successfully.", null);
	}

	@Override
	public ApiResponse<List<ProductDTO>> getSearchedResults(String q) {
		List<ProductModel> fetchedSearchedResults = productRepository.findSearchedResultsByQuery(q);

		if (fetchedSearchedResults.isEmpty()) {
			return new ApiResponse<>("not found", "No results found.", null);
		}

		List<ProductDTO> dtos = fetchedSearchedResults.stream().map(ProductMapper::toDTO).toList();
		return new ApiResponse<>("success", "Results fetched successfully.", dtos);
	}

	// Filter by Language
	@Override
	public PaginationApiResponse<List<ProductDTO>> getAllProductsFilterByLanguage(long languageId, int pageNo,
			int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		Page<ProductModel> filteredProducts = productRepository.findByLanguageId(languageId, pageable);

		if (filteredProducts.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No products found for Language ID: " + languageId, null, 0,
					0, 0);
		}

		List<ProductDTO> dtos = filteredProducts.stream().map(ProductMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "Products fetched successfully.", dtos,
				filteredProducts.getNumber() + 1, filteredProducts.getSize(), filteredProducts.getTotalElements());
	}

	// Filter by Category
	@Override
	public PaginationApiResponse<List<ProductDTO>> getAllProductsFilterByCategory(long categoryId, int pageNo,
			int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		Page<ProductModel> filteredProducts = productRepository.findByCategoryId(categoryId, pageable);

		if (filteredProducts.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No products found for Category ID: " + categoryId, null, 0,
					0, 0);
		}

		List<ProductDTO> dtos = filteredProducts.stream().map(ProductMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "Products fetched successfully.", dtos,
				filteredProducts.getNumber() + 1, filteredProducts.getSize(), filteredProducts.getTotalElements());
	}

	// Filter by SubCategory
	@Override
	public PaginationApiResponse<List<ProductDTO>> getAllProductsFilterBySubCategory(long subCategoryId, int pageNo,
			int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		Page<ProductModel> filteredProducts = productRepository.findBySubCategoryId(subCategoryId, pageable);

		if (filteredProducts.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No products found for SubCategory ID: " + subCategoryId,
					null, 0, 0, 0);
		}

		List<ProductDTO> dtos = filteredProducts.stream().map(ProductMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "Products fetched successfully.", dtos,
				filteredProducts.getNumber() + 1, filteredProducts.getSize(), filteredProducts.getTotalElements());
	}

}