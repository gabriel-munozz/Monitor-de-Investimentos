package com.example.api.repository;

import com.example.api.entity.Investidor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestidorRepository extends JpaRepository<Investidor, Long> {
}
