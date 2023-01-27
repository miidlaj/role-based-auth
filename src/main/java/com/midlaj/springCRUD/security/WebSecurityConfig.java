package com.midlaj.springCRUD.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Resource
	UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {



		http.authorizeRequests()
				.antMatchers("/").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/register/**").permitAll()
				.antMatchers("/new").hasAnyAuthority("ADMIN")
				.antMatchers("/edit/**").hasAnyAuthority("ADMIN")
				.antMatchers("/delete/**").hasAuthority("ADMIN")
				.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/").permitAll()
				.and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/404");

		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
}
