package com.example.whatsapp.modal;

import android.util.Base64;

import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

// Serializable interface que permite passar dados de uma activity para outra
public class Grupo implements Serializable {

    private String id;
    private  String nome;
    private String foto;
    private List<Usuario> membros;


    public Grupo() {

        //recuperar id do grupo ao ser instanciado
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference grupoFirebase = database.child("grupos");

        //Recuperando id unico gerado pelo firebase
        String idGrupoFirebase = grupoFirebase.push().getKey();
        setId(idGrupoFirebase);
    }


    public void salvar(){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference grupoFirebase = database.child("grupos");

        grupoFirebase.child(getId()).setValue(this);


        //Salvar conversa para membros do gruupo
        for(Usuario membro : getMembros()){

            String idRemetente = Base64Custom.codificarBase64(membro.getEmail());
            String idDestinatario = getId();


            Conversas conversa = new Conversas();
            conversa.setIdRemetente(idRemetente);
            conversa.setIdDestinatario(idDestinatario);
            conversa.setUltimaMensagem("");
            conversa.setIsGroup("true");
            conversa.setGrupo(this);

            conversa.salvar();
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}
