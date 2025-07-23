package shop.persistence.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Setter
@Getter
@NoArgsConstructor
public class User{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long id;
	@Column(unique = true, nullable = false)
	private String username;
	private String password;
	private boolean enabled;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Authority> authorities = new ArrayList<Authority>();
	
	@ManyToMany
	@JoinTable(
			name = "favourite",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="book_id"))
	private List<Book> favourite;

	private String email;
	
	@Column(unique = true)
	private String confirmationToken;
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime tokenCreationDate;
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", enabled=" + enabled
				+ ", authorities=" + authorities + ", favourite=" + favourite + ", email=" + email + "]";
	}


	
}
