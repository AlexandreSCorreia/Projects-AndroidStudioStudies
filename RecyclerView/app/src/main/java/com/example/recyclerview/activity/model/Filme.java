package com.example.recyclerview.activity.model;

public class Filme {

    private String Titulo;
    private String Genero;
    private String Ano;


    public Filme() {

    }

    public Filme(String titulo, String genero, String ano) {
        Titulo = titulo;
        Genero = genero;
        Ano = ano;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    public String getAno() {
        return Ano;
    }

    public void setAno(String ano) {
        Ano = ano;
    }
}
