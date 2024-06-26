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
@Table(name="comment_list")
public class ListComment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Long id;
	
	@Column(name="content")
	private String content;
	
	@ManyToOne
	@JoinColumn(name="list_id")
	private LocationList locationList;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private GeoshareUser user;

	public Long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public LocationList getLocationList() {
		return locationList;
	}

	public GeoshareUser getUser() {
		return user;
	}
	
}




