package com.example.api.repository;

import com.example.api.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    // Busca todas as transações de uma carteira pelo ID dela.
    List<Transacao> findByCarteiraId(Long carteiraId);
}
