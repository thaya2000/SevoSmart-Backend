package org.sevosmart.com.sevosmartbackend.repository;

import java.util.List;

import org.sevosmart.com.sevosmartbackend.model.Customer;
import org.sevosmart.com.sevosmartbackend.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.sevosmart.com.sevosmartbackend.enums.OrderStatus;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    List<Order> findByCustomer(Customer customer);
}
