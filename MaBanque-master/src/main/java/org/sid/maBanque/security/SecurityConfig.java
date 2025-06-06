package org.sid.maBanque.security;

import javax.sql.DataSource;

import org.sid.maBanque.web.CustomSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CustomSuccessHandler customSuccessHandler; 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder bcpe = getBCPE();
		System.out.println(bcpe.encode("1234"));

		auth.inMemoryAuthentication()
				.withUser("admin").password(bcpe.encode("1234")).roles("ADMIN", "USER")
				.and()
				.withUser("user").password(bcpe.encode("1234")).roles("USER")
				.and()
				.passwordEncoder(getBCPE());


	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/user/**").hasRole("USER")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.successHandler(customSuccessHandler) 
				.permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/403")
				.and()
				.logout().permitAll();
	}

	@Bean
	BCryptPasswordEncoder getBCPE() {
		return new BCryptPasswordEncoder();
	}
}
