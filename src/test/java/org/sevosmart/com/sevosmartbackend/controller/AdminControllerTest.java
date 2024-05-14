package org.sevosmart.com.sevosmartbackend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.sevosmart.com.sevosmartbackend.dto.request.PriceUpdateRequest;
import org.sevosmart.com.sevosmartbackend.model.Product;
import org.sevosmart.com.sevosmartbackend.service.ProductService;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void addNewProduct() throws Exception {
        MockMultipartFile file = new MockMultipartFile("productpic", "test.jpg", "image/jpeg", "test image".getBytes());
        when(productService.addNewProduct(any(Product.class), any(), anyString())).thenReturn("Product added");

        mockMvc.perform(multipart("/admin/addProduct/adminId")
                .file(file)
                .param("productName", "Test Product")
                .param("price", "99.99")
                .param("quantity", "10"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Product added"));

        verify(productService).addNewProduct(any(Product.class), any(), eq("adminId"));
    }

    @Test
    void getAllProduct() throws Exception {
        mockMvc.perform(get("/admin/allProduct"))
                .andExpect(status().isOk());

        verify(productService).getAllProduct();
    }

    @Test
    void getProductImage() throws Exception {
        byte[] image = new byte[] { 1, 2, 3 };
        when(productService.getProductImage("productId")).thenReturn(image);

        mockMvc.perform(get("/admin/productImage/productId"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(image));

        verify(productService).getProductImage("productId");
    }

    @Test
    void deleteProduct() throws Exception {
        when(productService.deleteProduct("productId")).thenReturn("Product deleted");

        mockMvc.perform(delete("/admin/deleteProduct/productId"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Product deleted"));

        verify(productService).deleteProduct("productId");
    }

    @Test
    void getProductById() throws Exception {
        when(productService.getProductById("productId")).thenReturn(new Product());

        mockMvc.perform(get("/admin/product/productId"))
                .andExpect(status().isOk());

        verify(productService).getProductById("productId");
    }

    @Test
    void updateProduct() throws Exception {
        // Prepare the file and product details
        MockMultipartFile file = new MockMultipartFile("productpic", "test.jpg", "image/jpeg", "test image".getBytes());
        when(productService.updateProduct(eq("productId"), any(MultipartFile.class), any(Product.class)))
                .thenReturn("Product updated");

        // Perform the PUT request with the correct method
        mockMvc.perform(multipart("/admin/updateProduct/productId")
                .file(file)
                .param("productName", "Updated Product")
                .param("price", "199.99")
                .param("quantity", "5")
                .with(request -> {
                    request.setMethod("PUT"); // Setting the request method to PUT
                    return request;
                }))
                .andExpect(status().isCreated())
                .andExpect(content().string("Product updated"));

        // Verify that the service method was called as expected
        verify(productService).updateProduct(eq("productId"), any(MultipartFile.class), any(Product.class));
    }

    @Test
    void updatePrice() throws Exception {
        PriceUpdateRequest priceUpdateRequest = new PriceUpdateRequest();
        priceUpdateRequest.setPrice(299.99);
        when(productService.updatePrice("productId", priceUpdateRequest)).thenReturn("Price updated");

        mockMvc.perform(put("/admin/updatePrice/productId")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"price\":299.99}"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Price updated"));

        verify(productService).updatePrice("productId", priceUpdateRequest);
    }
}
