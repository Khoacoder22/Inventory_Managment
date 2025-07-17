package com.example.InventoryManagment.models;

import com.example.InventoryManagment.enums.TransactionStatus;
import com.example.InventoryManagment.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalProducts;

    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String description;

    private String note;

    private final LocalDateTime createAt = LocalDateTime.now();

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Override
    public String toString() {
        return "Transaction{" +
                "updateAt=" + updateAt +
                ", createAt=" + createAt +
                ", note='" + note + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", transactionType=" + transactionType +
                ", totalPrice=" + totalPrice +
                ", totalProducts=" + totalProducts +
                ", id=" + id +
                '}';
    }
}
