package com.votrihieu.web26t9.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "[desc]") // Đặt tên cột là "[desc]" vì DESC là từ khóa trong SQL Server
    private String desc;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Mối quan hệ nhiều-một với User
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading để tránh n+1 queries
    @JoinColumn(name = "userid", nullable = false) // Tên cột khóa ngoại trong bảng Product
    private User user;

    // Constructor bỏ qua id cho create
    public Product(String title, Integer quantity, String desc, BigDecimal price, User user) {
        this.title = title;
        this.quantity = quantity;
        this.desc = desc;
        this.price = price;
        this.user = user;
    }
}