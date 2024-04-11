package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.CartItems;

import java.util.List;

public interface CartItemService {
    String addProductToCart(String productId, String buyerId);

    List<CartItems> cartProducts(String buyerId);

    String removeProductFromCartById(String productId, String buyerId);

    String deleteProductFromCartById(String productId, String buyerId);
}
