package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.PriceUpdateRequest;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.model.Seller;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.ProductRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public String addNewProduct(Product product, String sellerId) {
        Optional<User> userOptional = userRepository.findById(sellerId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Retrieved user type: " + user.getClass().getSimpleName());
            if (user instanceof Seller) {
                Seller seller = (Seller) user;
                product.setSeller(seller);
                productRepository.save(product);
                return "Product Added successfully";
            } else {
                return "User with ID " + sellerId + " is not a Seller";
            }
        } else {
            return "User not found with ID " + sellerId;
        }
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductBySeller(String sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    @Override
    public String deleteProduct(String productId, String sellerId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getSeller().getId().equals(sellerId)) {
                productRepository.deleteById(productId);
                return "Product Deleted Successfully";
            } else {
                return "You don't have permission to delete this product";
            }
        } else {
            return "Product not found";
        }
    }

    @Override
    public Product getProductById(String productId, String sellerId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getSeller().getId().equals(sellerId)) {
                return product;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String updateProduct(String productId, Product updatedProduct) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            updatedProduct.setId(productId);
            updatedProduct.setSeller(product.getSeller());
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
