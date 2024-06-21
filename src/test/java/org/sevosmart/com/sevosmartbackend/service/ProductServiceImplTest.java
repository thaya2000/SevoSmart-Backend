package org.sevosmart.com.sevosmartbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sevosmart.com.sevosmartbackend.model.Admin;
import org.sevosmart.com.sevosmartbackend.model.Product;
// import org.sevosmart.com.sevosmartbackend.model.User;
import org.sevosmart.com.sevosmartbackend.repository.AdminRepository;
import org.sevosmart.com.sevosmartbackend.repository.ProductRepository;
import org.sevosmart.com.sevosmartbackend.repository.UserRepository;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

//    @BeforeEach
//    void setUp() {
//        Admin admin = new Admin();
//        admin.setId("adminId");
//        admin.setProducts(new ArrayList<>());
//        lenient().when(userRepository.findById("adminId")).thenReturn(Optional.of(admin));
//        lenient().when(adminRepository.findById("adminId")).thenReturn(Optional.of(admin));
//    }
//
//    @Test
//    void addNewProduct_Success() throws IOException {
//        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
//                "test image content".getBytes());
//        Product product = new Product();
//        product.setProductName("Test Product");
//        product.setId("productId"); // Simulating setting an ID
//
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        String result = productService.addNewProduct(product, file, "adminId");
//        assertNotNull(result);
//        assertEquals("productId", result);
//        verify(productRepository).save(any(Product.class));
//    }
//
//    @Test
//    void getAllProduct_ReturnsList() {
//        List<Product> expectedProducts = Arrays.asList(new Product(), new Product());
//        when(productRepository.findAll()).thenReturn(expectedProducts);
//
//        List<Product> products = productService.getAllProduct();
//
//        assertNotNull(products);
//        assertEquals(2, products.size());
//    }
//
//    @Test
//    void getProductById_Found() {
//        Optional<Product> expectedProduct = Optional.of(new Product());
//        when(productRepository.findById("productId")).thenReturn(expectedProduct);
//
//        Product product = productService.getProductById("productId");
//
//        assertNotNull(product);
//    }
//
//    @Test
//    void updateProduct_Success() throws IOException {
//        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
//                "test image content".getBytes());
//        Product existingProduct = new Product();
//        existingProduct.setId("productId");
//        when(productRepository.findById("productId")).thenReturn(Optional.of(existingProduct));
//        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
//
//        String result = productService.updateProduct("productId", file, new Product());
//
//        assertEquals("Product Updated", result);
//        verify(productRepository).save(any(Product.class));
//    }
}
