package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.Mappers.ProductMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.CategoryModel;
import com.sufaltalukder.Models.LanguageModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.SubCategoryModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
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
	private AuthUserRepository authUserRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Override
	public ApiResponse<ProductDTO> createProduct(long authUserId, long categoryId, long subCategoryId, long languageId,
			ProductModel productModel) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		LanguageModel language = languageRepository.findById(languageId)
				.orElseThrow(() -> new RuntimeException("Language not found"));

		CategoryModel category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		SubCategoryModel subCategory = subCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new RuntimeException("Subcategory not found"));

		ProductModel isProductNameExist = productRepository.findByProductName(productModel.getProductName());

		if (isProductNameExist != null) {
			return new ApiResponse<>("exist", "Product name already exist!", null);
		}

		ProductModel savingData = new ProductModel();
		savingData.setAuthUserInfo(authUser);
		savingData.setCategoryInfo(category);
		savingData.setSubCategoryInfo(subCategory);
		savingData.setLanguageInfo(language);
		savingData.setProductName(productModel.getProductName());
		savingData.setProductBrand(productModel.getProductBrand());
		savingData.setProductCode(productModel.getProductCode());
		savingData.setProductAvailability(productModel.getProductAvailability());
		savingData.setProductPrice(productModel.getProductPrice());
		savingData.setProductDetails(productModel.getProductDetails());
		savingData.setProductStock(productModel.getProductStock());
		savingData.setProductActive(productModel.getProductActive());

		ProductModel savedData = productRepository.save(savingData);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Product created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

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

		Optional<ProductModel> isProductIdExist = productRepository.findProductByIdOfACSL(productId);

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
	public ApiResponse<List<ProductDTO>> getAllProducts() {

		List<ProductModel> products = productRepository.findAllProducts();

		if (products.isEmpty()) {
			return new ApiResponse<>("not found", "Product(s) not found.", null);
		}

		List<ProductDTO> dtos = products.stream().map(ProductMapper::toDTO).toList();

		return new ApiResponse<>("success", "All products fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<ProductDTO> updateProduct(long authUserId, long productId, long categoryId, long subCategoryId,
			long languageId, ProductModel productModel) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		LanguageModel language = languageRepository.findById(languageId)
				.orElseThrow(() -> new RuntimeException("Language not found"));

		CategoryModel category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		SubCategoryModel subCategory = subCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new RuntimeException("Subcategory not found"));

		Optional<ProductModel> isProductIdExist = productRepository.findById(productId);

		if (isProductIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "No product found.", null);
		}

		ProductModel isProductNameExist = productRepository.findByProductName(productModel.getProductName());

		if (isProductNameExist != null && isProductNameExist.getProductId() != productId) {
			return new ApiResponse<>("exist", "Product name already exist!", null);
		}

		ProductModel existingProduct = isProductIdExist.get();
		existingProduct.setAuthUserInfo(authUser);
		existingProduct.setCategoryInfo(category);
		existingProduct.setSubCategoryInfo(subCategory);
		existingProduct.setLanguageInfo(language);
		existingProduct.setProductName(productModel.getProductName());
		existingProduct.setProductBrand(productModel.getProductBrand());
		existingProduct.setProductCode(productModel.getProductCode());
		existingProduct.setProductAvailability(productModel.getProductAvailability());
		existingProduct.setProductPrice(productModel.getProductPrice());
		existingProduct.setProductDetails(productModel.getProductDetails());
		existingProduct.setProductStock(productModel.getProductStock());
		existingProduct.setProductActive(productModel.getProductActive());
		existingProduct.setProductUpdatedAt(ZonedDateTime.now());

		ProductModel updatedData = productRepository.save(existingProduct);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("Product updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

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
	public ApiResponse<List<ProductDTO>> getAllProductsFilterByLanguage(long languageId) {

		List<ProductModel> filteredProducts = productRepository.findProductsByLanguageId(languageId);

		if (filteredProducts.isEmpty()) {
			return new ApiResponse<>("not found", "No products found for Language ID: " + languageId, null);
		}

		List<ProductDTO> dtos = filteredProducts.stream().map(ProductMapper::toDTO).toList();

		return new ApiResponse<>("success", "Products fetched successfully.", dtos);
	}

	// Filter by Category
	@Override
	public ApiResponse<List<ProductDTO>> getAllProductsFilterByCategory(long categoryId) {

		List<ProductModel> filteredProducts = productRepository.findProductsByCategoryId(categoryId);

		if (filteredProducts.isEmpty()) {
			return new ApiResponse<>("not found", "No products found for Category ID: " + categoryId, null);
		}

		List<ProductDTO> dtos = filteredProducts.stream().map(ProductMapper::toDTO).toList();

		return new ApiResponse<>("success", "Products fetched successfully.", dtos);
	}

	// Filter by SubCategory
	@Override
	public ApiResponse<List<ProductDTO>> getAllProductsFilterBySubCategory(long subCategoryId) {

		List<ProductModel> filteredProducts = productRepository.findProductsBySubCategoryId(subCategoryId);

		if (filteredProducts.isEmpty()) {
			return new ApiResponse<>("not found", "No products found for SubCategory ID: " + subCategoryId, null);
		}

		List<ProductDTO> dtos = filteredProducts.stream().map(ProductMapper::toDTO).toList();

		return new ApiResponse<>("success", "Products fetched successfully.", dtos);
	}

}