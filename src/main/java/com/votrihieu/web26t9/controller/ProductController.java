package com.votrihieu.web26t9.controller;

import com.votrihieu.web26t9.model.Product;
import com.votrihieu.web26t9.model.User;
import com.votrihieu.web26t9.repository.ProductRepository;
import com.votrihieu.web26t9.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository; // Cần để liên kết Product với User

    public ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // --- QUERY MAPPINGS ---

    // Hiển thị tất cả product, sắp xếp theo giá
    @QueryMapping
    public List<Product> allProducts(@Argument String sortByPrice) {
        if ("desc".equalsIgnoreCase(sortByPrice)) {
            return productRepository.findAllByOrderByPriceDesc();
        }
        return productRepository.findAllByOrderByPriceAsc();
    }

    // Lấy product theo ID
    @QueryMapping
    public Optional<Product> productById(@Argument Long id) {
        return productRepository.findById(id);
    }

    // Lấy tất cả product của 01 category
    @QueryMapping
    public List<Product> productsByCategoryId(@Argument Long categoryId) {
        return productRepository.findByUserCategoriesId(categoryId);
    }
    
    // Lấy tất cả product của 01 user
    @QueryMapping
    public List<Product> productsByUserId(@Argument Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return productRepository.findByUser(user); 
    }

    // --- MUTATION MAPPINGS ---

    // Input type cho Product với Validation
    record ProductInput(
        @NotBlank(message = "Title cannot be blank")
        String title,
        
        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be non-negative")
        Integer quantity,
        
        String desc,

        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be non-negative")
        Double price,

        @NotNull(message = "User ID is required")
        Long userId
    ) {}

    // Tạo Product
    @MutationMapping
    public Product createProduct(@Argument @Valid ProductInput product) {
        User user = userRepository.findById(product.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + product.userId()));

        Product newProduct = new Product(
                product.title(),
                product.quantity(),
                product.desc(),
                java.math.BigDecimal.valueOf(product.price()),
                user
        );
        return productRepository.save(newProduct);
    }

    // Cập nhật Product
    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument @Valid ProductInput product) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            User user = userRepository.findById(product.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + product.userId()));

            existingProduct.setTitle(product.title());
            existingProduct.setQuantity(product.quantity());
            existingProduct.setDesc(product.desc());
            existingProduct.setPrice(java.math.BigDecimal.valueOf(product.price()));
            existingProduct.setUser(user);
            return productRepository.save(existingProduct);
        }
        return null; 
    }

    // Xóa Product
    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @SchemaMapping
    public User user(Product product) {
        return product.getUser();
    }
}