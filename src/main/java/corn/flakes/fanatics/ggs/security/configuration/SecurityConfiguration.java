package corn.flakes.fanatics.ggs.security.configuration;

import com.google.common.collect.ImmutableList;
import corn.flakes.fanatics.ggs.security.filter.JsonAuthenticationFilter;
import corn.flakes.fanatics.ggs.security.handler.AuthenticationEntryPointHandler;
import corn.flakes.fanatics.ggs.security.handler.AuthenticationFailureHandler;
import corn.flakes.fanatics.ggs.security.handler.AuthenticationSuccessHandler;
import corn.flakes.fanatics.ggs.security.handler.SuccessLogoutHandler;
import corn.flakes.fanatics.ggs.security.service.MongoUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    private final AuthenticationFailureHandler authenticationFailureHandler;
    
    private final AuthenticationEntryPointHandler authenticationEntryPointHandler;
    
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    
    private final SuccessLogoutHandler successLogoutHandler;
    
    private final MongoUserDetailsService mongoUserDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter();
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        jsonAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        jsonAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return jsonAuthenticationFilter;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mongoUserDetailsService);
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(ImmutableList.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(ImmutableList.of("GET", "POST", "PUT", "PATCH",
                "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(ImmutableList.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/register")
                .permitAll()
                .antMatchers("/user/**")
                .hasRole("USER")
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .and()
                .addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .deleteCookies("JESSSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessHandler(successLogoutHandler)
                .and()
                .oauth2Login()
                .and()
                .cors();
    }
    
}
