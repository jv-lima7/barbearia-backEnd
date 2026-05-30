package com.barbearia.application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "barbeiros")
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    public Long getId() {
        return id; }

    public String getNome() {
        return nome; }
    public void setNome(String nome) {
        this.nome = nome; }
}