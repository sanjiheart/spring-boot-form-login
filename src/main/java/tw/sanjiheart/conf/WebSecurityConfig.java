package tw.sanjiheart.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tw.sanjiheart.svc.UserService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // required for @Secured
public class WebSecurityConfig {
  
  private UserService userService;
  
  private PasswordEncoder passwordEncoder;
  
  public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors() // required for @CrossOrigin
        .and()
        .authorizeHttpRequests()
        .antMatchers("/web/node_modules/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .and()
        .formLogin()
        .loginPage("/login").permitAll()
        .defaultSuccessUrl("/web/index.html", true)
        .and()
        .logout()
        .logoutSuccessUrl("/login");
    return http.build();
  }

  @Bean
  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userService)
        .passwordEncoder(passwordEncoder)
        .and()
        .build();
  }

}
