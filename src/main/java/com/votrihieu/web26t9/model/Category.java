package com.votrihieu.web26t9.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String images; // Tên cột trong DB là 'images'

    // Mối quan hệ nhiều-nhiều với User
    // Đây là bên sở hữu mối quan hệ (owning side)
    @ManyToMany(mappedBy = "categories")
    private Set<User> users = new HashSet<>();

    // Constructor bỏ qua id cho create
    public Category(String name, String images) {
        this.name = name;
        this.images = images;
    }
}