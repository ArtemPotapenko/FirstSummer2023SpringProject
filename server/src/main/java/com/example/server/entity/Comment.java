package com.example.server.entity;
import com.example.server.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;
@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue()
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private Long userId;
    @Column(columnDefinition = "text",nullable = false)
    private String message;
    @Column(updatable = false)
    private Date createDate;

    @PrePersist
    public void onCreate(){
        createDate = new Date();
    }


}
