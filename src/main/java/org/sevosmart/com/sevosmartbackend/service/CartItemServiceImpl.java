package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.Buyer;
import org.sevosmart.com.sevosmartbackend.model.CartItems;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.BuyerRepository;
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

    private final BuyerRepository buyerRepository;

    @Override
    public String addProductToCart(String productId, String buyerId) {
        Optional<User> userOptional = userRepository.findById(buyerId);
        Product product = productRepository.findById(productId).orElse(null);
        if (userOptional.isEmpty())
            return "Buyer not found";
        if (!(userOptional.get() instanceof Buyer buyer))
            return "User is not a buyer";
        if (product == null)
            return "Product not found";

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
            newCartItem.setBuyerId(buyer.getId());
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
        Optional<CartItems> cartItemOptional = Optional
                .ofNullable(cartItemRepository.findByProductIdAndBuyerId(productId, buyerId));
        if (cartItemOptional.isPresent()) {
            CartItems cartItem = cartItemOptional.get();
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemRepository.save(cartItem);
                return "Product quantity reduced successfully";
            } else {
                deleteProductFromCartById(productId, buyerId);
                return "Product removed successfully";
            }
        }
        return "Record not found";
    }

    @Override
    public String deleteProductFromCartById(String productId, String buyerId) {
        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);
        if (buyerOptional.isPresent()) {
            Buyer buyer = buyerOptional.get();
            CartItems cartItem = cartItemRepository.findByProductIdAndBuyerId(productId, buyerId);
            if (cartItem != null) {
                buyer.getCartItems().remove(cartItem);
                buyerRepository.save(buyer);
                cartItemRepository.deleteById(cartItem.getId());
                return "Product deleted from cart";
            }
        }
        return "Record not found";
    }
}
