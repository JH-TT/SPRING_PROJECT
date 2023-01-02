package com.mysite.sbb.config;

import com.mysite.sbb.Service.UserSecurityService;
import com.mysite.sbb.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // PreAuthorize, PostAuthorize 어노테이션 사용가능.
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final UserSecurityService userSecurityService;
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        // 모든 인증되지 않은 요청을 허락.
//        http
//                // token을 사용하는 방식이기 때문에, csrf를 disable한다.
//                .csrf().disable()
//
//                .exceptionHandling() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다는 의미
//
//                .antMatchers("/**").permitAll()
////                .anyRequest().authenticated() // 나머지 요청은 인증받겠다.
//                .and()
//                    .formLogin()
//                    .loginPage("/user/login")
//                    .defaultSuccessUrl("/")
//                .and()
//                    .logout()
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                    .logoutSuccessUrl("/") // 루트URL로 이동
//                    .invalidateHttpSession(true) // 세션삭제
//                ;
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 모든 인증되지 않은 요청을 허락.
        http
                .addFilter(corsConfig.corsFilter())
                // token을 사용하는 방식이기 때문에, csrf를 disable한다.
                .csrf().disable()

                .exceptionHandling() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다는 의미
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated() // 나머지 요청은 인증받겠다.
//                .and()
//                    .formLogin()
//                    .loginPage("/user/login")
//                    .defaultSuccessUrl("/")
//                .and()
//                    .logout()
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                    .logoutSuccessUrl("/") // 루트URL로 이동
//                    .invalidateHttpSession(true) // 세션삭제
                .and()
                .apply(new JwtSecurityConfig(tokenProvider))
                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
