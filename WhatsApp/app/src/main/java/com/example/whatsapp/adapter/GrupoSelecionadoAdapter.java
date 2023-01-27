package com.example.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.modal.Usuario;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrupoSelecionadoAdapter extends RecyclerView.Adapter<GrupoSelecionadoAdapter.MysViewHolder> {

    private List<Usuario> contatosSelecionados;
    private Context context;


    public GrupoSelecionadoAdapter(List<Usuario> lista, Context c) {
        this.contatosSelecionados = lista;
        this.context = c;

    }

    @NonNull
    @Override
    public MysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_grupo_selecionado,parent,false);

        return new GrupoSelecionadoAdapter.MysViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MysViewHolder holder, int position) {


        Usuario usuario = contatosSelecionados.get(position);
        holder.nome.setText(usuario.getNome());

        if(usuario.getFoto() != null){
            Uri url = Uri.parse(usuario.getFoto());
            Glide.with(context)
                    .load(url)
                    .into(holder.foto);
        }else{

            holder.foto.setImageResource(R.drawable.padrao);

        }
    }

    @Override
    public int getItemCount() {
        return contatosSelecionados.size();
    }

    //Inner Class
    //Essa classe irá armazenar os dados de cada elemento da lista
    public class MysViewHolder extends  RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome;


        public MysViewHolder(@NonNull View itemView) {
            super(itemView);
            //Recuperar os componentes do XML convertido
            foto  = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            nome = itemView.findViewById(R.id.textViewNomeMembroSelecionado);


        }
    }
}
