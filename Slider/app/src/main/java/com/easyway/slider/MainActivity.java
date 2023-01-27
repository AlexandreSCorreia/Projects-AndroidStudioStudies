package com.easyway.slider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;


public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        // Add slides, edit configuration...

        setButtonBackVisible(false);
        setButtonNextVisible(false);


    addSlide(new FragmentSlide.Builder()
            .background(android.R.color.holo_orange_dark)
            .fragment(R.layout.intro_1)
            .build()
    );

    addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_dark)
                .fragment(R.layout.intro_2)
                .build()
    );




    /*Slide simples
        //Definir visibilidade do botão
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new SimpleSlide.Builder()
                    .title("Tirulo")
                    .description("Descrição")
                    //Adicionar imagem
                    .image(R.drawable.um)
                    //Cor de fundo
                    .background(android.R.color.white)
                    //Contruir esse objeto
                    .build());

        addSlide(new SimpleSlide.Builder()
                .title("Tirulo 2")
                .description("Descrição 2")
                //Adicionar imagem
                .image(R.drawable.dois)
                //Cor de fundo
                .background(android.R.color.white)
                //Contruir esse objeto
                .build());


        addSlide(new SimpleSlide.Builder()
                .title("Tirulo 3")
                .description("Descrição 3")
                //Adicionar imagem
                .image(R.drawable.tres)
                //Cor de fundo
                .background(android.R.color.white)
                //Contruir esse objeto
                .build());


        addSlide(new SimpleSlide.Builder()
                .title("Tirulo 4")
                .description("Descrição 4")
                //Adicionar imagem
                .image(R.drawable.quatro)
                //Cor de fundo
                .background(android.R.color.white)
                //Contruir esse objeto
                .build());
      */
    }
}
