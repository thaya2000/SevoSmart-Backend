package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.dto.request.PriceUpdateRequest;
import org.sevosmart.com.sevosmartbackend.model.Product;

import java.util.List;

public interface ProductService {
    String addNewProduct(Product product, String sellerId);
    List<Product> getAllProduct();
    List<Product> getAllProductBySeller(String sellerId);
    String deleteProduct(String productId, String sellerId);
    Product getProductById(String productId, String sellerId);
    String updateProduct(String productId, Product product);
    public String updatePrice(String productId, PriceUpdateRequest price);
}
