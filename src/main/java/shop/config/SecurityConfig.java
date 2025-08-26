package shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import shop.security.CustomAuthenticationFailureHandler;
import shop.security.CustomUsernamePasswordAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final CustomUsernamePasswordAuthenticationProvider authenticationProvider;
	private final CustomAuthenticationFailureHandler failureHandler;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.formLogin(c -> c.loginPage("/login").failureHandler(failureHandler))
				.userDetailsService(userDetailsService)
				.authenticationProvider(authenticationProvider)
				
				.authorizeHttpRequests(c -> c.requestMatchers("/admin/**").hasRole("ADMIN"))
				.authorizeHttpRequests(c -> c.requestMatchers("/book/**").permitAll())
				
				.authorizeHttpRequests(c -> c.requestMatchers("/book/{bookId:^[0-9]*$}/reviews").permitAll())
				.authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.DELETE,"/book/{bookId:^[0-9]*$}/reviews").authenticated())
				.authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.POST,"/book/{bookId:^[0-9]*$}/reviews").authenticated())
				.authorizeHttpRequests(c -> c.requestMatchers("/book/{bookId:^[0-9]*$}/reviews/admin/**").hasRole("ADMIN"))
				
				.authorizeHttpRequests(c -> c.requestMatchers("/authors/**").permitAll())

				.authorizeHttpRequests(c -> c.requestMatchers("/book/{bookId:^[0-9]*$}/reviews/**").authenticated())
				.authorizeHttpRequests(c -> c.requestMatchers("/book/{bookId:^[0-9]*$}/reviews/admin/**").hasRole("ADMIN"))
				
				.authorizeHttpRequests(c -> c.requestMatchers("/orders/**").authenticated())

				.authorizeHttpRequests(c -> c.requestMatchers("/checkout/**").authenticated())
				.authorizeHttpRequests(c -> c.requestMatchers("/cart/**").authenticated())
				.authorizeHttpRequests(c -> c.requestMatchers("/favourite/**").authenticated())
				
				.authorizeHttpRequests(c -> c.requestMatchers("/password-reset/**").anonymous())
				
				.authorizeHttpRequests(c -> c.anyRequest().permitAll()
						
				);

		http.httpBasic(Customizer.withDefaults());
		//TODO:REMOVE WHEN DEPLOYED
		http.csrf(c -> c.disable());

		return http.build();
	}

}



