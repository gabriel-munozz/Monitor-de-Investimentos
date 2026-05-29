package com.example.api.controller;

import com.example.api.client.HgBrasilClient;
import com.example.api.entity.AtivoFinanceiro;
import com.example.api.entity.Carteira;
import com.example.api.entity.CotacaoHistorica;
import com.example.api.entity.Investidor;
import com.example.api.repository.AtivoFinanceiroRepository;
import com.example.api.repository.CarteiraRepository;
import com.example.api.repository.CotacaoHistoricaRepository;
import com.example.api.repository.InvestidorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Gerencia carteiras de investimento.
@RestController
@RequestMapping("/carteiras")
@Tag(name = "Carteiras", description = "Gerenciamento de carteiras e relatório com cotações ao vivo")
public class CarteiraController {

    private final CarteiraRepository carteiraRepository;
    private final InvestidorRepository investidorRepository;
    private final AtivoFinanceiroRepository ativoRepository;
    private final CotacaoHistoricaRepository cotacaoRepository;
    private final HgBrasilClient hgBrasilClient;

    public CarteiraController(CarteiraRepository carteiraRepository, InvestidorRepository investidorRepository,
    AtivoFinanceiroRepository ativoRepository, CotacaoHistoricaRepository cotacaoRepository,
    HgBrasilClient hgBrasilClient) {
        this.carteiraRepository = carteiraRepository;
        this.investidorRepository = investidorRepository;
        this.ativoRepository = ativoRepository;
        this.cotacaoRepository = cotacaoRepository;
        this.hgBrasilClient = hgBrasilClient;
    }

    //READ - Lista todas as carteiras.
    @GetMapping
    @Operation(summary = "Lista todas as carteiras")
    public List<Carteira> listar() {
        return carteiraRepository.findAll();
    }

    
    //READ - Busca carteira por ID.
    @GetMapping("/{id}")
    @Operation(summary = "Busca uma carteira pelo ID")
    public ResponseEntity<Carteira> buscar(@PathVariable Long id) {
        Carteira carteira = carteiraRepository.findById(id).orElse(null);

        if (carteira == null) {
            return ResponseEntity.notFound().build(); 
        }

        return ResponseEntity.ok(carteira); 
    }

    //READ - Lista carteiras de um investidor.
    @GetMapping("/investidor/{investidorId}")
    @Operation(summary = "Lista todas as carteiras de um investidor")
    public List<Carteira> listarPorInvestidor(@PathVariable Long investidorId) {
        return carteiraRepository.findByInvestidorId(investidorId);
    }

    
    //CREATE - Cria uma carteira para um investidor.
    @PostMapping
    @Operation(summary = "Cria uma nova carteira vinculada a um investidor")
    public ResponseEntity<Carteira> adicionar(@RequestBody Carteira carteira) {
        Investidor investidor = investidorRepository.findById(carteira.getInvestidor().getId()).orElse(null);

        if (investidor == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        carteira.setInvestidor(investidor);
        return ResponseEntity.status(201).body(carteiraRepository.save(carteira)); // 201
    }

    
    //UPDATE - Atualiza nome e descrição da carteira.
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o nome e descrição de uma carteira")
    public ResponseEntity<Carteira> atualizar(@PathVariable Long id, @RequestBody Carteira dados) {

        Carteira carteira = carteiraRepository.findById(id).orElse(null);

        if (carteira == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        carteira.setNome(dados.getNome());
        carteira.setDescricao(dados.getDescricao());

        return ResponseEntity.ok(carteiraRepository.save(carteira)); // 200
    }

    
    //DELETE - Remove uma carteira.
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma carteira")
    public ResponseEntity<String> remover(@PathVariable Long id) {
        if (!carteiraRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        carteiraRepository.deleteById(id);
        return ResponseEntity.ok("Carteira removida com sucesso!"); // 200
    }

    @PostMapping("/{id}/ativos/{ativoId}")
    @Operation(summary = "Adiciona um ativo à carteira (relacionamento N:N)")
    public ResponseEntity<String> adicionarAtivo(@PathVariable Long id, @PathVariable Long ativoId) {

        Carteira carteira = carteiraRepository.findById(id).orElse(null);
        AtivoFinanceiro ativo = ativoRepository.findById(ativoId).orElse(null);

        if (carteira == null || ativo == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        carteira.getAtivos().add(ativo);
        carteiraRepository.save(carteira);

        return ResponseEntity.ok("Ativo " + ativo.getTicker() + " adicionado à carteira!"); // 200
    }

    //Remove um ativo da carteira.
    @DeleteMapping("/{id}/ativos/{ativoId}")
    @Operation(summary = "Remove um ativo da carteira")
    public ResponseEntity<String> removerAtivo(@PathVariable Long id, @PathVariable Long ativoId) {

        Carteira carteira = carteiraRepository.findById(id).orElse(null);
        AtivoFinanceiro ativo = ativoRepository.findById(ativoId).orElse(null);

        if (carteira == null || ativo == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        carteira.getAtivos().remove(ativo);
        carteiraRepository.save(carteira);

        return ResponseEntity.ok("Ativo " + ativo.getTicker() + " removido da carteira!"); // 200
    }

    @GetMapping("/{id}/relatorio")
    @Operation(summary = "Relatório da carteira com cotações em tempo real (API HG Brasil) e fallback local")
    public ResponseEntity<Map<String, Object>> relatorio(@PathVariable Long id) {

        Carteira carteira = carteiraRepository.findById(id).orElse(null);

        if (carteira == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("carteiraId", carteira.getId());
        relatorio.put("carteiraNome", carteira.getNome());

        List<Map<String, Object>> ativos = new ArrayList<>();

        for (AtivoFinanceiro ativo : carteira.getAtivos()) {

            Map<String, Object> dadosAtivo = new HashMap<>();
            dadosAtivo.put("ticker", ativo.getTicker());
            dadosAtivo.put("nome", ativo.getNome());
            dadosAtivo.put("tipo", ativo.getTipo());

            CotacaoHistorica cotacaoAPI = hgBrasilClient.buscarCotacao(ativo.getTicker());

            if (cotacaoAPI != null) {
                // Salva a cotação no banco para consulta offline posterior.
                cotacaoAPI.setAtivo(ativo);
                cotacaoRepository.save(cotacaoAPI);

                dadosAtivo.put("precoAtual", cotacaoAPI.getPreco());
                dadosAtivo.put("variacaoPercentual", cotacaoAPI.getVariacaoPercentual());
                dadosAtivo.put("precoMinimo", cotacaoAPI.getPrecoMinimo());
                dadosAtivo.put("precoMaximo", cotacaoAPI.getPrecoMaximo());
                dadosAtivo.put("fonte", "API_TEMPO_REAL");
            } else {
                //Usa a última cotação salva localmente se a API falhar.
                List<CotacaoHistorica> historico = cotacaoRepository.findByAtivoId(ativo.getId());

                if (!historico.isEmpty()) {
                    CotacaoHistorica ultima = historico.get(historico.size() - 1);
                    dadosAtivo.put("precoAtual", ultima.getPreco());
                    dadosAtivo.put("consultadoEm", ultima.getConsultadoEm());
                    dadosAtivo.put("fonte", "CACHE_LOCAL");
                } else {
                    dadosAtivo.put("precoAtual", "Sem cotação disponível");
                    dadosAtivo.put("fonte", "SEM_DADOS");
                }
            }

            ativos.add(dadosAtivo);
        }

        relatorio.put("ativos", ativos);
        return ResponseEntity.ok(relatorio);
}
