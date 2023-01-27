package com.example.recyclerview.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.activity.model.Filme;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MysViewHolder> {

    private List<Filme> listaFilmes;
    public Adapter(List<Filme> lista) {
        listaFilmes = lista;
    }

    //Esse primeiro metodo serve para criar as visualizações
    @NonNull
    @Override
    public MysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Converter o XML em um objeto do tipo VIEW "XLM é o adapter_lista"
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista,parent,false);
        //Passando a a view para a classe interna MyViewHold que recebe uma view como parametro
        //O myviewHolder é quem vai configurar os dados da visualização
        return new MysViewHolder(itemLista);


    }

    //Exibe os items da lista
    @Override
    public void onBindViewHolder(@NonNull MysViewHolder holder, int position) {

        //Recuperar item na lista pela posição
        Filme filme = listaFilmes.get(position);

        holder.titulo.setText(filme.getTitulo());
        holder.genero.setText(filme.getGenero());
        holder.ano.setText(filme.getAno());
    }

    //Retorna a quantidade de items que serão exibidos
    @Override
    public int getItemCount() {
        return listaFilmes.size();
    }

    //Inner Class
    //Essa classe irá armazenar os dados de cada elemento da lista
    public class MysViewHolder extends  RecyclerView.ViewHolder{

        TextView titulo;
        TextView genero;
        TextView ano;

        public MysViewHolder(@NonNull View itemView) {
            super(itemView);
            //Recuperar os componentes do XML convertido


             titulo = itemView.findViewById(R.id.textTitulo);
             genero = itemView.findViewById(R.id.textGenero);;
             ano = itemView.findViewById(R.id.textAno);;
        }
    }
}
