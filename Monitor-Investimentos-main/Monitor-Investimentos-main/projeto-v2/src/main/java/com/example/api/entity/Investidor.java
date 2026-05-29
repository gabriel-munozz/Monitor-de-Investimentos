package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Investidor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String cpf;

    @OneToMany(mappedBy = "investidor", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Carteira> carteiras = new ArrayList<>();

    public Investidor() {}

    public Investidor(String nome, String email, String cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }

    // Getters e setters
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

    public String getEmail() { 
        return email; 
        }

    public void setEmail(String email) { 
        this.email = email; 
        }

    public String getCpf() { 
        return cpf; 
        }
    public void setCpf(String cpf) { 
        this.cpf = cpf; 
        }

    public List<Carteira> getCarteiras() { 
        return carteiras; 
        }

    public void setCarteiras(List<Carteira> carteiras) { 
        this.carteiras = carteiras; 
        }
}
