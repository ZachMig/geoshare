package com.geoshare.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Table(name="list")
public class LocationList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="public")
	private boolean isPublic;
	
	@Column(name="like_count")
	private Long likeCount;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public Long getLikeCount() {
		return likeCount;
	}

	public User getUser() {
		return user;
	}
	
}
