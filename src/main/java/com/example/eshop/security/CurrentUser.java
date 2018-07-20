package com.example.eshop.security;

import com.example.eshop.model.User;
import com.example.eshop.model.UserRole;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return user.getId();
    }

    public UserRole getRole() {
        return user.getRole();
    }

}