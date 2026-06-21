package com.tradeguard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tradeguard.entity.Trade;
import com.tradeguard.entity.User;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByUserOrderByCreatedAtDesc(User user);
    List<Trade> findByUserAndStatus(User user, String status);
    List<Trade> findByUserAndStatusOrderByCreatedAtDesc(User user, String status);
}