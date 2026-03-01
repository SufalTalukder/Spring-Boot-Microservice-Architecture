package com.sufaltalukder.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.DTOs.RequestProductDTO;
import com.sufaltalukder.Mappers.ProductMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.CategoryModel;
import com.sufaltalukder.Models.LanguageModel;
import com.sufaltalukder.Models.NotificationModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.SubCategoryModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.CategoryRepository;
import com.sufaltalukder.Repositories.LanguageRepository;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.SubCategoryRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;
import com.sufaltalukder.feign.Services.NotificationFeignService;

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

	@Autowired
	private NotificationFeignService notificationFeignService;

	private final String UPLOAD_DIR = "uploads";

	@Override
	public ApiResponse<ProductDTO> createProduct(long authUserId, long categoryId, long subCategoryId, long languageId,
			RequestProductDTO productInfo, MultipartFile productImage) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		LanguageModel language = languageRepository.findById(languageId)
				.orElseThrow(() -> new RuntimeException("Language not found"));

		CategoryModel category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found"));

		SubCategoryModel subCategory = subCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new RuntimeException("Subcategory not found"));

		ProductModel isProductNameExist = productRepository.findByProductName(productInfo.getProductName());

		if (isProductNameExist != null) {
			return new ApiResponse<>("exist", "Product name already exists!", null);
		}

		ProductModel savingData = new ProductModel();
		savingData.setAuthUserInfo(authUser);
		savingData.setCategoryInfo(category);
		savingData.setSubCategoryInfo(subCategory);
		savingData.setLanguageInfo(language);
		savingData.setProductName(productInfo.getProductName());
		savingData.setProductBrand(productInfo.getProductBrand());
		savingData.setProductCode(productInfo.getProductCode());
		savingData.setProductAvailability(productInfo.getProductAvailability());
		savingData.setProductPrice(productInfo.getProductPrice());
		savingData.setProductDetails(productInfo.getProductDetails());
		savingData.setProductStock(productInfo.getProductStock());
		savingData.setProductActive(productInfo.getProductActive());

		ProductModel savedData = productRepository.save(savingData);

		// Upload image using newly created productId
		if (productImage != null && !productImage.isEmpty()) {
			String imageName = storeProductImage(savedData.getProductId(), productImage);
			savedData.setProductImage(imageName);
			productRepository.save(savedData);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Product created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		// Push data inside notificationFeignService
		NotificationModel notificationData = new NotificationModel();
		notificationData.setAuthUserId(authUserId);
		notificationData.setUserId(0);
		notificationData.setNotificationTitle("New Product Added");
		notificationData
				.setNotificationDescription("Product #" + savedData.getProductId() + " has been created successfully.");
		notificationFeignService.pushMgmtNotification(notificationData);

		return new ApiResponse<>("success", "Product created successfully.", ProductMapper.toDTO(savedData));
	}

	private String storeProductImage(long categoryId, MultipartFile file) {
		try {
			Path uploadPath = Paths.get(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			String fileName = categoryId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

			Path filePath = uploadPath.resolve(fileName);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return fileName;

		} catch (IOException e) {
			throw new RuntimeException("Failed to store product image", e);
		}
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
	public ApiResponse<List<ProductDTO>> getAllProducts(long categoryId, long subCategoryId, long languageId) {

		List<ProductModel> products = productRepository.findProductsByFilters(categoryId > 0 ? categoryId : null,
				subCategoryId > 0 ? subCategoryId : null, languageId > 0 ? languageId : null);

		if (products == null || products.isEmpty()) {
			return new ApiResponse<>("not found", "Product(s) not found.", null);
		}

		List<ProductDTO> dtos = products.stream().map(ProductMapper::toDTO).toList();

		return new ApiResponse<>("success", "All products fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<ProductDTO> updateProduct(long authUserId, long productId, long categoryId, long subCategoryId,
			long languageId, RequestProductDTO productInfo, MultipartFile productImage) {

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

		ProductModel isProductNameExist = productRepository.findByProductName(productInfo.getProductName());

		if (isProductNameExist != null && isProductNameExist.getProductId() != productId) {
			return new ApiResponse<>("exist", "Product name already exist!", null);
		}

		ProductModel existingProduct = isProductIdExist.get();
		existingProduct.setAuthUserInfo(authUser);
		existingProduct.setCategoryInfo(category);
		existingProduct.setSubCategoryInfo(subCategory);
		existingProduct.setLanguageInfo(language);
		existingProduct.setProductName(productInfo.getProductName());
		existingProduct.setProductBrand(productInfo.getProductBrand());
		existingProduct.setProductCode(productInfo.getProductCode());
		existingProduct.setProductAvailability(productInfo.getProductAvailability());
		existingProduct.setProductPrice(productInfo.getProductPrice());
		existingProduct.setProductDetails(productInfo.getProductDetails());
		existingProduct.setProductStock(productInfo.getProductStock());
		existingProduct.setProductActive(productInfo.getProductActive());

		ProductModel updatedData = productRepository.save(existingProduct);

		// Upload image using newly created productId
		if (productImage != null && !productImage.isEmpty()) {
			String imageName = storeProductImage(updatedData.getProductId(), productImage);
			updatedData.setProductImage(imageName);
			productRepository.save(updatedData);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("Product updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		// Push data inside notificationFeignService
		NotificationModel notificationData = new NotificationModel();
		notificationData.setAuthUserId(authUserId);
		notificationData.setUserId(0);
		notificationData.setNotificationTitle("Product Updated");
		notificationData.setNotificationDescription(
				"Product #" + updatedData.getProductId() + " has been updated successfully.");
		notificationFeignService.pushMgmtNotification(notificationData);

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

		// Push data inside notificationFeignService
		NotificationModel notificationData = new NotificationModel();
		notificationData.setAuthUserId(authUserId);
		notificationData.setUserId(0);
		notificationData.setNotificationTitle("Product Deleted");
		notificationData.setNotificationDescription("Product #" + productId + " has been deleted successfully.");
		notificationFeignService.pushMgmtNotification(notificationData);

		productRepository.deleteById(productId);
		return new ApiResponse<>("success", "Product deleted successfully.", null);
	}
}