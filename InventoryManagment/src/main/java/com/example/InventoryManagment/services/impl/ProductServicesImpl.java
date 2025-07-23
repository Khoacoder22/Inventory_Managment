package com.example.InventoryManagment.services.impl;

import com.example.InventoryManagment.dtos.CategoryDTO;
import com.example.InventoryManagment.dtos.ProductDTO;
import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.exception.NotFoundException;
import com.example.InventoryManagment.models.Category;
import com.example.InventoryManagment.models.Product;
import com.example.InventoryManagment.repository.CategoryRepository;
import com.example.InventoryManagment.repository.ProductRepository;
import com.example.InventoryManagment.services.ProductServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServicesImpl implements ProductServices {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-images";

    @Override
    public Response SaveProduct(ProductDTO productDTO, MultipartFile multipartFile) {

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Not found"));

        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .build();

        if(multipartFile != null && !multipartFile.isEmpty()){
            log.info("Image doesn't exist");
            String imagePath = saveImage(multipartFile);
            productToSave.setImageURL(imagePath);
        }

        productRepository.save(productToSave);
        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response DeleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Product not found"));

        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("deleted success")
                .build();
    }

    @Override
    public Response UpdateProduct(ProductDTO productDTO, MultipartFile multipartFile) {
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        //check if product exist
        if (multipartFile != null & !multipartFile.isEmpty()){
            String imagePath = saveImage(multipartFile);
        }

        if(productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0){
            Category category = categoryRepository.findById(productDTO.getProductId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }

        // Check if product fields is to be changed and update
        if(productDTO.getName() != null && !productDTO.getName().isBlank()){
            existingProduct.setName(productDTO.getName());
        }

        if(productDTO.getName() != null && !productDTO.getName().isBlank()){
            existingProduct.setSku(productDTO.getSku());
        }

        if(productDTO.getName() != null && !productDTO.getName().isBlank()){
            existingProduct.setDescription(productDTO.getDescription());
        }

        if(productDTO.getPrice() != null && !(productDTO.getPrice() > 0)){
            existingProduct.setPrice(productDTO.getPrice());
        }
        productRepository.save(existingProduct);

        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response getAllProduct() {
        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDTO> productDTOList = modelMapper.map(productList, new TypeToken< List<ProductDTO>>(){}.getType());

        productList.forEach(product -> product.setCategory(null));

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOList)
                .build();
    }

    @Override
    public Response SearchProduct(String input) {

        List<Product> products = productRepository.findByNameContainingOrDescriptionContaining(input, input);

        if(products.isEmpty()){
            throw new NotFoundException("Product not found");
        }

        List<ProductDTO> productDTOList = modelMapper.map(products, new TypeToken< List<ProductDTO>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOList)
                .build();
    }

    @Override
    public Response getById(Long id){
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Product not found"));

        return Response.builder()
                .status(200)
                .message("success")
                .product(modelMapper.map(existingProduct, ProductDTO.class))
                .build();
    }

    // save image file path
    private String saveImage(MultipartFile multipartFile) {
        if(!multipartFile.getContentType().startsWith("image/") || multipartFile.getSize() > 1024 * 1024 * 1024){
            throw new IllegalArgumentException("Only image file under 1GIG is allowed");
        }
        File directory = new File(IMAGE_DIRECTORY);
        if(!directory.exists()){
            directory.mkdir();
            log.info("Directory was created");
        }

        //generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + " " + multipartFile.getOriginalFilename();

        // lấy path
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            multipartFile.transferTo(destinationFile);
        } catch (IOException e){
            throw new IllegalArgumentException("Error saving image" + e.getMessage());
        }
        return imagePath;
    }
}
