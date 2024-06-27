package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.OrderDetailRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.DetailOrderResponse;
import org.sevosmart.com.sevosmartbackend.dto.response.OrderResponse;
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
import java.util.ArrayList;
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
    public String placeOrderByCartId(List<String> cartIds, String customerId, OrderDetailRequest orderDetailRequest) {
        Optional<User> userOptional = userRepository.findById(customerId);
        if (userOptional.isEmpty())
            return "Customer not found";
        if (!(userOptional.get() instanceof Customer customer))
            return "User is not a Customer";

        System.out.println("Let Print the cart Items");
        System.out.println(cartIds);
        List<CartItems> customerCartItems = cartItemRepository.findAllById(cartIds);
        System.out.println("Let Print the cart Items");
        System.out.println(customerCartItems.size());


        if (customerCartItems.isEmpty())
            return "No valid cart items found";
//        else
//            return "Cart items found";
//            return customerCartItems.toString();
//            System.out.println(customerCartItems);


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

        customer.getCartItems().removeAll(customerCartItems);
        userRepository.save(customer);

        return "Order placed successfully";
    }

    @Override
    public DetailOrderResponse getOrderDetails(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            DetailOrderResponse detailOrderResponse = new DetailOrderResponse();
            detailOrderResponse.setOrderNumber(order.getOrderId());
            detailOrderResponse
                    .setOrderCustomerName(order.getCustomer().getFirstname() + " " + order.getCustomer().getLastname());
            detailOrderResponse.setOrderDate(order.getOrderDate());
            detailOrderResponse.setOrderStatus(order.getOrderStatus().toString());
            detailOrderResponse.setOrderAmount(String.valueOf(order.getTotalPrice()));
            detailOrderResponse
                    .setOrderBillingAddress(order.getAddressLineOne() + ", " + order.getAddressLineTwo() + ", "
                            + order.getCity() + ", " + order.getDistrict());

            List<DetailOrderResponse.ProductDetail> productDetails = new ArrayList<>();
            for (CartItems cartItem : order.getOrderItems()) {
                DetailOrderResponse.ProductDetail productDetail = new DetailOrderResponse.ProductDetail();
                productDetail.setProductName(cartItem.getProduct().getProductName());
                productDetail.setProductQuantity(cartItem.getQuantity());
//                productDetail.setProductImage(cartItem.getProduct().getProductImage());
                productDetail.setProductImageUrl(cartItem.getProduct().getProductImageURL());
                productDetails.add(productDetail);
            }
            detailOrderResponse.setProducts(productDetails);
            return detailOrderResponse;
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Override
    public List<DetailOrderResponse> getOrdersByCustomerId(String customerId) {
        Optional<User> userOptional = userRepository.findById(customerId);
        if (userOptional.isEmpty())
            throw new RuntimeException("Customer not found");
        if (!(userOptional.get() instanceof Customer customer))
            throw new RuntimeException("User is not a Customer");

        List<Order> orders = orderRepository.findByCustomer(customer);

        if (orders.isEmpty()) {
            throw new RuntimeException("No orders found for this customer");
        }

        List<DetailOrderResponse> detailOrderResponses = new ArrayList<>();
        for (Order order : orders) {
            DetailOrderResponse detailOrderResponse = new DetailOrderResponse();
            detailOrderResponse.setOrderNumber(order.getOrderId());
            detailOrderResponse
                    .setOrderCustomerName(order.getCustomer().getFirstname() + " " + order.getCustomer().getLastname());
            detailOrderResponse.setOrderDate(order.getOrderDate());
            detailOrderResponse.setOrderStatus(order.getOrderStatus().toString());
            detailOrderResponse.setOrderAmount(String.valueOf(order.getTotalPrice()));
            detailOrderResponse
                    .setOrderBillingAddress(order.getAddressLineOne() + ", " + order.getAddressLineTwo() + ", "
                            + order.getCity() + ", " + order.getDistrict());

            List<DetailOrderResponse.ProductDetail> productDetails = new ArrayList<>();
            for (CartItems cartItem : order.getOrderItems()) {
                DetailOrderResponse.ProductDetail productDetail = new DetailOrderResponse.ProductDetail();
                productDetail.setProductName(cartItem.getProduct().getProductName());
                productDetail.setProductQuantity(cartItem.getQuantity());
//                productDetail.setProductImage(cartItem.getProduct().getProductImage());
                productDetail.setProductImageUrl(cartItem.getProduct().getProductImageURL());
                productDetails.add(productDetail);
            }
            detailOrderResponse.setProducts(productDetails);
            detailOrderResponses.add(detailOrderResponse);
        }
        return detailOrderResponses;
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
    public List<OrderResponse> getOrdersByStatus(String status) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.valueOf(status));

        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderNumber(order.getOrderId());
            orderResponse
                    .setCustomerName(order.getCustomer().getFirstname() + " " + order.getCustomer().getLastname());
            orderResponse.setOrderDate(order.getOrderDate());
            orderResponse.setOrderStatus(order.getOrderStatus().toString());
            orderResponse.setOrderAmount(String.valueOf(order.getTotalPrice()));
            orderResponse.setOrderBillingAddress(order.getAddressLineOne() + ", " + order.getAddressLineTwo() + ", "
                    + order.getCity() + ", " + order.getDistrict());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderNumber(order.getOrderId());
            orderResponse
                    .setCustomerName(order.getCustomer().getFirstname() + " " + order.getCustomer().getLastname());
            orderResponse.setOrderDate(order.getOrderDate());
            orderResponse.setCustomerPhoneNo(order.getPhoneNo());
            orderResponse.setOrderStatus(order.getOrderStatus().toString());
            orderResponse.setOrderAmount(String.valueOf(order.getTotalPrice()));
            orderResponse.setOrderBillingAddress(order.getAddressLineOne() + ", " + order.getAddressLineTwo() + ", "
                    + order.getCity() + ", " + order.getDistrict());
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }
}
