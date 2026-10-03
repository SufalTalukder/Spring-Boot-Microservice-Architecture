@echo off
echo Starting all microservices...

start "Discovery Service" cmd /k ".\mvnw spring-boot:run -pl discovery-service"
timeout /t 10

start "API Gateway" cmd /k ".\mvnw spring-boot:run -pl api-gateway"
timeout /t 5

start "Auth Service" cmd /k ".\mvnw spring-boot:run -pl auth-service"
start "User Service" cmd /k ".\mvnw spring-boot:run -pl user-service"
start "Action Log Service" cmd /k ".\mvnw spring-boot:run -pl action-log-service"
start "Add To Cart Service" cmd /k ".\mvnw spring-boot:run -pl add-to-cart-service"
start "Add To Favourite Service" cmd /k ".\mvnw spring-boot:run -pl add-to-favourite-service"
start "Banner Service" cmd /k ".\mvnw spring-boot:run -pl banner-service"
start "Category Service" cmd /k ".\mvnw spring-boot:run -pl category-service"
start "Checkout Service" cmd /k ".\mvnw spring-boot:run -pl checkout-service"
start "Language Service" cmd /k ".\mvnw spring-boot:run -pl language-service"
start "Newsletter Service" cmd /k ".\mvnw spring-boot:run -pl newsletter-service"
start "Notification Service" cmd /k ".\mvnw spring-boot:run -pl notification-service"
start "Order Return Service" cmd /k ".\mvnw spring-boot:run -pl order-return-service"
start "Product Service" cmd /k ".\mvnw spring-boot:run -pl product-service"
start "Sub Category Service" cmd /k ".\mvnw spring-boot:run -pl sub-category-service"
start "Support Service" cmd /k ".\mvnw spring-boot:run -pl support-service"
start "User Rating Service" cmd /k ".\mvnw spring-boot:run -pl user-rating-service"

echo All services have been started in separate windows!
