package org.sevosmart.com.sevosmartbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sevosmart.com.sevosmartbackend.model.*;
import org.sevosmart.com.sevosmartbackend.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CartItemServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProductToCart_ProductExists_CustomerExists() {
        // Arrange
        String customerId = "customerId";
        String productId = "productId";
        Customer customer = new Customer();
        customer.setCartItems(new ArrayList<>());
        Product product = new Product();
        product.setId(productId);
        product.setQuantity(10);  // Ensure product has a quantity
        when(userRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        String result = cartItemService.addProductToCart(productId, customerId);

        // Assert
        assertEquals("Product added to cart", result);
        verify(cartItemRepository, times(1)).save(any(CartItems.class));
    }

    @Test
    void removeProductFromCartById_ProductExists() {
        // Arrange
        String customerId = "customerId";
        String productId = "productId";
        CartItems cartItem = new CartItems();
        cartItem.setProduct(new Product());
        cartItem.setQuantity(2);
        when(cartItemRepository.findByProductIdAndCustomerId(productId, customerId)).thenReturn(cartItem);
        // Act
        String result = cartItemService.removeProductFromCartById(productId, customerId);
        // Assert
        assertEquals("Product quantity reduced successfully", result);
    }

    @Test
    void deleteProductFromCartById_ProductInCart_DeletesProduct() {
        // Arrange
        String customerId = "customerId";
        String productId = "productId";
        Customer customer = new Customer();
        customer.setId(customerId);
        Product product = new Product();
        product.setId(productId);
        product.setQuantity(5);
        CartItems cartItem = new CartItems();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        List<CartItems> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        customer.setCartItems(cartItems);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByProductIdAndCustomerId(productId, customerId))
                .thenReturn(cartItem);

        // Act
        String result = cartItemService.deleteProductFromCartById(productId, customerId);

        // Assert
        assertEquals("Product deleted from cart", result);
        verify(cartItemRepository, times(1)).deleteByProductIdAndCustomerId(productId, customerId);
    }

}
