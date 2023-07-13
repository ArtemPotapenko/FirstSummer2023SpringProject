package com.example.server.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String tittle;
    @Column
    private String caption;
    @Column
    private String location;
    @Column
    private Integer likes;
    @Column
    @ElementCollection(targetClass = javax.print.attribute.TextSyntax.class)
    private Set<User> likedUsers = new HashSet<>();
    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Column
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();
    @Column(updatable = false)
    private Date createDate;

    @PrePersist
    public void onCreate(){
        createDate = new Date();
    }
}
