package com.vin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vin.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
