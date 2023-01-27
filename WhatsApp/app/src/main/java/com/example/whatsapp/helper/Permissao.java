package com.example.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissao(String[] permissoes, Activity activity, int requestCode){

        //SDK 23 é a api do MARSHMALLOW pois é a partir dessa api que tem que solicitar permissão
        if(Build.VERSION.SDK_INT >=23){

            List<String> listaPermissoes = new ArrayList<>();
            /*
            * Percorre as permissões passadas, verificando uma a uma
            * se ja não tem permissao liberada
            * */
            for (String permissao : permissoes){
              Boolean temPermissao = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;
                if(!temPermissao) listaPermissoes.add(permissao);
            }


            /*Caso a lista esteja vazia, não é necessario solicitar permissão*/
            if(listaPermissoes.isEmpty()){
                return true;
            }
            //Converter a lista de permissoes para um array
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //Solicitar permissao
            ActivityCompat.requestPermissions(activity,novasPermissoes,requestCode);
        }

        return true;
    }
}
