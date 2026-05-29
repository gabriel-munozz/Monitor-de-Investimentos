package com.example.api.controller;

import com.example.api.entity.Investidor;
import com.example.api.repository.InvestidorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investidores")
@Tag(name = "Investidores", description = "Operações de cadastro e consulta de investidores")
public class InvestidorController {

    private final InvestidorRepository repository;

    public InvestidorController(InvestidorRepository repository) {
        this.repository = repository;
    }

    //READ - Lista todos os investidores.
    @GetMapping
    @Operation(summary = "Lista todos os investidores")
    public List<Investidor> listar() {
        return repository.findAll();
    }

    //READ - Busca um investidor por ID.
    @GetMapping("/{id}")
    @Operation(summary = "Busca um investidor pelo ID")
    public ResponseEntity<Investidor> buscar(@PathVariable Long id) {
        Investidor investidor = repository.findById(id).orElse(null);

        if (investidor == null) {
            return ResponseEntity.notFound().build(); 
        }

        return ResponseEntity.ok(investidor); 
    }

    
    //CREATE - Cadastra um novo investidor.
    @PostMapping
    @Operation(summary = "Cadastra um novo investidor")
    public ResponseEntity<Investidor> adicionar(@RequestBody Investidor investidor) {
        Investidor salvo = repository.save(investidor);
        return ResponseEntity.status(201).body(salvo);
    }

    
    //UPDATE- Atualiza dados de um investidor.
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um investidor")
    public ResponseEntity<Investidor> atualizar(@PathVariable Long id, @RequestBody Investidor dados) {

        Investidor investidor = repository.findById(id).orElse(null);

        if (investidor == null) {
            return ResponseEntity.notFound().build(); 
        }

        investidor.setNome(dados.getNome());
        investidor.setEmail(dados.getEmail());
        investidor.setCpf(dados.getCpf());

        return ResponseEntity.ok(repository.save(investidor)); 
    }

    
    //DELETE - Remove um investidor.
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um investidor")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        repository.deleteById(id);
        return ResponseEntity.ok("Investidor removido com sucesso!"); // 200
    }
}
