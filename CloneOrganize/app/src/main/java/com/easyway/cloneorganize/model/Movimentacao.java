package com.easyway.cloneorganize.model;

import com.easyway.cloneorganize.config.ConfiguracaoFirebase;
import com.easyway.cloneorganize.helper.Base64Custom;
import com.easyway.cloneorganize.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {
    private String data;
    private String categoria;
    private String descricao;
    private Double valor;
    private String tipo;
    private String idMovimentacao;


    public Movimentacao() {
    }



    public void salvar(String dataEscolhida){
        //Capturar o id do usuario
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida);
        //Salvar dados no banco
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()//Firebase cria um id unico automaticamente
                .setValue(this);//Aqui estou passando o objeto usuario
    }


    public String getIdMovimentacao() { return idMovimentacao; }

    public void setIdMovimentacao(String idMovimentacao) { this.idMovimentacao = idMovimentacao; }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
