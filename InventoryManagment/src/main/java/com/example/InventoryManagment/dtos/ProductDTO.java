package com.example.InventoryManagment.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    private Long categoryId;

    private Long supplierId;

    private Long productId;

    private String name;

    private String sku;

    private BigDecimal price;

    private int stockQuantity;

    private String description;

    private String imageURL;

    private LocalDateTime createAt;

    private CategoryDTO category;
}
