package com.easyway.cloneorganize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.easyway.cloneorganize.R;
import com.easyway.cloneorganize.activity.CadastroActivity;
import com.easyway.cloneorganize.activity.LoginActivity;
import com.easyway.cloneorganize.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //Tirando os botõesw para back e next
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        //Criando os Slides
        addSlide(new FragmentSlide.Builder()
                //Cor de fundo
                .background(android.R.color.white)
                //XML personalizado
                .fragment(R.layout.intro_1)
                //Criar o slider
                .build()
        );

        //Criando os Slides
        addSlide(new FragmentSlide.Builder()
                //Cor de fundo
                .background(android.R.color.white)
                //XML personalizado
                .fragment(R.layout.intro_2)
                //Criar o slider
                .build()
        );

        //Criando os Slides
        addSlide(new FragmentSlide.Builder()
                //Cor de fundo
                .background(android.R.color.white)
                //XML personalizado
                .fragment(R.layout.intro_3)
                //Criar o slider
                .build()
        );

        //Criando os Slides
        addSlide(new FragmentSlide.Builder()
                //Cor de fundo
                .background(android.R.color.white)
                //XML personalizado
                .fragment(R.layout.intro_4)
                //Criar o slider
                .build()
        );


        //Criando os Slides
        addSlide(new FragmentSlide.Builder()
                //Cor de fundo
                .background(android.R.color.white)
                //XML personalizado
                .fragment(R.layout.intro_cadastro)
                //canGoBackward se pode voutar ou seja não deixar dar back voutar ao slide anterior tem que setar como false
                .canGoBackward(true)
                //anGoForward se pode ir adiante ou seja não deixar dar next passar para a proxima tela
                .canGoForward(false)
                //Criar o slider
                .build()
        );
    }

    //Ciclo de vida de uma activity o onStart é inicializando sempre que inicia uma activity
    @Override
    protected void onStart() {
        super.onStart();

        //chamar esse metodo antes de carregar os slides pois se o usuario já for autenticado
        //Irá logar automaticamente no sistema
        //Esse metodo fica aqui para que sempre queesta activity seja iniciada ele teste se há algum usuario autenticado
        verificarUsuarioLogado();
    }

    public void btnEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btnCadastrar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    //Metodo responsavel por fazer o usuario autenticado logar automaticamente no sistema
    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
      //Verificar autenticação
        //getCurrentUser esse metodo recupera o usuario atual se estiver vazio significa que nenhum usuario está autenticado
        if(autenticacao.getCurrentUser() != null){
            abriTelaPrincipal();
        }
    }

    //Metodo responsavel por abrir a tela principal
    public void abriTelaPrincipal(){
        startActivity(new Intent(this,PrincipalActivity.class));
    }
}
