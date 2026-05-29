package com.example.api.repository;

import com.example.api.entity.CotacaoHistorica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CotacaoHistoricaRepository extends JpaRepository<CotacaoHistorica, Long> {

    // Busca todas as cotações de um ativo pelo ID dele.
    List<CotacaoHistorica> findByAtivoId(Long ativoId);
}
