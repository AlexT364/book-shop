package shop.persistence.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.persistence.entities.embeddables.AuthorityPK;

@Entity
@Table(name="authorities")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Authority{
	
	@EmbeddedId
	private AuthorityPK pk;
	
}
