package com.rinha2023.rinha2023.Entity;


import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PessoaEntity {
    private UUID id;
    @NotNull
    @Size(max = 100, message = "Nome precisa de 100 caracteres")
    private String nome;
    @NotNull
    @Size(max = 32, message = "apelido precisa der 32 caracteres")
    @UniqueElements(message = "apelido é unico")
    private String apelido;
    @NotNull
    private Date nascimento;
    @Size(max = 32, message = "Skill cannot be longer than 32 characters")
    private List<@Size(max = 32) String> stack;

    public PessoaEntity(UUID id, String nome, String apelido, Date nascimento, List<String> stack) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.nascimento = nascimento;
        this.stack = stack;
    }

    public PessoaEntity(UUID id, String nome, String apelido, Date nascimento) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.nascimento = nascimento;
    }

    public PessoaEntity() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date data) {
        this.nascimento = data;
    }

    public List<String> getStack() {
        return stack;
    }

    public void setStack(List<String> stack) {
        this.stack = stack;
    }
}
