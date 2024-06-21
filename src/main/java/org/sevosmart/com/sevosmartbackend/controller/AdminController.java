package org.sevosmart.com.sevosmartbackend.controller;

import lombok.AllArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.PriceUpdateRequest;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
@CrossOrigin
@AllArgsConstructor
public class AdminController {
    private final ProductService productService;
    @PostMapping(value = "/addProduct/{adminId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addNewProduct(@ModelAttribute Product product, @RequestParam("productpic") MultipartFile productpic, @PathVariable String adminId) throws IOException {
        return new ResponseEntity<>(productService.addNewProduct(product, productpic, adminId), HttpStatus.CREATED);
    }

    @GetMapping("/allProduct")
    public ResponseEntity<?> getAllProduct(@RequestHeader("Authorization") String authorizationHeader) {
        return productService.getAllProduct(authorizationHeader);   
    }

//    @GetMapping("/productImage/{productId}")
//    public ResponseEntity<byte[]> getProductImage(@PathVariable String productId) {
//        String image = productService.getProductImage(productId);
//        if (image != null) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .body(image);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @PutMapping(value = "/updateProduct/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@ModelAttribute Product product, @RequestParam("productpic") MultipartFile productpic, @PathVariable String productId) throws IOException {
        return new ResponseEntity<>(productService.updateProduct(productId, productpic, product), HttpStatus.CREATED);
    }

    @PutMapping("/updatePrice/{id}")
    public ResponseEntity<String> updatePrice(@PathVariable String id,
            @RequestBody PriceUpdateRequest priceUpdateRequest) {
        return new ResponseEntity<>(productService.updatePrice(id, priceUpdateRequest), HttpStatus.ACCEPTED);
    }

}
