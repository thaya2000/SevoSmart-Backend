package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.dto.request.PriceUpdateRequest;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    String addNewProduct(Product product, MultipartFile productImage, String adminId) throws IOException;

    List<Product> getAllProduct();

    byte[] getProductImage(String productId);

//    List<Product> getAllProductBySeller(String sellerId);

    String deleteProduct(String productId);

    Product getProductById(String productId);

    String updateProduct(String productId, MultipartFile productImage, Product product) throws IOException;

    String updatePrice(String productId, PriceUpdateRequest price);
}
