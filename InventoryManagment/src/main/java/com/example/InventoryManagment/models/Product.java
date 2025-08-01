package com.example.InventoryManagment.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "is required")
    private String name;

    @Column(unique = true)
    @NotBlank(message = "is required")
    private String sku;

    @NotBlank(message = "is required")
    @Positive(message = "price is not negative")
    private Long price;

    @NotBlank(message = "is required")
    @Min(value = 0, message = "quantity cannot be negative")
    private int stockQuantity;

    @NotBlank(message = "is required")
    private String description;

    @NotBlank(message = "is required")
    private String imageURL;

    @NotBlank(message = "is required")
    private final LocalDateTime createAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
