package dev.maczkowski.panelite.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class WebSecurityConfig {

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        return http
                .requestCache(requestCacheConfigurer -> { // to get rid of ?continue query param
                    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                    requestCache.setMatchingRequestParameterName(null);
                    requestCacheConfigurer.requestCache(requestCache);
                })
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(new AntPathRequestMatcher("/actuator/panelite/**"))
                        .hasAuthority("ROLE_HEROES")
                        .requestMatchers(mvc.pattern("/actuator/paneliteApi/**"))
                        .hasAuthority("ROLE_HEROES")
                        .requestMatchers(mvc.pattern("/**"))
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .contextSource()
                .url("ldap://localhost:389/dc=maczkowski,dc=dev")
                .and()
                .userDnPatterns("cn={0},ou=users")
                .userSearchBase("ou=users")
                .groupSearchBase("ou=users")
                .groupSearchFilter("(member={0})")
                .passwordCompare()
                .passwordAttribute("userPassword");
    }

}
