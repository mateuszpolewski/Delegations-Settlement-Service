package pl.pracainz.delegacje.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.pracainz.delegacje.service.MyUserDetailsService;


    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;
        @Autowired
        private MyUserDetailsService userDetailsService;
        @Autowired
        private CustomSuccessAuthenticationHandler customSuccessAuthenticationHandler;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.
                    authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/forgot-password").permitAll()
                    .antMatchers("/mobile/**").permitAll()
                    .antMatchers("/auth/**").hasAnyAuthority("ADMIN", "USER", "ACCOUNTANT", "OWNER")
                    .antMatchers("/admin/**").hasAuthority("ADMIN")
                    .antMatchers("/accountant/**").hasAnyAuthority("ACCOUNTANT")
                    .antMatchers("/owner/**").hasAuthority("OWNER")
                    .antMatchers("/user/**").hasAuthority("USER")
                    .antMatchers("/management/**").hasAnyAuthority("ADMIN", "ACCOUNTANT", "OWNER")
                    .anyRequest()
                    .authenticated()
                    .and().csrf().disable()
                    .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .successHandler(customSuccessAuthenticationHandler)
                    //.defaultSuccessUrl("/home",true)
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .and().logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/").and().exceptionHandling();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .ignoring()
                    .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
        }

    }


