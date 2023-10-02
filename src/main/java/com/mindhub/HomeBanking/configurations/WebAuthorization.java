package com.mindhub.HomeBanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/index.html", "/assets/styles/login.css", "/assets/styles/style.css" , "/assets/images/**", "/scripts/login.js").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/login", "/api/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/api/clients", "/loans/admin").hasAuthority("ADMIN")
                .antMatchers("/rest/**", "/admin/**", "/manager.html", "/api/loans/admin", "/web/admin/**", "/scripts/admin/**").hasAuthority("ADMIN")
                .antMatchers("/web/**", "/sound/**", "/scripts/**", "/assets/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/accounts/**", "/api/cards/**", "/api/loans/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/api/accounts/**", "/api/active/**",
                        "/api/cards/**", "/api/loans/**", "/api/transactions/**", "/api/clients/**", "/api/pdf/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/accounts", "/api/transactions", "/api/cards", "/api/loans", "/api/transactions/pdf/**").hasAuthority("CLIENT")
                        .anyRequest().permitAll();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.formLogin().defaultSuccessUrl("/web/accounts.html").permitAll();

        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID").logoutSuccessUrl("/index.html");


        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();

    }
// sesion actual
    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}

