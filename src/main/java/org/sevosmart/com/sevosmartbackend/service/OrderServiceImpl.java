package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.OrderDetailRequest;
import org.sevosmart.com.sevosmartbackend.enums.OrderStatus;
import org.sevosmart.com.sevosmartbackend.model.CartItems;
import org.sevosmart.com.sevosmartbackend.model.Customer;
import org.sevosmart.com.sevosmartbackend.model.Order;
import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.CartItemRepository;
import org.sevosmart.com.sevosmartbackend.repository.OrderRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;

    private final CartItemRepository cartItemRepository;

    private final OrderRepository orderRepository;

    @Override
    public String placeOrder(String customerId, OrderDetailRequest orderDetailRequest) {

        Optional<User> userOptional = userRepository.findById(customerId);

        if (userOptional.isEmpty())
            return "Customer not found";
        if (!(userOptional.get() instanceof Customer customer))
            return "User is not a Customer";
        if (customer.getCartItems() == null || customer.getCartItems().isEmpty())
            return "There is no product in cart";

        List<CartItems> customerCartItems = customer.getCartItems();

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDate.now());
        order.setAddressLineOne(orderDetailRequest.getAddressLineOne());
        order.setAddressLineTwo(orderDetailRequest.getAddressLineTwo());
        order.setCity(orderDetailRequest.getCity());
        order.setDistrict(orderDetailRequest.getDistrict());
        order.setPhoneNo(orderDetailRequest.getPhoneNo());
        order.setOrderStatus(OrderStatus.PLACED);
        order.setOrderItems(customerCartItems);
        order.setShippingCost(1000.00);
        order.setTotalPrice(
                customerCartItems.stream().mapToDouble(c -> c.getProduct().getPrice() * c.getQuantity()).sum()
                        + order.getShippingCost());
        orderRepository.save(order);

        customer.setCartItems(null);
        userRepository.save(customer);

        return "Order placed successfully";
    }

    @Override
    public Order getOrderDetails(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Override
    public List<Order> getOrdersByCustomerId(String customerId) {
        Optional<User> userOptional = userRepository.findById(customerId);
        if (userOptional.isEmpty())
            throw new RuntimeException("Customer not found");
        if (!(userOptional.get() instanceof Customer customer))
            throw new RuntimeException("User is not a Customer");
        return orderRepository.findByCustomer((Customer) userOptional.get());
    }

    @Override
    public List<Order> getOrdersToDeliver() {
        List<Order> ordersToDeliver = orderRepository.findByOrderStatus(OrderStatus.PLACED);
        return ordersToDeliver;
    }

    @Override
    public String cancelOrder(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return "Order cancelled successfully";
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Override
    public String deliverOrder(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
            return "Order delivered successfully";
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByOrderStatus(OrderStatus.valueOf(status));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
