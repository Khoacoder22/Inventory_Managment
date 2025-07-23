package com.example.InventoryManagment.controller;

import com.example.InventoryManagment.dtos.CategoryDTO;
import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.SupplierDTO;
import com.example.InventoryManagment.services.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addSupplier(@RequestBody @Valid SupplierDTO supplierDTO){
        return ResponseEntity.ok(supplierService.addSupplier(supplierDTO));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateCategory(@PathVariable Long id, @RequestBody @Valid SupplierDTO supplierDTO){
        return ResponseEntity.ok(supplierService.UpdateSupplier(id, supplierDTO));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long id)
    {
        return ResponseEntity.ok((supplierService.deleteSupplier(id)));
    }

    @GetMapping("/getAll")
    public ResponseEntity<Response> getSupplier(){
        return ResponseEntity.ok((supplierService.getAllSupplier()));
    }
}
