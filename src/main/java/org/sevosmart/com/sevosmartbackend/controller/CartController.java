package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.CartItems;
import org.sevosmart.com.sevosmartbackend.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;

    @PostMapping("/addProductToCart/{productId}/{customerId}")
    public ResponseEntity<String> addProductToCart(@PathVariable String productId, @PathVariable String customerId) {
        return new ResponseEntity<>(cartItemService.addProductToCart(productId, customerId), HttpStatus.CREATED);
    }

    @GetMapping("/cart_products/{customerId}")
    public ResponseEntity<List<CartItems>> cartProducts(@PathVariable String customerId) {
        return new ResponseEntity<>(cartItemService.cartProducts(customerId), HttpStatus.OK);
    }

    @DeleteMapping("/removeProductFromCartById/{productId}/{customerId}")
    public ResponseEntity<String> removeProductFromCartById(@PathVariable String productId,
            @PathVariable String customerId) {
        return new ResponseEntity<>(cartItemService.removeProductFromCartById(productId, customerId),
                HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/deleteProductFromCartById/{productId}/{customerId}")
    public ResponseEntity<String> deleteProductFromCartById(@PathVariable String productId,
            @PathVariable String customerId) {
        return new ResponseEntity<>(cartItemService.deleteProductFromCartById(productId, customerId),
                HttpStatus.ACCEPTED);
    }
}
