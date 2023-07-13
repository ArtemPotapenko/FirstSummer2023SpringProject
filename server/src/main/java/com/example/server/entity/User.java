package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.server.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
@Setter
@Getter
@NoArgsConstructor
@Entity
public class User implements UserDetails {
    public User(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(columnDefinition = "text")
    private String bio;
    @Column(nullable = false, length = 3000)
    private String password;
    @ElementCollection
    @CollectionTable(name = "user_role",joinColumns =@JoinColumn(name = "user_id"))
    private Set<ERole> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user",orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss ")
    private Date createDate;

    @PrePersist
    public void onCreate(){
        createDate = new Date();
    }
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
