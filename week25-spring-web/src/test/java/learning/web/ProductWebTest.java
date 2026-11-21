package learning.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties={
        "spring.datasource.url=jdbc:mysql://127.0.0.1:3306/week26_catalog_test",
        "spring.datasource.username=backend_user",
        "spring.datasource.password=${TEST_DB_PASSWORD}"
})
public class ProductWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    private void deleteTestSku(String sku) throws SQLException {
        try(Connection connection=dataSource.getConnection()) {
            if(!"week26_catalog_test".equals(connection.getCatalog())) {
                throw new IllegalStateException("Refusing to modify non-test database");
            }
            String sql="DELETE FROM products WHERE sku=?";
            try(PreparedStatement ps=connection.prepareStatement(sql)) {
                ps.setString(1,sku);
                ps.executeUpdate();
            }
        }
    }
    @BeforeEach
    void setUP() throws SQLException {
        deleteTestSku("T901");
        deleteTestSku("T903");
        deleteTestSku("T902");
        deleteTestSku("T904");
    }

    @AfterEach
    void tearDown() throws SQLException {
        deleteTestSku("T901");
        deleteTestSku("T903");
        deleteTestSku("T902");
        deleteTestSku("T904");
    }

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
                        {"sku":"T903","name":"原键盘","stock":10}
                        """))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"sku":"T903","name":"新键盘","stock":999}
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE_SKU"));
        mockMvc.perform(get("/products/T903"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("原键盘"))
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
        mockMvc.perform(get("/products/T904"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("PRODUCT_NOT_FOUND"));
    }
}
