package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CotacaoHistorica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double preco;
    private double variacaoPercentual;
    private double precoMinimo;
    private double precoMaximo;
    private LocalDateTime consultadoEm;

    @ManyToOne
    @JoinColumn(name = "ativo_id")
    @JsonIgnore
    private AtivoFinanceiro ativo;

    public CotacaoHistorica() {}

    // Getters e setters.
    public Long getId() { 
        return id; 
        }

    public void setId(Long id) { 
        this.id = id; 
        }

    public double getPreco() { 
        return preco; 
        }

    public void setPreco(double preco) { 
        this.preco = preco; 
        }

    public double getVariacaoPercentual() { 
        return variacaoPercentual; 
        }

    public void setVariacaoPercentual(double variacaoPercentual) { 
        this.variacaoPercentual = variacaoPercentual; 
        }

    public double getPrecoMinimo() { 
        return precoMinimo; 
        }

    public void setPrecoMinimo(double precoMinimo) { 
        this.precoMinimo = precoMinimo; 
        }

    public double getPrecoMaximo() { 
        return precoMaximo; 
        }

    public void setPrecoMaximo(double precoMaximo) { 
        this.precoMaximo = precoMaximo; 
        }

    public LocalDateTime getConsultadoEm() { 
        return consultadoEm; 
        }

    public void setConsultadoEm(LocalDateTime consultadoEm) { 
        this.consultadoEm = consultadoEm; 
        }

    public AtivoFinanceiro getAtivo() { 
        return ativo; 
        }

    public void setAtivo(AtivoFinanceiro ativo) { 
        this.ativo = ativo; 
        }
}
