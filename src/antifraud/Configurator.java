package antifraud;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
    public class Configurator extends WebSecurityConfigurerAdapter {

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index.html", "/public","/h2-console/**","/error/**","/api/auth/list/**","/api/auth/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/auth/list/**")
                .hasRole("User")
                .antMatchers("/actuator/shutdown").permitAll() // needs to run test
                .anyRequest().permitAll()
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .httpBasic();

    }

}