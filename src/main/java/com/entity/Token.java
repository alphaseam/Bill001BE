package com.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;
    private String refreshToken;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	
    
    public Token() {
		
		// TODO Auto-generated constructor stub
	}



	public Token(Long id, String accessToken, String refreshToken, boolean revoked, User user) {
		super();
		this.id = id;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.revoked = revoked;
		this.user = user;
	}
    
    
}