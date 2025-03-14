package com.example.ecommerce.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.ecommerce.dto.CreateProductDTO;
import com.example.ecommerce.dto.UpdateProductDTO;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ProductControllerRBAC_Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void adminShouldBeAbleToCreateProduct() throws Exception {

        // Dummy category (assuming category ID 1 exists in DB)
        Long categoryId = 1L;

        CreateProductDTO productDTO = new CreateProductDTO();
        productDTO.setName("Product 1");
        productDTO.setDescription("Ini Produk 1");
        productDTO.setPrice(1000.0);
        productDTO.setCategoryId(categoryId); // ✅ Set category ID

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void adminShouldBeAbleToUpdateProduct() throws Exception {
        // Dummy category (assuming category ID 1 exists in DB)
        Long categoryId = 1L;

        // Create a product first
        CreateProductDTO newProduct = new CreateProductDTO();
        newProduct.setName("Product Before Update");
        newProduct.setDescription("Old Desc");
        newProduct.setPrice(1500.0);
        newProduct.setCategoryId(categoryId);

        String response = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andReturn().getResponse().getContentAsString();

        System.out.println("CREATE RESPONSE: " + response); // ✅ Debug create response

        // Extract product ID
        Long productId = objectMapper.readTree(response).get("id").asLong();

        // Prepare updated product data
        UpdateProductDTO updateProductDTO = new UpdateProductDTO();
        updateProductDTO.setName("Product 1 Updated");
        updateProductDTO.setDescription("Ini Produk 1 Updated");
        updateProductDTO.setPrice(2000.0);
        updateProductDTO.setCategoryId(2L);

        String updateJson = objectMapper.writeValueAsString(updateProductDTO);
        System.out.println("UPDATE JSON: " + updateJson); // ✅ Debug update JSON

        // Perform update request
        mockMvc.perform(put("/api/v1/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void adminShouldBeAbleToDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1")) // Ubah ID sesuai database
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void userShouldNotBeAbleToCreateProduct() throws Exception {
        CreateProductDTO productDTO = new CreateProductDTO();
        productDTO.setName("Unauthorized Product");
        productDTO.setDescription("Should not create");
        productDTO.setPrice(500.0);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isForbidden()); // Harusnya gagal
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void userShouldNotBeAbleToUpdateProduct() throws Exception {
        UpdateProductDTO updateProductDTO = new UpdateProductDTO();
        updateProductDTO.setName("Hacked");
        updateProductDTO.setDescription("Hacked Desc");
        updateProductDTO.setPrice(1.0);

        mockMvc.perform(put("/api/v1/products/1") // Ubah ID sesuai database
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateProductDTO)))
                .andExpect(status().isForbidden()); // Harusnya gagal
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void userShouldNotBeAbleToDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1")) // Ubah ID sesuai database
                .andExpect(status().isForbidden()); // Harusnya gagal
    }

    @Test
    void anyoneShouldBeAbleToGetProduct() throws Exception {
        // Arrange - Tambahin produk ke DB dulu
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(1000.0);
        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);
        product = productRepository.save(product); // Simpen ke DB

        // Act & Assert - Coba get produknya
        mockMvc.perform(get("/api/v1/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Harusnya 200 OK
    }
}