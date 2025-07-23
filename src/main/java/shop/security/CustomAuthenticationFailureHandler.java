package shop.security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import shop.exceptions.security.AccountNotConfirmedException;
import shop.services.registration.RegistrationService;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{

	private final RegistrationService registrationService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if(exception instanceof AccountNotConfirmedException) {
			registrationService.resendConfirmationToken(request.getParameter("username"));
		    response.sendRedirect("/registration/resend-confirmation");
		}else if(exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException){
			response.sendRedirect("/login?error=bad_credentials");
		}else{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized.");
		}
	}

}
