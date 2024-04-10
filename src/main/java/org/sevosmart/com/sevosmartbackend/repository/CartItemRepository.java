package org.sevosmart.com.sevosmartbackend.repository;

import org.bson.types.ObjectId;
import org.sevosmart.com.sevosmartbackend.model.CartItems;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartItemRepository extends MongoRepository<CartItems, ObjectId> {
    CartItems findByProductIdAndBuyerId(String product_id, String buyer_id);
}
