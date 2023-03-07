package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.UserRepository;
import com.yuri.shoppingsite.exception.DataNotFoundException;
import com.yuri.shoppingsite.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String password, String email, String nickname, String birth, String phone){
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setNickname(nickname);
        user.setBirth(birth);
        user.setPhone(phone);
        this.userRepository.save(user);
        return user;
    }
    public SiteUser getUser(String username){
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if(siteUser.isPresent()){
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser is not found");
        }
    }

}
