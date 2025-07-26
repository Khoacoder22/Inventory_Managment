package com.example.InventoryManagment.controller;


import com.example.InventoryManagment.dtos.ProductDTO;
import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServices productServices;

    @PostMapping("/add")
    public ResponseEntity<Response> saveProduct(@RequestParam(value ="imageFile", required = false)MultipartFile multipartFile,
                                                @RequestParam(value ="name", required = false) String name,
                                                @RequestParam(value ="sku", required = false) String sku,
                                                @RequestParam(value ="price", required = false) Long price,
                                                @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
                                                @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                @RequestParam(value = "description", required = false) String description)
    {

        ProductDTO productDTO = new ProductDTO();
            productDTO.setName(name);
            productDTO.setPrice(BigDecimal.valueOf(price));
            productDTO.setStockQuantity(stockQuantity);
            productDTO.setSku(sku);
            productDTO.setCategoryId(categoryId);
            productDTO.setDescription(description);

        return ResponseEntity.ok(productServices.SaveProduct(productDTO, multipartFile));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sku", required = false) String sku,
            @RequestParam(value = "price", required = false) Long price,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("productId") Long productId
    ) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setSku(sku);
        productDTO.setPrice(BigDecimal.valueOf(price));
        productDTO.setProductId(productId);
        productDTO.setStockQuantity(stockQuantity);
        productDTO.setCategoryId(categoryId);
        productDTO.setDescription(description);

        return ResponseEntity.ok(productServices.UpdateProduct(productDTO, imageFile));

    }
    @GetMapping("/all")
    public ResponseEntity<Response> getAllProduct(){
        return ResponseEntity.ok(productServices.getAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id){
        return ResponseEntity.ok(productServices.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteById(@PathVariable Long id){
        return ResponseEntity.ok(productServices.DeleteProduct(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchProduct(@PathVariable String input){
        return ResponseEntity.ok(productServices.SearchProduct(input));
    }
}
