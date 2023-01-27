package com.example.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.adapter.ConversasAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.RecyclerItemClickListener;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.modal.Conversas;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewListaConversas;
    private ArrayList<Conversas> listaConversas = new ArrayList<>();
    private ConversasAdapter adapter;

    private DatabaseReference database;
    private DatabaseReference conversasRef;
    private ChildEventListener childEventListener;


    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //Configurações iniciais
        recyclerViewListaConversas = view.findViewById(R.id.recyclerViewListaConversas);

        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        conversasRef = database.child("conversas").child(identificadorUsuario);


        //Configurar Adapter
        adapter = new ConversasAdapter(listaConversas,getActivity());

        //Configurar RecyclerView
        //Como estamos num fragment com o getActivity podemos passar a activity principal que recebe o fragment
        //Assim será usado o contexto da activity principal neste fragment
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaConversas.setLayoutManager(layoutManager);
        recyclerViewListaConversas.setHasFixedSize(true);
        recyclerViewListaConversas.setAdapter(adapter);




        //Configurar Evento clique no recyclerView
        recyclerViewListaConversas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewListaConversas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                List<Conversas> listaConversaAtualizada = adapter.getConversas();

                                Conversas conversaSelecionado = listaConversaAtualizada.get(position);

                                if(conversaSelecionado.getIsGroup().equals("true")){
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("chatGrupo", conversaSelecionado.getGrupo());
                                    startActivity(intent);

                                }else{
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("chatContato", conversaSelecionado.getUsuarioExibicao());
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        listaConversas.clear();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListener);
    }



    public void pesquisarConversas(String texto){


        List<Conversas> listaConversasBusca = new ArrayList<>();

        for(Conversas conversa : listaConversas){

            if(conversa.getUsuarioExibicao() != null){//Conversa convencional

                //toLowerCase() vai converter o texto inteiro em letras minusculas
                //para a pesquisa sempre ser exata pois o texto digitado e os nomes na lista
                //estaram com letras minusculas assim seram iguais
                String nome =   conversa.getUsuarioExibicao().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();

                //Ao finalizar esse for terei uma nova lista criada a partir da primeira
                //só que filtrada de acordo com o texto digitado
                if(nome.contains(texto) || ultimaMsg.contains(texto)){

                    listaConversasBusca.add(conversa);
                }

            }else{//Conversa de grupo

                //toLowerCase() vai converter o texto inteiro em letras minusculas
                //para a pesquisa sempre ser exata pois o texto digitado e os nomes na lista
                //estaram com letras minusculas assim seram iguais
                String nome =   conversa.getGrupo().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();

                //Ao finalizar esse for terei uma nova lista criada a partir da primeira
                //só que filtrada de acordo com o texto digitado
                if(nome.contains(texto) || ultimaMsg.contains(texto)){

                    listaConversasBusca.add(conversa);
                }
            }

        }

        //reconfigurara o adapter
        adapter = new ConversasAdapter(listaConversasBusca,getActivity());
        recyclerViewListaConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void recarregarConversas(){
        //basicamente o recarregar conversas troca o arrayList que está configurado
        //reconfigurara o adapter
        adapter = new ConversasAdapter(listaConversas,getActivity());
        recyclerViewListaConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarConversas(){


        childEventListener = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Recuerar conversas
                Conversas conversa = dataSnapshot.getValue(Conversas.class);
                listaConversas.add(conversa);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
