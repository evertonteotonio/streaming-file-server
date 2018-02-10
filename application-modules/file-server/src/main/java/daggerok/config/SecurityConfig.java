package daggerok.config;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override protected void configure(final HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .authorizeRequests()
//        .requestMatchers()
//          .antMatchers("/actuator/health")
//            .permitAll()
          .requestMatchers(EndpointRequest.to("status", "info", "health"))
            .permitAll()
          .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .permitAll()
          .anyRequest()
            .authenticated()
        .and()
          .formLogin()
            .disable()
        .headers()
          .frameOptions()
            .sameOrigin()
        .and()
          .csrf()
            .disable()
        .httpBasic()
    ;
    // @formatter:on
  }

  @Bean
  @SneakyThrows
  public UserDetailsService userDetailsService() {

    val inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

    inMemoryUserDetailsManager.createUser(User//.withUsername("user")
                                              .withDefaultPasswordEncoder()
                                              .username("user")
                                              .password("password")
                                              .roles("USER")
                                              .build());

    inMemoryUserDetailsManager.createUser(User//.withUsername("admin")
                                              .withDefaultPasswordEncoder()
                                              .username("admin")
                                              .password("admin")
                                              .roles("USER")
                                              .build());
    return inMemoryUserDetailsManager;
  }
}
