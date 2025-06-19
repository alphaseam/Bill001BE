package com.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "bills")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Bill {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @ManyToOne
//    @JoinColumn(name = "hotel_id")
//    private Hotel hotel;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//	private ArrayList items;
//	
//	
//
//
//	public Bill() {
//		
//		// TODO Auto-generated constructor stub
//	}
//
//	public Bill(Long id, LocalDateTime createdAt, Hotel hotel, User user) {
//		super();
//		this.id = id;
//		this.createdAt = createdAt;
//		this.hotel = hotel;
//		this.user = user;
//		
//		this.items = new ArrayList<>();
//	}
//    
//}








@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bill {
    
	
	public Bill(Object object, LocalDateTime of, Hotel h1, User u1) {
		// TODO Auto-generated constructor stub
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<BillItem> items;
}
