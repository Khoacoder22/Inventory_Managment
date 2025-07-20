package com.example.InventoryManagment.specification;

import com.example.InventoryManagment.models.Transaction;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {

    //search từ khoa trong database
    public static Specification<Transaction> byFilter(String searchValue) {
        return (root, query, criteriaBuilder) -> {
            if (searchValue == null || searchValue.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + searchValue.toLowerCase() + "%"; // tạo mẫu tìm kiếm
            List<Predicate> predicates = new ArrayList<>();

            // tìm kiếm nhiều trường của bảng transaction và các liên quan(user, supplier, product, category)
            // Transaction fields
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("note")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("transactionType").as(String.class)), searchPattern));

            // Join with user
            Join<Object, Object> userJoin = root.join("user", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("name")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("email")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("phoneNumber")), searchPattern));

            // Join with supplier
            Join<Object, Object> supplierJoin = root.join("supplier", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(supplierJoin.get("name")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(supplierJoin.get("contactInfor")), searchPattern));

            // Join with product
            Join<Object, Object> productJoin = root.join("product", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("name")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("sku")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("description")), searchPattern));
        
            // Join with product.category
            Join<Object, Object> categoryJoin = productJoin.join("category", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("name")), searchPattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };

        //SPRING BOOT -> SQL
        // SELECT * FROM transactions t
        //LEFT JOIN users u ON t.user_id = u.id
        //LEFT JOIN suppliers s ON t.supplier_id = s.id
        //LEFT JOIN products p ON t.product_id = p.id
        //LEFT JOIN categories c ON p.category_id = c.id
        //WHERE
        //  LOWER(t.description) LIKE '%laptop%' OR
        //  LOWER(u.name) LIKE '%laptop%' OR
        //  LOWER(p.name) LIKE '%laptop%' OR
        //  ...
    }
}
