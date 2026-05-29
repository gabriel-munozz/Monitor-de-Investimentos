package com.example.api.repository;

import com.example.api.entity.AtivoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtivoFinanceiroRepository extends JpaRepository<AtivoFinanceiro, Long> {
}
