package com.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bill_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

	public BillItem() {
		
		// TODO Auto-generated constructor stub
	}

	public BillItem(Long id, int quantity, Bill bill, Product product) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.bill = bill;
		this.product = product;
	}
	
	
    
    
}
