package com.example.eshop.security;


import com.example.eshop.model.User;
import com.example.eshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findOneByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with %s email is not found", s));
        }
        return new CurrentUser(user);
    }


    public  void saveUser() {
        //save image
        //send mail to admin and registered user
        //save user in db repository
        // 3 layer   ----<> user request backend -> Controller -> service (mailing, image save, some processes, repository) -> repository

    }
}
