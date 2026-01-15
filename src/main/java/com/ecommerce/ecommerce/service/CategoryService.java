package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CategoryDto;
import com.ecommerce.ecommerce.entity.CategoryEntity;
import com.ecommerce.ecommerce.exceptionHanding.ResourceNotFoundException;
import com.ecommerce.ecommerce.repository.CategoryRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepo categoryRepo;
    public CategoryService(CategoryRepo categoryRepo){
        this.categoryRepo = categoryRepo;
    }
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        CategoryEntity categoryByName = categoryRepo.findCategoryByName(categoryDto.name()).orElse(null);
        if(categoryByName == null){
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName(categoryDto.name());
            categoryByName = categoryRepo.save(categoryEntity);
        }
        else{
            throw new ResourceNotFoundException("Category Already Exists");
        }
        return new CategoryDto(categoryByName.getCategory(),categoryByName.getName());

    }


    public List<CategoryDto> getCategories(){
        List<CategoryEntity> all = categoryRepo.findAll();

        return all.stream().map(category -> new CategoryDto(category.getCategory(),category.getName())).toList();
    }

}
