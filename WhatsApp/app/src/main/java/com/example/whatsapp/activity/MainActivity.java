package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.fragment.ContatosFragment;
import com.example.whatsapp.fragment.ConversasFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView materialSearchViewPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();



        //Configurar Toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar); //metodo de suporte para a actionBar funcione em versoes anterios do android

        //Configurar Abas
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversasFragment.class)
                        .add("Contatos", ContatosFragment.class)
                        .create()
        );

        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout smartTabLayout = findViewById(R.id.smartTabLayout);
        smartTabLayout.setViewPager(viewPager);

        //Configuracao do searchView
        materialSearchViewPrincipal = findViewById(R.id.materialSearchPrincipal);

        //listener para search view
        materialSearchViewPrincipal.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            //é ativado quando fexa o search view
            @Override
            public void onSearchViewClosed() {

                //recuperar fragment a partir da posicao dele no adapter
                //Colocar o cast (ConversasFragment) porque por padrao ele tem outro retorno
                ConversasFragment fragment =(ConversasFragment)adapter.getPage(0);
                fragment.recarregarConversas();
            }
        });

        //listener para nossa caixa de testo
        materialSearchViewPrincipal.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Só ira ser ativado apos o usuario terminar de digitar o que ele quer pesquisar
                //e depois confirmar
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //Verificar se esta pesquisando Conversas ou contatos a partir da tab que está ativa
                switch (viewPager.getCurrentItem()){

                    case 0:
                        //é ativado em tempo de execulcao ele vai pesquisando a partir do momento que começca a digitar

                        //recuperar fragment a partir da posicao dele no adapter
                        //Colocar o cast (ConversasFragment) porque por padrao ele tem outro retorno
                        ConversasFragment conversasFragment =(ConversasFragment)adapter.getPage(0);
                        if(newText != null && !newText.isEmpty()){
                            //toLowerCase() vai converter o texto inteiro em letras minusculas
                            //para a pesquisa sempre ser exata pois o texto digitado e os nomes na lista
                            //estaram com letras minusculas assim seram iguais
                            conversasFragment.pesquisarConversas(newText.toLowerCase());
                        }else{
                            conversasFragment.recarregarConversas();
                        }

                        break;

                    case 1:
                        //é ativado em tempo de execulcao ele vai pesquisando a partir do momento que começca a digitar

                        //recuperar fragment a partir da posicao dele no adapter
                        //Colocar o cast (ConversasFragment) porque por padrao ele tem outro retorno
                        ContatosFragment contatosFragment =(ContatosFragment) adapter.getPage(1);
                        if(newText != null && !newText.isEmpty()){
                            //toLowerCase() vai converter o texto inteiro em letras minusculas
                            //para a pesquisa sempre ser exata pois o texto digitado e os nomes na lista
                            //estaram com letras minusculas assim seram iguais
                            contatosFragment.pesquisarContatos(newText.toLowerCase());
                        }else{
                            contatosFragment.recarregarContatos();
                        }
                        break;

                }


                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        //Configurar Botão de pesquisar
        //passar o item que vai ativar o search view
        MenuItem item = menu.findItem(R.id.menu_pesquisa);
        materialSearchViewPrincipal.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.menu_configuracao:
                abrirConfiguracoes();
                break;

            case R.id.menu_sair:
                deslogarUsuario();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void deslogarUsuario(){
        try {
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirConfiguracoes(){
        startActivity(new Intent(this,ConfiguracoesActivity.class));
    }

}
