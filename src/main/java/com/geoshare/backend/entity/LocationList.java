package com.geoshare.backend.entity;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Table(name="list")
public class LocationList implements Ownable {
	
	public LocationList(String name, String description, GeoshareUser user) {
		this.name = name;
		this.description = description;
		this.user = user;
		this.locations = new HashSet<>();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private GeoshareUser user;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "list_and_location",
        joinColumns = @JoinColumn(name = "list_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
	private Set<Location> locations;
	
	
	
	public boolean addToLocations(Location location) {
		return locations.add(location);
	}
	
	public boolean removeFromLocations(Location location) {
		return locations.remove(location);
	}
	
}
