package learning.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createThenFindProduct() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"sku":"T901","name":"测试耳机","stock":7}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("T901"));
        mockMvc.perform(get("/products/T901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("测试耳机"))
                .andExpect(jsonPath("$.stock").value(7));

    }

    @Test
    void duplicateSkuDoesNotOverwrite() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"sku":"A001","name":"新键盘","stock":999}
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE_SKU"));
        mockMvc.perform(get("/products/A001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("键盘"))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void invalidInputDoesNotCreateProduct() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"sku":"T902","name":"","stock":-1}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_PRODUCT_INPUT"));
        mockMvc.perform(get("/products/T902"))
                .andExpect(status().isNotFound());

    }

    @Test
    void missingProductReturns404() throws Exception {
        mockMvc.perform(get("/products/A999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }
}
