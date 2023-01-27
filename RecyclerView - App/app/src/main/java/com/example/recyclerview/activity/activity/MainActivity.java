package com.example.recyclerview.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.recyclerview.R;
import com.example.recyclerview.activity.RecyclerItemClickListener;
import com.example.recyclerview.activity.adapter.Adapter;
import com.example.recyclerview.activity.model.Filme;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recycleView;
    private List<Filme> filmesL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       this.recycleView = (RecyclerView) findViewById(R.id.recyclerView);

       //lista de filmes
        this.criaFilmes();

        //Configurar Adapter
        Adapter adapter = new Adapter(filmesL);

        //Configurar RecyclerView]
        //Gerenciador de layout na instancia da para escolher o tipo de layout e passar o contexto como parametro
        RecyclerView.LayoutManager layoutManeger = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        //Configurando layout setando o layout
        this.recycleView.setLayoutManager(layoutManeger);
        //Deixando ele com o tamanho fixo e assim otimizando o recycleView
        this.recycleView.setHasFixedSize(true);
        //Criar uma linha de divisão
        this.recycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        //Preenchendo com o adaptar que vai adaptar os dados ao recycleView
        this.recycleView.setAdapter(adapter);

        //Adicionar evento de click
        this.recycleView.addOnItemTouchListener(
            new RecyclerItemClickListener(
                    getApplicationContext(),
                    recycleView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onItemClick(View view, int position) {
                            Filme filme = filmesL.get(position);
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Item selecionado " +filme.getTitulo(),
                                    Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            Filme filme = filmesL.get(position);

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Click longo" +  filme.getTitulo(),
                                    Toast.LENGTH_LONG).show();

                        }
                    })
        );

    }



    //Metodo para preencher lista de filmes
    public void criaFilmes(){

        Filme filme =new Filme("Joker"," Ação, Crime, Dram","2019",R.drawable.joker);
        filmesL.add(filme);

        filme =new Filme("Maleficent: \nMistress of Evil","Aventura, Família, Fantasia","2019",R.drawable.malev);
        filmesL.add(filme);


        filme =new Filme("It: Chapter Two","Terror","2019",R.drawable.it);
        filmesL.add(filme);

        filme =new Filme("Avengers: Endgame"," Ação, Aventura, Ficção científica","2019",R.drawable.avenger);
        filmesL.add(filme);

        filme =new Filme("Fast & Furious Presents: \nHobbs & Shaw"," Ação","2019",R.drawable.velosesfuriosos);
        filmesL.add(filme);

        filme =new Filme("John Wick: Chapter 3 \n– Parabellum","Ação, Crime","2019",R.drawable.johnwick);
        filmesL.add(filme);

        filme =new Filme("Gemini Man"," Ação","2019",R.drawable.gemini);
        filmesL.add(filme);

        filme =new Filme("Project X","Comédia","2012",R.drawable.projetox);
        filmesL.add(filme);

        filme =new Filme("Shazam!","Ação, Aventura, Comédia, Fantasia, Ficção científica","2019",R.drawable.shazam);
        filmesL.add(filme);

        filme =new Filme("After"," Drama, Eróticos, Romance","2019",R.drawable.after);
        filmesL.add(filme);

        filme =new Filme("Casal Improvável","Comédia, Romance", "2019",R.drawable.casalimprovavel);
        filmesL.add(filme);

        filme =new Filme("Homem-Aranha: \nLonge de Casa"," Ação, Aventura, Ficção científica","2019",R.drawable.homenaranha);
        filmesL.add(filme);
    }
}
