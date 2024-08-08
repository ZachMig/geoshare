package com.geoshare.backend.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.geoshare.backend.parents.Ownable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="location")
public class Location implements Ownable {
	
	public Location(String url, BigDecimal lat, BigDecimal lng, String description, Country country,
			GeoshareUser user, Meta meta) {
		this.url = url;
		this.lat = lat;
		this.lng = lng;
		this.description = description;
		this.country = country;
		this.user = user;
		this.lists = new HashSet<>();
		this.listed = Long.valueOf(0);
		this.meta = meta;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	
	@ManyToMany(mappedBy="locations", fetch = FetchType.LAZY)
	private Set<LocationList> lists;
	
	@Column(name="listed")
	private Long listed;
	
	@ManyToOne
	@JoinColumn(name="meta_id")
	private Meta meta;
	
}
