package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.CartItems;

import java.util.List;

public interface CartItemService {
    public String addProductToCart(String productId, String buyerId);
    public List<CartItems> cartProducts(String buyerId);
    public String removeProductFromCartById(String productId,  String buyerId);
    public String deleteProductFromCartById(String productId,  String buyerId);
}
