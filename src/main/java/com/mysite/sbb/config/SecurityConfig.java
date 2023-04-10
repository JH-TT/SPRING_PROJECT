package com.mysite.sbb.config;

import com.mysite.sbb.OAuth.PrincipalOauth2UserService;
import com.mysite.sbb.Service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // PreAuthorize, PostAuthorize 어노테이션 사용가능.
public class SecurityConfig {

    private final UserSecurityService userSecurityService;
    private final PrincipalOauth2UserService principalOauth2UserService;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 모든 인증되지 않은 요청을 허락.
        http
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll()
                .and()
                    .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/") // 루트URL로 이동
                    .invalidateHttpSession(true) // 세션삭제
                .and()
                    .oauth2Login()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/user/setNickName")
    //                .defaultSuccessUrl("/")
                    .failureUrl("/user/login")
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService);

        return http.build();
    }

}
