package com.sufaltalukder.config;

import com.sufaltalukder.Models.*;
import com.sufaltalukder.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public void run(String... args) throws Exception {
        // যদি ডাটাবেস সম্পূর্ণ ফাঁকা থাকে, তবেই ডামি ডাটা ইনসার্ট করবে
        if (productRepository.count() == 0) {
            System.out.println("🌱 Database is empty! Seeding dummy data for AI testing...");

            // 1. Fetch or Create User
            AuthUserModel user = null;
            java.util.List<AuthUserModel> users = authUserRepository.findAll();
            if (!users.isEmpty()) {
                user = users.get(0);
            } else {
                user = new AuthUserModel();
                user.setAuthUserName("Sufal Talukder");
                user.setAuthUserEmailAddress("admin_" + System.currentTimeMillis() + "@example.com"); // Unique email
                user.setAuthUserActive(AuthUserModel.AuthUserActive.YES);
                user.setAuthUserType(AuthUserModel.AuthUserType.ADMIN);
                user = authUserRepository.save(user);
            }

            // 2. Fetch or Create Language
            LanguageModel lang = null;
            java.util.List<LanguageModel> langs = languageRepository.findAll();
            if (!langs.isEmpty()) {
                lang = langs.get(0);
            } else {
                lang = new LanguageModel();
                lang.setLanguageName("English");
                lang.setAuthUserId(user.getAuthUserId());
                lang.setLanguageActive(LanguageModel.LanguageActive.YES);
                lang = languageRepository.save(lang);
            }

            // 3. Fetch or Create Categories
            CategoryModel catFashion = null;
            CategoryModel catElectronics = null;
            java.util.List<CategoryModel> cats = categoryRepository.findAll();
            
            if (cats.size() >= 2) {
                catFashion = cats.get(0);
                catElectronics = cats.get(1);
            } else {
                catFashion = new CategoryModel();
                catFashion.setCategoryName("Fashion");
                catFashion.setAuthUserId(user.getAuthUserId());
                catFashion.setCategoryActive(CategoryModel.CategoryActive.YES);
                catFashion = categoryRepository.save(catFashion);

                catElectronics = new CategoryModel();
                catElectronics.setCategoryName("Electronics");
                catElectronics.setAuthUserId(user.getAuthUserId());
                catElectronics.setCategoryActive(CategoryModel.CategoryActive.YES);
                catElectronics = categoryRepository.save(catElectronics);
            }

            // 4. Fetch or Create SubCategories
            SubCategoryModel subCatShoes = null;
            SubCategoryModel subCatGadgets = null;
            java.util.List<SubCategoryModel> subCats = subCategoryRepository.findAll();
            
            if (subCats.size() >= 2) {
                subCatShoes = subCats.get(0);
                subCatGadgets = subCats.get(1);
            } else {
                subCatShoes = new SubCategoryModel();
                subCatShoes.setSubCategoryName("Shoes");
                subCatShoes.setAuthUserId(user.getAuthUserId());
                subCatShoes.setSubCategoryActive(SubCategoryModel.SubCategoryActive.YES);
                subCatShoes = subCategoryRepository.save(subCatShoes);

                subCatGadgets = new SubCategoryModel();
                subCatGadgets.setSubCategoryName("Gadgets");
                subCatGadgets.setAuthUserId(user.getAuthUserId());
                subCatGadgets.setSubCategoryActive(SubCategoryModel.SubCategoryActive.YES);
                subCatGadgets = subCategoryRepository.save(subCatGadgets);
            }

            // 5. Generate 1000 Realistic E-commerce Products (Amazon/Flipkart style)
            String[] techBrands = {"Apple", "Samsung", "Sony", "Dell", "HP", "Lenovo", "Asus", "OnePlus", "Xiaomi", "Bose"};
            String[] techProducts = {
                "iPhone 15 Pro Max 256GB", "Galaxy S24 Ultra 512GB", "WH-1000XM5 Noise Cancelling Headphones", 
                "XPS 15 Laptop with 4K OLED", "Pavilion 14 inch Core i7", "ThinkPad X1 Carbon Gen 10",
                "ROG Strix G15 Gaming Laptop", "12 Pro 5G 12GB RAM", "Redmi Note 13 Pro", "QuietComfort Earbuds II"
            };
            double[] techPrices = {145000, 135000, 35000, 185000, 85000, 160000, 120000, 80000, 30000, 25000};
            
            String[] fashionBrands = {"Nike", "Adidas", "Puma", "Zara", "Levi's", "Gucci", "H&M", "Calvin Klein", "Reebok", "Vans"};
            String[] fashionProducts = {
                "Air Force 1 '07 Sneakers", "Ultraboost 22 Running Shoes", "RS-X 3D Sneakers",
                "Slim Fit Casual Shirt", "501 Original Fit Jeans", "Premium Leather Wallet",
                "Cotton Crew Neck T-Shirt", "CK One Unisex Perfume", "Classic Workout Shoes", "Old Skool Canvas Sneakers"
            };
            double[] fashionPrices = {12000, 15000, 8000, 3500, 4500, 25000, 800, 6000, 7000, 5000};

            java.util.List<ProductModel> productsToSave = new java.util.ArrayList<>();
            java.util.Random random = new java.util.Random();
            
            for (int i = 0; i < 1000; i++) {
                boolean isTech = (i % 2 == 0);
                
                int rIndex = random.nextInt(10);
                String brand = isTech ? techBrands[rIndex] : fashionBrands[rIndex];
                String baseName = isTech ? techProducts[rIndex] : fashionProducts[rIndex];
                
                // Add color variations to make products unique
                String[] colors = {"Midnight Black", "Titanium White", "Silver", "Ocean Blue", "Ruby Red", "Graphite Grey"};
                String color = colors[random.nextInt(colors.length)];
                
                String name = brand + " " + baseName + " (" + color + ")";
                
                double basePrice = isTech ? techPrices[rIndex] : fashionPrices[rIndex];
                // Add random variance to price (± 10%) so not every iPhone costs exactly the same
                double price = basePrice + (random.nextDouble() * basePrice * 0.2) - (basePrice * 0.1);
                
                String details = isTech 
                    ? "Experience the latest in technology with this premium " + baseName + " from " + brand + ". Features cutting-edge performance, sleek design in " + color + ", and excellent reliability. Perfect for tech enthusiasts and professionals."
                    : "Upgrade your style with this authentic " + baseName + " by " + brand + ". Made from high-quality materials for maximum comfort and durability. The " + color + " finish makes it perfect for everyday use.";
                
                CategoryModel cat = isTech ? catElectronics : catFashion;
                SubCategoryModel subCat = isTech ? subCatGadgets : subCatShoes;
                
                long code = 100000 + i; // Unique product code
                int availability = random.nextInt(100) + 1; // 1 to 100 in stock
                
                productsToSave.add(createProduct(name, brand, code, availability, price, details, user, lang, cat, subCat));
            }

            productRepository.saveAll(productsToSave);

            System.out.println("✅ Dummy data successfully injected!");
        }
    }

    private ProductModel createProduct(String name, String brand, long code, int availability, double price, String details, 
                                       AuthUserModel user, LanguageModel lang, CategoryModel cat, SubCategoryModel subCat) {
        ProductModel p = new ProductModel();
        p.setProductName(name);
        p.setProductBrand(brand);
        p.setProductCode(code);
        p.setProductAvailability(availability);
        p.setProductPrice(price);
        p.setProductDetails(details);
        p.setProductStock(ProductModel.ProductStock.IN_STOCK);
        p.setProductActive(ProductModel.ProductActive.YES);
        
        p.setAuthUserInfo(user);
        p.setLanguageInfo(lang);
        p.setCategoryInfo(cat);
        p.setSubCategoryInfo(subCat);
        return p;
    }
}
