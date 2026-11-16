package learning.web.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class CreateProductRequest {
    @NotBlank
    private String sku;

    @NotBlank
    @Size(max=20)
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    public CreateProductRequest() {}

    public String getSku() {
        return this.sku;
    }

    public String getName() {
        return this.name;
    }

    public Integer  getStock() {
        return this.stock;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}
