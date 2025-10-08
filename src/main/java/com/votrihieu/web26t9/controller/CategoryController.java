package com.votrihieu.web26t9.controller;

import com.votrihieu.web26t9.model.Category;
import com.votrihieu.web26t9.model.User;
import com.votrihieu.web26t9.repository.CategoryRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // --- QUERY MAPPINGS ---

    @QueryMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    @QueryMapping
    public Optional<Category> categoryById(@Argument Long id) {
        return categoryRepository.findById(id);
    }

    // --- MUTATION MAPPINGS ---

    // Input type cho Category với Validation
    record CategoryInput(
            @NotBlank(message = "Category name cannot be blank")
            @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
            String name,
            String images
    ) {}

    // Tạo Category
    @MutationMapping
    public Category createCategory(@Argument @Valid CategoryInput category) {
        Category newCategory = new Category(category.name(), category.images());
        return categoryRepository.save(newCategory);
    }

    // Cập nhật Category
    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument @Valid CategoryInput category) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();
            existingCategory.setName(category.name());
            existingCategory.setImages(category.images());
            return categoryRepository.save(existingCategory);
        }
        return null;
    }

    // Xóa Category
    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // SchemaMapping cho trường 'users' trong Category (để load lazy relations)
    @SchemaMapping
    public Set<User> users(Category category) {
        // Điều này đảm bảo rằng các user được load khi truy vấn từ GraphQL
        return category.getUsers();
    }
}