package com.hotelapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelapi.entity.BillItem;


@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {

}
