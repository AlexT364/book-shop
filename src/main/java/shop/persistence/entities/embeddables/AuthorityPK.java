package shop.persistence.entities.embeddables;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.persistence.entities.User;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityPK implements Serializable{
	
	 @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
	    private User user;

	    @Column(name = "authority", nullable = false)
	    private String authority;

		@Override
		public int hashCode() {
			return Objects.hash(authority, user != null ? user.getId() : null);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			AuthorityPK other = (AuthorityPK) obj;
			return Objects.equals(authority, other.authority) && Objects.equals(user != null ? user.getId() : null,
					other.user != null ? other.user.getId() : null);
		}
	
	
	
}
