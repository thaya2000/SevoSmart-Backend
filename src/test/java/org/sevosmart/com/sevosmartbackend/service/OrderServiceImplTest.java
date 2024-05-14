package org.sevosmart.com.sevosmartbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sevosmart.com.sevosmartbackend.dto.request.OrderDetailRequest;
import org.sevosmart.com.sevosmartbackend.model.CartItems;
import org.sevosmart.com.sevosmartbackend.model.Customer;
import org.sevosmart.com.sevosmartbackend.model.Order;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.repository.CartItemRepository;
import org.sevosmart.com.sevosmartbackend.repository.OrderRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private OrderDetailRequest orderDetailRequest;
    private Order order;
    // private CartItems cartItem;

    @BeforeEach
    void setUp() {
        // Create a product using Lombok's builder
        Product prod = Product.builder()
                .id("prod123")
                .price(100.0)
                .build();

        // Assuming CartItems has a suitable constructor or setter methods
        CartItems cartItem = new CartItems();
        cartItem.setProduct(prod);
        cartItem.setQuantity(2);

        customer = new Customer();
        customer.setId("cust123");
        customer.setCartItems(new ArrayList<>()); // Ensure the list is initialized
        customer.getCartItems().add(cartItem);

        orderDetailRequest = new OrderDetailRequest();
        orderDetailRequest.setAddressLineOne("123 Main St");
        orderDetailRequest.setCity("Metropolis");
        orderDetailRequest.setPhoneNo("1234567890");

        order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(new ArrayList<>(customer.getCartItems()));
        order.setTotalPrice(300.0);
    }

    @Test
    void placeOrder_CustomerNotFound() {
        when(userRepository.findById("cust123")).thenReturn(Optional.empty());
        String result = orderService.placeOrder("cust123", orderDetailRequest);
        assertEquals("Customer not found", result);
    }

    @Test
    void placeOrder_Success() {
        when(userRepository.findById("cust123")).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = orderService.placeOrder("cust123", orderDetailRequest);
        assertEquals("Order placed successfully", result);
        verify(orderRepository).save(any(Order.class));
    }
}
