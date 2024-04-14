package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.Customer;
import org.sevosmart.com.sevosmartbackend.model.CartItems;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.CustomerRepository;
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

    private final CustomerRepository customerRepository;

    @Override
    public String addProductToCart(String productId, String customerId) {
        Optional<User> userOptional = userRepository.findById(customerId);
        Product product = productRepository.findById(productId).orElse(null);
        if (userOptional.isEmpty())
            return "Customer not found";
        if (!(userOptional.get() instanceof Customer customer))
            return "User is not a Customer";
        if (product == null)
            return "Product not found";

        List<CartItems> cartItems = customer.getCartItems();
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
            newCartItem.setCustomer(customer);
            customer.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        product.setQuantity(product.getQuantity() - 1);
        productRepository.save(product);
        userRepository.save(customer);
        return "Product added to cart";
    }

    @Override
    public List<CartItems> cartProducts(String customerId) {
        Optional<User> userOptional = userRepository.findById(customerId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user instanceof Customer customer) {
                List<CartItems> cartItems = customer.getCartItems();
                if (!cartItems.isEmpty()) {
                    return cartItems;
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String removeProductFromCartById(String productId, String customerId) {
        Optional<CartItems> cartItemOptional = Optional
                .ofNullable(cartItemRepository.findByProductIdAndCustomerId(productId, customerId));
        Product product = productRepository.findById(productId).orElse(null);
        if (cartItemOptional.isPresent()) {
            CartItems cartItem = cartItemOptional.get();
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                Optional.ofNullable(product)
                        .ifPresent(p -> {
                            p.setQuantity(p.getQuantity() + 1);
                            productRepository.save(p);
                        });
                cartItemRepository.save(cartItem);
                return "Product quantity reduced successfully";
            } else {
                deleteProductFromCartById(productId, customerId);
                return "Product removed successfully";
            }
        }
        return "Record not found";
    }

//    @Override
//    public String deleteProductFromCartById(String productId, String customerId) {
//        Optional<Customer> buyerOptional = customerRepository.findById(customerId);
//        if (buyerOptional.isPresent()) {
//            Customer customer = buyerOptional.get();
//            CartItems cartItemToRemove = null;
//            for (CartItems cartItem : customer.getCartItems()) {
//                if (cartItem.getProduct().getId().equals(productId)) {
//                    cartItemToRemove = cartItem;
//                    break;
//                }
//            }
//            if (cartItemToRemove != null) {
//                customer.getCartItems().remove(cartItemToRemove);
//                customerRepository.save(customer);
//                cartItemRepository.delete(cartItemToRemove);
//                return "Product deleted from cart";
//            }
//        }
//        return "Record not found";
//    }

    @Override
    public String deleteProductFromCartById(String productId, String customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        Product product = productRepository.findById(productId).orElse(null);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            boolean isCartAvailable = customer.getCartItems().stream()
                    .anyMatch(cartItem -> cartItem.getProduct().getId().equals(productId));
            if(isCartAvailable) {
                customer.getCartItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
                CartItems cartItems = cartItemRepository.findByProductIdAndCustomerId(productId, customerId);
                Optional.ofNullable(product)
                        .ifPresent(p -> {
                            p.setQuantity(p.getQuantity() + cartItems.getQuantity());
                            productRepository.save(p);
                        });
                customerRepository.save(customer);
                cartItemRepository.deleteByProductIdAndCustomerId(productId, customerId);
                return "Product deleted from cart";
            }else  return "Record not found customer cart";
        }
        return "Record not found";
    }
}
