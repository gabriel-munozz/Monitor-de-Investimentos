package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "investidor_id")
    @JsonBackReference
    private Investidor investidor;

    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL)
    private List<Transacao> transacoes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "carteira_ativo",
        joinColumns = @JoinColumn(name = "carteira_id"),
        inverseJoinColumns = @JoinColumn(name = "ativo_id")
    )
    @JsonIgnore
    private List<AtivoFinanceiro> ativos = new ArrayList<>();

    public Carteira() {}

    public Carteira(String nome, String descricao, Investidor investidor) {
        this.nome = nome;
        this.descricao = descricao;
        this.investidor = investidor;
    }

    // Getters e setters.
    public Long getId() { 
        return id;
        }

    public void setId(Long id) { 
        this.id = id; 
        }

    public String getNome() { 
        return nome; 
        }

    public void setNome(String nome) { 
        this.nome = nome; 
        }

    public String getDescricao() { 
        return descricao; 
        }

    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
        }

    public Investidor getInvestidor() { 
        return investidor; 
        }

    public void setInvestidor(Investidor investidor) { 
        this.investidor = investidor; 
        }

    public List<Transacao> getTransacoes() { 
        return transacoes; 
        }

    public void setTransacoes(List<Transacao> transacoes) { 
        this.transacoes = transacoes; 
        }

    public List<AtivoFinanceiro> getAtivos() { 
        return ativos; 
        }

    public void setAtivos(List<AtivoFinanceiro> ativos) { 
        this.ativos = ativos; 
        }
}
