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


public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MysViewHolder> {

    private List<Usuario> contatos;
    private Context context;

    public ContatosAdapter(List<Usuario> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }

    public List<Usuario> getContatos(){
        return  this.contatos;
    }

    @NonNull
    @Override
    public MysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos,parent,false);

        return new MysViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MysViewHolder holder, int position) {
        Usuario usuario = contatos.get(position);
        //se o email for null ele retorna true e o contrario retorna false
        boolean cabeçalho =  usuario.getEmail().isEmpty();

        holder.nome.setText(usuario.getNome());
        holder.email.setText(usuario.getEmail());

        if(usuario.getFoto() != null){
            Uri url = Uri.parse(usuario.getFoto());
            Glide.with(context)
                    .load(url)
                    .into(holder.foto);
        }else{
            //precisamos exibir duas imagens padroes a do usuario sem foto
            // e a do grupo
            if(cabeçalho){
                holder.foto.setImageResource(R.drawable.icone_grupo);
                //caso seja grupo que recebe um email vazio, não irei exibir o email
                //Na verdade irei tirar o TextView que guarda o valor do email para ele não ocupar espaço no adapter
                //assim o nome "Novo Grupo" ficará centralizado
                holder.email.setVisibility(View.GONE);
            }else{
                holder.foto.setImageResource(R.drawable.padrao);
            }

        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    //Inner Class
    //Essa classe irá armazenar os dados de cada elemento da lista
    public class MysViewHolder extends  RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome,email;


        public MysViewHolder(@NonNull View itemView) {
            super(itemView);
            //Recuperar os componentes do XML convertido
            foto  = itemView.findViewById(R.id.circleImageViewFoto);
            nome = itemView.findViewById(R.id.textViewTitulo);
            email  = itemView.findViewById(R.id.textViewSubTitulo);

        }
    }
}
