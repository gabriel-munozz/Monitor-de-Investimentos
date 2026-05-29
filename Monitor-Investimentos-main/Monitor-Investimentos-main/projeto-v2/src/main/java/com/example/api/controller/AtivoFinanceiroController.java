package com.example.api.controller;

import com.example.api.client.HgBrasilClient;
import com.example.api.entity.AtivoFinanceiro;
import com.example.api.entity.CotacaoHistorica;
import com.example.api.repository.AtivoFinanceiroRepository;
import com.example.api.repository.CotacaoHistoricaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Ao cadastrar um ativo, o sistema chama automaticamente a API HG Brasil
@RestController
@RequestMapping("/ativos")
@Tag(name = "Ativos Financeiros", description = "Gerenciamento de ativos e consulta de cotações via API HG Brasil")
public class AtivoFinanceiroController {

    private final AtivoFinanceiroRepository repository;
    private final CotacaoHistoricaRepository cotacaoRepository;
    private final HgBrasilClient hgBrasilClient;

    public AtivoFinanceiroController(AtivoFinanceiroRepository repository, CotacaoHistoricaRepository cotacaoRepository,
    HgBrasilClient hgBrasilClient) {
        this.repository = repository;
        this.cotacaoRepository = cotacaoRepository;
        this.hgBrasilClient = hgBrasilClient;
    }

    //READ - Lista todos os ativos cadastrados.
    @GetMapping
    @Operation(summary = "Lista todos os ativos financeiros cadastrados")
    public List<AtivoFinanceiro> listar() {
        return repository.findAll();
    }

    //READ - Busca ativo por ID.
    @GetMapping("/{id}")
    @Operation(summary = "Busca um ativo financeiro pelo ID")
    public ResponseEntity<AtivoFinanceiro> buscar(@PathVariable Long id) {
        AtivoFinanceiro ativo = repository.findById(id).orElse(null);

        if (ativo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ativo);
    }

    //CREATE - Cadastra ativo e busca cotação inicial na API HG Brasil.
    @PostMapping
    @Operation(summary = "Cadastra um ativo e busca sua cotação inicial na API HG Brasil")
    public ResponseEntity<AtivoFinanceiro> adicionar(@RequestBody AtivoFinanceiro ativo) {

        ativo.setTicker(ativo.getTicker().toUpperCase());
        AtivoFinanceiro salvo = repository.save(ativo);

        // Chama a API HG Brasil. O client já trata todas as exceções de rede.
        CotacaoHistorica cotacao = hgBrasilClient.buscarCotacao(salvo.getTicker());

        if (cotacao != null) {
            // Vincula a cotação ao ativo e persiste no banco para consulta offline.
            cotacao.setAtivo(salvo);
            cotacaoRepository.save(cotacao);
        }

        return ResponseEntity.status(201).body(salvo);
    }

    //Update
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um ativo financeiro")
    public ResponseEntity<AtivoFinanceiro> atualizar(@PathVariable Long id,
                                                      @RequestBody AtivoFinanceiro dados) {

        AtivoFinanceiro ativo = repository.findById(id).orElse(null);

        if (ativo == null) {
            return ResponseEntity.notFound().build();
        }

        ativo.setNome(dados.getNome());
        ativo.setTipo(dados.getTipo());
        ativo.setSetor(dados.getSetor());

        return ResponseEntity.ok(repository.save(ativo));
    }

    //Delete
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um ativo financeiro")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.ok("Ativo removido com sucesso!"); // 200
    }

    //READ - Lista o histórico de cotações salvas de um ativo.
    @GetMapping("/{id}/historico")
    @Operation(summary = "Lista o histórico de cotações salvas do ativo (dados persistidos da API)")
    public ResponseEntity<List<CotacaoHistorica>> historico(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        return ResponseEntity.ok(cotacaoRepository.findByAtivoId(id)); // 200
    }
}
