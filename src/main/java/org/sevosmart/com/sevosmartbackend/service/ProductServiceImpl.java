package org.sevosmart.com.sevosmartbackend.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.PriceUpdateRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.UserInfoResponse;
import org.sevosmart.com.sevosmartbackend.model.Admin;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.AdminRepository;
import org.sevosmart.com.sevosmartbackend.repository.ProductRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ImageService imageService;

    private final AdminRepository adminRepository;

    private final JwtService jwtService;

    @Override
    public String addNewProduct(Product product, MultipartFile productImage, String adminId) throws IOException {
        Optional<User> userOptional = userRepository.findById(adminId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user instanceof Admin admin) {
                product.setProductImageURL(imageService.upload(productImage));
//                product.setProductImage(productImage.getBytes());
                product.setAdmin(admin);
                productRepository.save(product);
                System.out.println(product.getId());
                admin.getProducts().add(product);
                userRepository.save(admin);
                return product.getId();
            } else {
                return "User with ID " + adminId + " is not a Admin";
            }
        } else {
            return "User not found with ID " + adminId;
        }
    }

    @Override
    public ResponseEntity<?> getAllProduct(String authorizationHeader) {
//         if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Authorization header is missing or does not start with Bearer");
//        }
//
//        String token = authorizationHeader.substring(7);
//        try {
//            String userEmail = jwtService.extractUsername(token);
//            if (userEmail == null || userEmail.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("User not signed in");
//            }
//
//            var user = userRepository.findByEmail(userEmail)
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//            if (!jwtService.isTokenValid(token, user)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("Invalid or expired token");
//            }

            return ResponseEntity.ok(productRepository.findAll());

//        } catch (ExpiredJwtException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("JWT token is expired");
//        } catch (JwtException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("JWT token is invalid");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred processing your request");
//        }
    }

//    @Override
//    public String getProductImage(String productId) {
//        Product product = productRepository.findById(productId).orElse(null);
//        if (product != null) {
//            return product.getProductImageURL();
//        } else {
//            return null;
//        }
//    }

//    @Override
//    public List<Product> getAllProductBySeller(String sellerId) {
//        return productRepository.findBySellerId(sellerId);
//    }

    @Override
    public String deleteProduct(String productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String adminId = product.getAdmin().getId();
            Optional<User> adminOptional = userRepository.findById(adminId);
            if (adminOptional.isPresent() && adminOptional.get() instanceof Admin admin) {
                admin.getProducts().removeIf(p -> p.getId().equals(productId));
                userRepository.save(admin);
            }
            imageService.delete(product.getProductImageURL());
            productRepository.deleteById(productId);
            return "Product Deleted Successfully";
        } else {
            return "Product not found";
        }
    }

    @Override
    public Product getProductById(String productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return product;
        } else {
            return null;
        }
    }

    @Override
    public String updateProduct(String productId, MultipartFile productImage, Product updatedProduct) throws IOException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            updatedProduct.setId(productId);
            updatedProduct.setAdmin(product.getAdmin());
            if (productImage != null && !productImage.isEmpty()) {
                updatedProduct.setProductImageURL(imageService.edit(productImage, product.getProductImageURL()));
            } else {
                updatedProduct.setProductImageURL(product.getProductImageURL());
            }
            productRepository.save(updatedProduct);
            return "Product Updated";
        } else {
            return "Product not found";
        }
    }

    @Override
    public String updatePrice(String productId, PriceUpdateRequest request) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setPrice(request.getPrice());
            productRepository.save(product);
            return "Product Price Updated";
        } else {
            return "Product not found";
        }
    }
}
