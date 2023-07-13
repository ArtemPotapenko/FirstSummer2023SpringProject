package com.example.server.services;

import com.example.server.entity.Comment;
import com.example.server.entity.User;
import com.example.server.repository.UserRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return build(userRepository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " : User doesn't found with this email.")));
    }
    public User loadUserById(Long id){
        return build(userRepository.findUserById(id).orElseThrow(()->new UsernameNotFoundException("Not found this id : "+id)));
    }
    public static User build(User user){
        List<? extends GrantedAuthority> authorities = user.getRoles().stream().map(x->new SimpleGrantedAuthority(x.name())).toList();
        return new User(user.getId(),user.getUsername(),user.getEmail(),user.getPassword(),authorities);

    }
}
