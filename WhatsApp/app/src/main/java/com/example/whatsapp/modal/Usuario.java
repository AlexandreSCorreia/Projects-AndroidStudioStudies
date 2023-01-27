package com.example.whatsapp.modal;

import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String identificador_Usuario;
    private String nome;
    private String email;
    private String senha;
    private String foto;




    public Usuario() {
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseRef.child("usuarios")
                .child(identificador_Usuario)
                .setValue(this);//Aqui estou passando o objeto usuario
    }

    public void atualizar(){

        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();

        DatabaseReference firebaseRef = database.child("usuarios")
                .child(identificadorUsuario);

        Map<String,Object> valoresUsuario = converteraraMap();
        firebaseRef.updateChildren(valoresUsuario);
    }
    @Exclude
    public Map<String,Object> converteraraMap(){
        HashMap<String,Object> usuarioMap = new HashMap<>();

        usuarioMap.put("email",getEmail());
        usuarioMap.put("nome",getNome());
        usuarioMap.put("foto",getFoto());
        return usuarioMap;
    }

    @Exclude
    public String getIdentificador_Usuario() {
        return identificador_Usuario;
    }

    public void setIdentificador_Usuario(String identificador_Usuario) {
        this.identificador_Usuario = identificador_Usuario;
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
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
