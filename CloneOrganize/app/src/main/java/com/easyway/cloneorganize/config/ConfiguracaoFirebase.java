package com.easyway.cloneorganize.config;

import com.easyway.cloneorganize.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebaseDatabase;




    //Metodo que retorna a instancia do firebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){

        //Verificar se esse objeto já não existe ouseja se ja foi configurado
        if(firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        }

        return firebaseDatabase;

    }

    //Metodo que retorna a instancia do firebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){

        //Se em algum momento no codigo já for configurada esse objeto estatico
        //Farei uma verificação para não alterar novamente
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;

    }

   /* public DatabaseReference recuperarRefenciaUsuario(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseDatabase.child("usuarios").child(idUsuario);
        return usuarioRef;
    }*/
}
