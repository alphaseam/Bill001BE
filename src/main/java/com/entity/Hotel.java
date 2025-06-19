package com.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
	
    public Hotel() {
		
		// TODO Auto-generated constructor stub
	}

	public Hotel(Long id, String name, String address) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
	}	
    
    
    
    
}