package com.sglink.config;

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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.sglink.service.STU_MemberService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   
   @Autowired
   STU_MemberService memberService;
   
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.formLogin().loginPage("/members/stu/login") // 로그인 페이지 url을 설정
            .defaultSuccessUrl("/") // 로그인 성공 시 이동할 url
            .usernameParameter("stuuserId") // 로그인 시 사용할 파라미터 이름으로 email을 지정
            .failureUrl("/members/stu/login/error") // 로그인 실패 시 이동할 url을 설정
            .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/members/stu/logout")) // 로그아웃 url을 설정
            .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 url을 설정
      ;
      http.authorizeRequests()
      .mvcMatchers("/", "/members/**").permitAll() // 모든 사용자 인증없이 해당경로에 접근하도록 설정
      .mvcMatchers("/admin/**").hasRole("ADMIN") // /admin 경로 접근자는 ADMIN Role일 경우만 접근가능하도록 설정
//      .anyRequest().authenticated() // 나머지 경로들은 모두 인증을 요구하도록 설정
      ;
      http.exceptionHandling() // 인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러 등록.
      .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
      ;
      
      http.csrf().ignoringAntMatchers("/board/notice/**"); //공지게시판 시큐리티 허용
      
      
      http.csrf().ignoringAntMatchers("/board/**");//게시판 시큐리티 허용


   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
   }
   
   @Override
   public void configure(WebSecurity web) throws Exception {
   web.ignoring().antMatchers("/css/**", "/js/**", "/img/**"); 
   }
}