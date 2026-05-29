package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AtivoFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ticker;
    private String nome;
    private String tipo;
    private String setor;

    @OneToMany(mappedBy = "ativo", cascade = CascadeType.ALL)
    private List<CotacaoHistorica> cotacoes = new ArrayList<>();

    @ManyToMany(mappedBy = "ativos")
    @JsonIgnore
    private List<Carteira> carteiras = new ArrayList<>();

    public AtivoFinanceiro() {}

    public AtivoFinanceiro(String ticker, String nome, String tipo, String setor) {
        this.ticker = ticker;
        this.nome = nome;
        this.tipo = tipo;
        this.setor = setor;
    }

    // Getters e setters.
    public Long getId() { 
        return id; 
        }

    public void setId(Long id) { 
        this.id = id; 
        }

    public String getTicker() { 
        return ticker; 
        }

    public void setTicker(String ticker) { 
        this.ticker = ticker; 
        }

    public String getNome() { 
        return nome; 
        }

    public void setNome(String nome) { 
        this.nome = nome; 
        }

    public String getTipo() { 
        return tipo; 
        }

    public void setTipo(String tipo) { 
        this.tipo = tipo; 
        }

    public String getSetor() { 
        return setor; 
        }

    public void setSetor(String setor) { 
        this.setor = setor; 
        }

    public List<CotacaoHistorica> getCotacoes() { 
        return cotacoes; 
        }

    public void setCotacoes(List<CotacaoHistorica> cotacoes) { 
        this.cotacoes = cotacoes; 
        }

    public List<Carteira> getCarteiras() { 
        return carteiras; 
        }

    public void setCarteiras(List<Carteira> carteiras) { 
        this.carteiras = carteiras; 
        }
}
