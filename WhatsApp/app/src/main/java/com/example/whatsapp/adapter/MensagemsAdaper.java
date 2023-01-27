package com.example.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.modal.Mensagem;

import java.util.List;


public class MensagemsAdaper extends RecyclerView.Adapter<MensagemsAdaper.MysViewHolder>{


    private List<Mensagem> listMensagem;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MensagemsAdaper(List<Mensagem> lista, Context c) {

        this.listMensagem = lista;
        this.context = c;

    }

    @NonNull
    @Override
    public MysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = null;
        if(viewType == TIPO_REMETENTE){
            itemLista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_mensagem_remetente,parent,false);

        }else if(viewType == TIPO_DESTINATARIO){
            itemLista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_mensagem_destinatario,parent,false);
        }

        return new MysViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(@NonNull MysViewHolder holder, int position) {

        //recuperar mensagem
        Mensagem mensagem = listMensagem.get(position);
        String msg = mensagem.getMensagem();
        String imagem = mensagem.getImagem();
        String nome = mensagem.getNome();
        if(imagem != null){
            Uri url = Uri.parse( imagem);
            Glide.with(context)
                    .load(url)
                    .into(holder.imagem);


            if(!nome.isEmpty()){
                //mostrar nome
                holder.nomeExibicao.setText(nome);

            }else{
                //esconder nome
                holder.nomeExibicao.setVisibility(View.GONE);
            }

            //esconder texto
            holder.mensagem.setVisibility(View.GONE);
        }else{
            holder.mensagem.setText(msg);

            if(!nome.isEmpty()){
                //mostrar nome
                holder.nomeExibicao.setText(nome);

            }else{
                //esconder nome
                holder.nomeExibicao.setVisibility(View.GONE);
            }
            //esconder imagem
            holder.imagem.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return listMensagem.size();
    }

    //Sobrescrever esse metodo ele irá retornar o tipo da visualização
    @Override
    public int getItemViewType(int position) {
        //Com isso será verigicado item a item para saber quem enviou a mensagem
        //recuperar mensagem
        Mensagem mensagem = listMensagem.get(position);
        //recuperar idUsuario
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        if(idUsuario.equals(mensagem.getIdUsuario())){
            return TIPO_REMETENTE;
        }

        return  TIPO_DESTINATARIO;
    }

    //Inner Class
    //Essa classe irá armazenar os dados de cada elemento da lista
    public class MysViewHolder extends  RecyclerView.ViewHolder{

        TextView mensagem,nomeExibicao;
        ImageView imagem;

        public MysViewHolder(@NonNull View itemView) {
            super(itemView);
            //Recuperar os componentes do XML convertido
            mensagem = itemView.findViewById(R.id.textViewMensagemTexto);
            imagem = itemView.findViewById(R.id.imageViewMensagemFoto);
            nomeExibicao = itemView.findViewById(R.id.textViewNomeExibicao);

        }
    }
}
