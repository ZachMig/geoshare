package com.geoshare.backend.entity;

import java.math.BigDecimal;

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
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name="location")
public class Location {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="url", length=255)
	private String url;
	
    @Column(name="lat", precision = 9, scale = 7)
    private BigDecimal lat;
	
    @Column(name="lng", precision = 10, scale = 8)
    private BigDecimal lng;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="country_id")
	private Country country;
    
	@ManyToOne
	@JoinColumn(name="user_id")
	private GeoshareUser user;

	public Long getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public String getDescription() {
		return description;
	}

	public Country getCountry() {
		return country;
	}

	public GeoshareUser getUser() {
		return user;
	}
	
}
