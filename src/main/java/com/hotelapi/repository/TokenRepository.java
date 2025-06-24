package com.hotelapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelapi.entity.Token;


@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{

}
