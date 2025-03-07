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

        Product product = new Product();
        product.setName("Product 1");
        product.setDescription("Ini Produk 1");
        product.setPrice(1000.0);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void adminShouldBeAbleToUpdateProduct() throws Exception {
        // Buat produk dulu
        CreateProductDTO newProduct = new CreateProductDTO();
        newProduct.setName("Product Before Update");
        newProduct.setDescription("Old Desc");
        newProduct.setPrice(1500.0);

        String response = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andReturn().getResponse().getContentAsString();

        // Ambil ID produk yang baru dibuat
        Long productId = objectMapper.readTree(response).get("id").asLong();

        UpdateProductDTO updateProductDTO = new UpdateProductDTO();
        updateProductDTO.setName("Product 1 Updated");
        updateProductDTO.setDescription("Ini Produk 1 Updated");
        updateProductDTO.setPrice(1500.0);

        mockMvc.perform(put("/api/v1/products/" + productId) // Ubah ID sesuai database
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateProductDTO)))
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
        product = productRepository.save(product); // Simpen ke DB

        // Act & Assert - Coba get produknya
        mockMvc.perform(get("/api/v1/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Harusnya 200 OK
    }
}