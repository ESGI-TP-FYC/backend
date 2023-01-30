package fr.esgi.ecommerce.configuration;

import fr.esgi.ecommerce.security.JwtConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable().authorizeRequests()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**",
                        "/api/v1/auth/login",
                        "/api/v1/registration/**",
                        "/api/v1/products/**",
                        "/api/v1/users/cart",
                        "/api/v1/users/order/**",
                        "/api/v1/users/review",
                        "/websocket", "/websocket/**",
                        "/img/**",
                        "/static/**").permitAll()
                .antMatchers("/auth/**", "/oauth2/**", "/**/*swagger*/**", "/v2/api-docs").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(jwtConfigurer);
    }
    /**
     * Configuration principale du coeur d'application
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        //ici on autorise ici toute les origines par defaut. celle-ci seront filtrées ensuite via le allowedOriginFilter ce qui permet d'ajotuer des allowedOrigin en runtime
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(
                Collections.unmodifiableList(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"))
        );
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(false);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders((Collections.singletonList("*")));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
