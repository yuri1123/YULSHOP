package com.yuri.shoppingsite.config;

import com.yuri.shoppingsite.service.PrincipalOauth2UserService;
import com.yuri.shoppingsite.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;
    @Autowired private PrincipalOauth2UserService principalOauth2UserService;
    @Override
    // http 요청에 대한 보안을 설정한다.
    // 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정
    protected void configure(HttpSecurity http) throws Exception{
//        http
//                .csrf()
//                .disable();
         http.headers()
                 .and()
                 .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                 .and();

        http.formLogin()
                //로그인 페이지 URL 설정
                .loginPage("/member/login")
                //로그인 성공시 이동할 URL 설정
                .defaultSuccessUrl("/")
                //로그인 시 사용할 파라미터 이름으로 name 지정
                .usernameParameter("name")
                //로그인 실패 시 이동할 URL을 설정
                .failureUrl("/member/login/error")
                .and()
                .logout()
                //로그아웃 URL 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                //로그아웃 성공 시 이동할 URL 설정
                .logoutSuccessUrl("/");

        http.oauth2Login()				// OAuth2기반의 로그인인 경우
                .loginPage("/member/login")		// 인증이 필요한 URL에 접근하면 /loginForm으로 이동
                .defaultSuccessUrl("/")			// 로그인 성공하면 "/" 으로 이동
                .failureUrl("/member/login")		// 로그인 실패 시 /loginForm으로 이동
                .userInfoEndpoint()			// 로그인 성공 후 사용자정보를 가져온다
                .userService(principalOauth2UserService);	//사용자정보를 처리할 때 사용한다


        //시큐리티 처리에 HttpServletRequest 이용
        http.authorizeRequests()
                //permitAll()을 통해 모든 사용자가 인증(로그인)없이 해당 경로에 접근할 수 있도록 설정한다.
                //메인페이지, 회원관련 URL, 상품 상세 페이지, 상품이미지 불러오기가 해당
                .antMatchers("/", "/shopping/**","/include/**","/member/login","/member/signup").permitAll()
                .antMatchers("/order/**","/cart/**","/community/**","/member/**").hasRole("ADMIN,USER")
                // /amdin으로 시작하는 경로는 해당 계정이 ADMIN Role인 경우에만 접근 가능하도록 설정
                .antMatchers("/admin/**").hasRole("ADMIN")
                //나머지 경로는 모드 인증을 요구하도록 설정
                .anyRequest().authenticated();
        //미인증 사용자가 리소스에 접근하였을 때 수행되는 핸들러 등록
//        http.exceptionHandling()
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    //spring security에서 인증은 AuthenticationManager를 통해 이루어지며
    //AuthenticationManagerBUilder가 AuthenticationManager를 생성한다.
    //userDetailService를 구현하고 있는 객체로 memberService 지정,
    // 비밀번호 암호화 위해 passwordEncoder 지정
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(encoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**/**","/admin_asset/**/**");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }