package shop.exceptions.security;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;

public class AccountNotConfirmedException extends DisabledException{

	public AccountNotConfirmedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountNotConfirmedException(String message) {
		super(message);
	}

}
