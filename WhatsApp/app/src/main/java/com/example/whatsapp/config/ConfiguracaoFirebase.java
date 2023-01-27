package com.example.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference dataBase;
    private static StorageReference storage;


    //retornar a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if(dataBase == null){
            dataBase = FirebaseDatabase.getInstance().getReference();
        }
        return dataBase;
    }


    //retorna instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    //retorna instancia do FirebaseStorage
    public static StorageReference getFirebaseStorage(){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

}
