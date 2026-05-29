package com.example.api.repository;

import com.example.api.entity.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {

    // Busca todas as carteiras de um investidor pelo ID dele.
    List<Carteira> findByInvestidorId(Long investidorId);
}
