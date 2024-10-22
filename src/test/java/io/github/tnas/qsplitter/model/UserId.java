package io.github.tnas.qsplitter.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ID")
public class UserId {
	
	@Id
	private Long id;

	public UserId() { }
	
	public UserId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
