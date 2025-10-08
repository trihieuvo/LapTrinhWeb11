package com.votrihieu.web26t9.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "[User]")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails { // Implement UserDetails
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String fullname;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Product> products = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "UserCategory", joinColumns = @JoinColumn(name = "userid"), inverseJoinColumns = @JoinColumn(name = "categoryid"))
	private Set<Category> categories = new HashSet<>();

	public User(String fullname, String email, String password, String phone) {
		this.fullname = fullname;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = Role.USER;
	}

	// CÁC PHƯƠNG THỨC CỦA USERDETAILS
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Trả về một danh sách quyền, ở đây ta dùng role
		return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		// Spring Security sẽ dùng email để định danh user
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // tài khoản không hết hạn
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // tài khoản không bị khóa
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // mật khẩu không hết hạn
	}

	@Override
	public boolean isEnabled() {
		return true; // tài khoản được kích hoạt
	}
}