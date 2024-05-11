package org.sevosmart.com.sevosmartbackend.service;

import java.util.List;

import org.sevosmart.com.sevosmartbackend.dto.request.OrderDetailRequest;
import org.sevosmart.com.sevosmartbackend.model.Order;

public interface OrderService {
    String placeOrder(String customerId, OrderDetailRequest orderDetailRequest);

    Order getOrderDetails(String orderId);

    List<Order> getOrdersToDeliver();

    String cancelOrder(String orderId);

    String deliverOrder(String orderId);

    List<Order> getOrdersByCustomerId(String customerId);

    List<Order> getOrdersByStatus(String status);

    List<Order> getAllOrders();

    // String updateOrderStatus(String orderId, String status);

    // String updateOrderPaymentStatus(String orderId, String paymentStatus);

    // String updateOrderDeliveryStatus(String orderId, String deliveryStatus);

    // String updateOrderPaymentAndDeliveryStatus(String orderId, String
    // paymentStatus, String deliveryStatus);

    // String updateOrderPaymentStatusAndDeliveryStatus(String orderId, String
    // paymentStatus, String deliveryStatus);

    // String updateOrderStatusAndPaymentStatus(String orderId, String status,
    // String paymentStatus);

    // String updateOrderStatusAndDeliveryStatus(String orderId, String status,
    // String deliveryStatus);

    // String updateOrderStatusPaymentStatusAndDeliveryStatus(String orderId, String
    // status, String paymentStatus, String deliveryStatus);

}
