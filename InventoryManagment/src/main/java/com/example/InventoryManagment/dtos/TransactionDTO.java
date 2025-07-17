package com.example.InventoryManagment.dtos;

import com.example.InventoryManagment.enums.TransactionStatus;
import com.example.InventoryManagment.enums.TransactionType;
import com.example.InventoryManagment.models.Product;
import com.example.InventoryManagment.models.Supplier;
import com.example.InventoryManagment.models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;

    private int totalProducts;

    private Long totalPrice;

    private TransactionType transactionType;

    private TransactionStatus status;

    private String description;

    private String note;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private ProductDTO product;

    private UserDTO user;

    private SupplierDTO supplier;

}
