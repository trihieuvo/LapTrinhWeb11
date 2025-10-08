package com.votrihieu.web26t9.repository;

import com.votrihieu.web26t9.model.Product;
import com.votrihieu.web26t9.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Phương thức để tìm tất cả product theo Category id
    List<Product> findByUserCategoriesId(Long categoryId);

    // Phương thức để tìm tất cả product, sắp xếp theo giá
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();
    List<Product> findByUser(User user);
}