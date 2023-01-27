package com.easyway.cloneorganize.helper;

import java.text.SimpleDateFormat;

public class DateCustom {


    public static String dataAtual(){
       long date = System.currentTimeMillis();
       //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString  = simpleDateFormat.format(date);
        return dataString;
    }

    public static String mesAnoDataEscolhida(String data){
       String dataRetorno[] =  data.split("/");
       //variavel data chegou assim 21/09/2019
        //O metodo split quebra uma string com base num caractere
        //vai separar essa data e colocar cada parte em um indice de um array
       String dia = dataRetorno[0];//dia 21
       String mes = dataRetorno[1];//mes 09
       String Ano = dataRetorno[2];//ano 2019

       String mesAno = mes + Ano;

       return mesAno;
    }
}
