package org.sevosmart.com.sevosmartbackend.controller;

import lombok.AllArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.PriceUpdateRequest;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin
@AllArgsConstructor
public class AdminController {
    private final ProductService productService;

    @PostMapping("/addProduct/{sellerId}")
    public ResponseEntity<String> addNewProduct(@RequestBody Product product, @PathVariable String sellerId) {
        return new ResponseEntity<String>(productService.addNewProduct(product, sellerId), HttpStatus.CREATED);
    }

    @GetMapping("/allProduct")
    public ResponseEntity<?> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/getAllProductBySeller/{sellerId}")
    public ResponseEntity<?> getAllProductBySeller(@PathVariable String sellerId) {
        return ResponseEntity.ok(productService.getAllProductBySeller(sellerId));
    }

    @DeleteMapping("/deleteProduct/{productId}/{sellerId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId, @PathVariable String sellerId) {
        return new ResponseEntity<>(productService.deleteProduct(productId, sellerId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/product/{productId}/{sellerId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId, @PathVariable String sellerId) {
        return new ResponseEntity<Product>(productService.getProductById(productId, sellerId), HttpStatus.OK);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody Product product) {
        return new ResponseEntity<String>(productService.updateProduct(id, product), HttpStatus.ACCEPTED);
    }

    @PutMapping("/updatePrice/{id}")
    public ResponseEntity<String> updatePrice(@PathVariable String id,
            @RequestBody PriceUpdateRequest priceUpdateRequest) {
        return new ResponseEntity<>(productService.updatePrice(id, priceUpdateRequest), HttpStatus.ACCEPTED);
    }

}
