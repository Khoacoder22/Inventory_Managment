package com.example.InventoryManagment.services.impl;


import com.example.InventoryManagment.dtos.CategoryDTO;
import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.exception.NotFoundException;
import com.example.InventoryManagment.models.Category;
import com.example.InventoryManagment.repository.CategoryRepository;
import com.example.InventoryManagment.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;

    @Override
    public Response createCategory(CategoryDTO categoryDTO) {
        Category categoryTosave = modelMapper.map(categoryDTO, Category.class);

        categoryRepository.save(categoryTosave);

        return Response.builder()
                .status(200)
                .message("Category save successfully")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        categories.forEach(category -> category.setProducts(null));

        List<CategoryDTO> categoryDTOList = modelMapper.map(categories, new TypeToken<CategoryDTO>(){}.getType());

        return Response.builder()
                .status(200)
                .message("List of categories")
                .categories(categoryDTOList)
                .build();
    }

    @Override
    public Response getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                 .orElseThrow(() -> new NotFoundException("Category not found"));

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

         return  Response.builder()
                 .status(200)
                 .category(categoryDTO)
                 .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        existingCategory.setName(categoryDTO.getName());

        categoryRepository.save(existingCategory);

        return  Response.builder()
                .status(200)
                .message("successfully saved")
                .build();
    }

    @Override
    public Response deleteCategory(Long id) {
         categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        categoryRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("delete successfully")
                .build();
    }
}
