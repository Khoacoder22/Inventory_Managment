package com.example.InventoryManagment.repository;

import com.example.InventoryManagment.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
