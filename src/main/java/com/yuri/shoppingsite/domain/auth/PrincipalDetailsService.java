package com.yuri.shoppingsite.domain.auth;

import com.yuri.shoppingsite.Repository.MemberRepository;
import com.yuri.shoppingsite.domain.auth.PrincipalDetails;
import com.yuri.shoppingsite.domain.user.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member byUsername = memberRepository.findByName(username);
        if(byUsername != null){
            return new PrincipalDetails(byUsername);
        }
        return null;
    }
}