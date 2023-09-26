package com.yamukja.security.config;

import com.yamukja.security.filter.JwtAuthenticationFilter;
import com.yamukja.security.handler.MemberAuthenticationFailureHandler;
import com.yamukja.security.handler.MemberAuthenticationSuccessHandler;
import com.yamukja.security.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.*;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .headers().frameOptions().sameOrigin() // H2 웹 콘솔을 정상적으로 사용하기 위한 설정
//                .and()
//                .csrf().disable()
//                .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/process_login")
//                .failureUrl("/login?error")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/")
//                .and()
//                .exceptionHandling().accessDeniedPage("/access-denied") // 403에러 발생시 리다이렉트
//                .and()
//                .authorizeHttpRequests(authorize -> authorize
////                        .antMatchers("/members/**").hasRole("USER") >> "/members" 및 그 하위 모든 uri와 매칭된다.
//                        .antMatchers("/**").permitAll()
//                );

        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(withDefaults()) // withDefaults()를 적용하면 corsConfigurationSource라는 이름으로 등록된 Bean을 이용한다.
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // vs List.of() 차이 다시 정리하기!!
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/login"); // "/login"이 디폴트라 생략가능
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler()); // 일반적으로 인증을 위한 Security Filter마다 handler 구현 클래스를 각각 생성하기 때문에 new 키워드를 사용해도 무방하다.
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());
            builder.addFilter(jwtAuthenticationFilter);
        }
    }
}
