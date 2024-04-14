package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.CartItems;

import java.util.List;

public interface CartItemService {
    String addProductToCart(String productId, String customerId);

    List<CartItems> cartProducts(String customerId);

    String removeProductFromCartById(String productId, String customerId);

    String deleteProductFromCartById(String productId, String customerId);
}
