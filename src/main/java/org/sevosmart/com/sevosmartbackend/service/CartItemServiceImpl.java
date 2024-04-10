package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.Buyer;
import org.sevosmart.com.sevosmartbackend.model.CartItems;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.CartItemRepository;
import org.sevosmart.com.sevosmartbackend.repository.ProductRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public String addProductToCart(String productId, String buyerId) {
        Optional<User> userOptional = userRepository.findById(buyerId);
        Product product = productRepository.findById(productId).orElse(null);
        if (userOptional.isEmpty())  return "Buyer not found";
        if (!(userOptional.get() instanceof Buyer buyer)) return "User is not a buyer";
        if (product == null) return "Product not found";

        List<CartItems> cartItems = buyer.getCartItems();
        Optional<CartItems> existingCartItemOptional = cartItems.stream()
                .filter(c -> Objects.equals(c.getProduct().getId(), productId))
                .findFirst();

        if (existingCartItemOptional.isPresent()) {
            CartItems existingCartItem = existingCartItemOptional.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemRepository.save(existingCartItem);
        } else {
            CartItems newCartItem = new CartItems();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(1);
            newCartItem.setBuyer(buyer);
            buyer.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        userRepository.save(buyer);
        return "Product added to cart";
    }


    @Override
    public List<CartItems> cartProducts(String buyerId) {
        Optional<User> userOptional = userRepository.findById(buyerId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user instanceof Buyer buyer) {
                List<CartItems> cartItems = buyer.getCartItems();
                if (!cartItems.isEmpty()) {
                    return cartItems;
                }
            }
        }
        return Collections.emptyList();
    }


    @Override
    public String removeProductFromCartById(String productId, String buyerId) {
        return null;
    }

    @Override
    public String deleteProductFromCartById(String productId, String buyerId) {
        return null;
    }
}
