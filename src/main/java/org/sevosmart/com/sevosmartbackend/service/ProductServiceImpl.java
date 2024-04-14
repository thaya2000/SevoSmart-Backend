package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.PriceUpdateRequest;
import org.sevosmart.com.sevosmartbackend.model.Admin;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.AdminRepository;
import org.sevosmart.com.sevosmartbackend.repository.ProductRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;

    @Override
    public String addNewProduct(Product product, MultipartFile productImage, String adminId) throws IOException {
        Optional<User> userOptional = userRepository.findById(adminId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user instanceof Admin admin) {
                product.setProductImage(productImage.getBytes());
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
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public byte[] getProductImage(String productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            return product.getProductImage();
        } else {
            return null;
        }
    }

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
            updatedProduct.setProductImage(productImage.getBytes());
            updatedProduct.setAdmin(product.getAdmin());
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
