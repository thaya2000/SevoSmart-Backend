package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;

import org.sevosmart.com.sevosmartbackend.dto.request.OrderDetailRequest;
import org.sevosmart.com.sevosmartbackend.model.Order;
import org.sevosmart.com.sevosmartbackend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/placeOrder/{customerId}")
    public ResponseEntity<String> placeOrder(@PathVariable String customerId,
            @RequestBody OrderDetailRequest orderDetailRequest) {
        return new ResponseEntity<>(orderService.placeOrder(customerId, orderDetailRequest), HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        return new ResponseEntity<>(orderService.getOrderDetails(orderId), HttpStatus.OK);
    }

    @GetMapping("/ordersByCustomer/{customerId}")
    public ResponseEntity<?> getOrdersByCustomerId(@PathVariable String customerId) {
        try {
            return new ResponseEntity<>(orderService.getOrdersByCustomerId(customerId), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ordersToDeliver")
    public ResponseEntity<List<Order>> getOrdersToDeliver() {
        return new ResponseEntity<>(orderService.getOrdersToDeliver(), HttpStatus.OK);
    }

    @PutMapping("/deliverOrder/{orderId}")
    public ResponseEntity<String> deliverOrder(@PathVariable String orderId) {
        try {
            return new ResponseEntity<>(orderService.deliverOrder(orderId), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
        try {
            return new ResponseEntity<>(orderService.cancelOrder(orderId), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ordersByStatus/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Order> orders = orderService.getOrdersByStatus(status);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
