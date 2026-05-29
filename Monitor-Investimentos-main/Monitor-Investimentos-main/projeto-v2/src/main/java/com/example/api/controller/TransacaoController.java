package com.example.api.controller;

import com.example.api.entity.Carteira;
import com.example.api.entity.Transacao;
import com.example.api.repository.CarteiraRepository;
import com.example.api.repository.TransacaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Registra compras e vendas de ativos dentro de carteiras.
@RestController
@RequestMapping("/transacoes")
@Tag(name = "Transações", description = "Registro de compras e vendas de ativos")
public class TransacaoController {

    private final TransacaoRepository transacaoRepository;
    private final CarteiraRepository carteiraRepository;

    public TransacaoController(TransacaoRepository transacaoRepository, CarteiraRepository carteiraRepository) {
        this.transacaoRepository = transacaoRepository;
        this.carteiraRepository = carteiraRepository;
    }

    //READ - Lista todas as transações de uma carteira.
    @GetMapping("/carteira/{carteiraId}")
    @Operation(summary = "Lista todas as transações de uma carteira")
    public List<Transacao> listarPorCarteira(@PathVariable Long carteiraId) {
        return transacaoRepository.findByCarteiraId(carteiraId);
    }

    //READ - Busca uma transação por ID.
    @GetMapping("/{id}")
    @Operation(summary = "Busca uma transação pelo ID")
    public ResponseEntity<Transacao> buscar(@PathVariable Long id) {
        Transacao transacao = transacaoRepository.findById(id).orElse(null);

        if (transacao == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transacao);
    }

    
    //CREATE - Registra uma compra ou venda.
    @PostMapping
    @Operation(summary = "Registra uma compra ou venda de ativo em uma carteira")
    public ResponseEntity<Transacao> adicionar(@RequestBody Transacao transacao) {
        Carteira carteira = carteiraRepository
                .findById(transacao.getCarteira().getId()).orElse(null);

        if (carteira == null) {
            return ResponseEntity.notFound().build();
        }

        transacao.setCarteira(carteira);

        // Calcula o valor total automaticamente.
        transacao.setValorTotal(transacao.getQuantidade() * transacao.getPrecoUnitario());

        return ResponseEntity.status(201).body(transacaoRepository.save(transacao));
    }

    //DELETE - Remove uma transação.
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma transação")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        if (!transacaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        transacaoRepository.deleteById(id);
        return ResponseEntity.ok("Transação removida com sucesso!");
    }
}
