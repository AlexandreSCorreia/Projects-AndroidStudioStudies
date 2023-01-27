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
import com.example.whatsapp.modal.Conversas;
import com.example.whatsapp.modal.Grupo;
import com.example.whatsapp.modal.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MysViewHolder> {

    private List<Conversas> listaConversas;
    private Context context;

    public ConversasAdapter(List<Conversas> lista, Context c) {
        this.listaConversas = lista;
        this.context = c;
    }

    public List<Conversas> getConversas(){
        return this.listaConversas;
    }

    @NonNull
    @Override
    public MysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos,parent,false);

        return new MysViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MysViewHolder holder, int position) {
         Conversas conversa = listaConversas.get(position);
         holder.ultimaMensagem.setText(conversa.getUltimaMensagem());

         if(conversa.getIsGroup().equals("true")){

             //Recuperar dados do grupo
             Grupo grupo = conversa.getGrupo();
             holder.nome.setText(grupo.getNome());

             if(grupo.getFoto() != null){
                 Uri url = Uri.parse(grupo.getFoto());
                 Glide.with(context)
                         .load(url)
                         .into(holder.foto);
             }else{
                 holder.foto.setImageResource(R.drawable.padrao);
             }


         }else{
             Usuario usuario = conversa.getUsuarioExibicao();

             if(usuario != null){
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
         }
    }

    @Override
    public int getItemCount() {
        return listaConversas.size();
    }

    //Inner Class
    //Essa classe irá armazenar os dados de cada elemento da lista
    public class MysViewHolder extends  RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome,ultimaMensagem;


        public MysViewHolder(@NonNull View itemView) {
            super(itemView);
            //Recuperar os componentes do XML convertido
            foto  = itemView.findViewById(R.id.circleImageViewFoto);
            nome = itemView.findViewById(R.id.textViewTitulo);
            ultimaMensagem  = itemView.findViewById(R.id.textViewSubTitulo);

        }
    }
}
