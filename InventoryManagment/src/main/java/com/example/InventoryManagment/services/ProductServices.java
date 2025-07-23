package com.example.InventoryManagment.services;


import com.example.InventoryManagment.dtos.ProductDTO;
import com.example.InventoryManagment.dtos.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductServices {

    Response SaveProduct(ProductDTO productDTO, MultipartFile multipartFile);

    Response DeleteProduct(Long id);

    Response UpdateProduct(ProductDTO productDTO, MultipartFile multipartFile);

    Response getAllProduct();

    Response SearchProduct(String input);

    Response getById(Long id);
}
