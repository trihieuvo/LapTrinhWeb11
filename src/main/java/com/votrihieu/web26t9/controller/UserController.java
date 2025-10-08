package com.votrihieu.web26t9.controller;

import com.votrihieu.web26t9.model.Category;
import com.votrihieu.web26t9.model.User;
import com.votrihieu.web26t9.repository.CategoryRepository;
import com.votrihieu.web26t9.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public UserController(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // --- QUERY MAPPINGS ---

    @QueryMapping
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @QueryMapping
    public Optional<User> userById(@Argument Long id) {
        return userRepository.findById(id);
    }

    // --- MUTATION MAPPINGS ---

    // Input type cho User với Validation
    record UserInput(
        @NotBlank(message = "Fullname cannot be blank")
        String fullname,
        
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,
        
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,
        
        String phone,
        List<Long> categoryIds
    ) {}

    // Tạo User
    @MutationMapping
    public User createUser(@Argument @Valid UserInput user) {
        User newUser = new User(user.fullname(), user.email(), user.password(), user.phone());
        if (user.categoryIds() != null && !user.categoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(user.categoryIds()));
            newUser.setCategories(categories);
        }
        return userRepository.save(newUser);
    }

    // Cập nhật User
    @MutationMapping
    public User updateUser(@Argument Long id, @Argument @Valid UserInput user) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setFullname(user.fullname());
            existingUser.setEmail(user.email());
            existingUser.setPassword(user.password());
            existingUser.setPhone(user.phone());
            
            if (user.categoryIds() != null) {
                Set<Category> categories = new HashSet<>(categoryRepository.findAllById(user.categoryIds()));
                existingUser.setCategories(categories);
            } else {
                existingUser.setCategories(new HashSet<>());
            }

            return userRepository.save(existingUser);
        }
        return null;
    }

    // Xóa User
    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @SchemaMapping
    public Set<Category> categories(User user) {
        return user.getCategories();
    }
}