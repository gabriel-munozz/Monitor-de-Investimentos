package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private String tickerAtivo;
    private double quantidade;
    private double precoUnitario;
    private double valorTotal;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "carteira_id")
    @JsonIgnore
    private Carteira carteira;

    public Transacao() {}

    // Getters e setters.
    public Long getId() { 
        return id; 
        }

    public void setId(Long id) { 
        this.id = id; 
        }

    public String getTipo() { 
        return tipo; 
        }

    public void setTipo(String tipo) { 
        this.tipo = tipo; 
        }

    public String getTickerAtivo() { 
        return tickerAtivo; 
        }

    public void setTickerAtivo(String tickerAtivo) { 
        this.tickerAtivo = tickerAtivo; 
        }

    public double getQuantidade() { 
        return quantidade; 
        }

    public void setQuantidade(double quantidade) { 
        this.quantidade = quantidade; 
        }

    public double getPrecoUnitario() { 
        return precoUnitario; 
        }

    public void setPrecoUnitario(double precoUnitario) { 
        this.precoUnitario = precoUnitario; 
        }

    public double getValorTotal() { 
        return valorTotal; 
        }

    public void setValorTotal(double valorTotal) { 
        this.valorTotal = valorTotal; 
        }

    public String getObservacao() { 
        return observacao; 
        }

    public void setObservacao(String observacao) { 
        this.observacao = observacao; 
        }

    public Carteira getCarteira() { 
        return carteira; 
        }

    public void setCarteira(Carteira carteira) { 
        this.carteira = carteira; 
        }
}
