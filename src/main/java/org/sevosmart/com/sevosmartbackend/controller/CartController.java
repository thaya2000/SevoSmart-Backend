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

    @PostMapping("/addProductToCart/{productId}/{buyerId}")
    public ResponseEntity<String> addProductToCart(@PathVariable String productId, @PathVariable String buyerId) {
        return new ResponseEntity<>(cartItemService.addProductToCart(productId, buyerId), HttpStatus.CREATED);
    }

    @GetMapping("/cart_products/{buyerId}")
    public ResponseEntity<List<CartItems>> cartProducts(@PathVariable String buyerId) {
        return new ResponseEntity<>(cartItemService.cartProducts(buyerId), HttpStatus.OK);
    }

    @DeleteMapping("/removeProductFromCartById/{productId}/{buyerId}")
    public ResponseEntity<String> removeProductFromCartById(@PathVariable String productId,
            @PathVariable String buyerId) {
        return new ResponseEntity<>(cartItemService.removeProductFromCartById(productId, buyerId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/deleteProductFromCartById/{productId}/{buyerId}")
    public ResponseEntity<String> deleteProductFromCartById(@PathVariable String productId,
            @PathVariable String buyerId) {
        return new ResponseEntity<>(cartItemService.deleteProductFromCartById(productId, buyerId), HttpStatus.ACCEPTED);
    }
}
