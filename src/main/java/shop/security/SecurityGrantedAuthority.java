package shop.security;

import org.springframework.security.core.GrantedAuthority;

import lombok.RequiredArgsConstructor;
import shop.persistence.entities.Authority;

@RequiredArgsConstructor
public class SecurityGrantedAuthority implements GrantedAuthority{
	
	private final Authority authority;
	
	@Override
	public String getAuthority() {
		return authority.getPk().getAuthority();
	}
	
}
