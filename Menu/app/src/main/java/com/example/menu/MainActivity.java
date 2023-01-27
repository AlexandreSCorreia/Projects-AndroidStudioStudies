package com.example.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView btnNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnNav = (BottomNavigationView) findViewById(R.id.btn_nav);
        btnNav.setOnNavigationItemSelectedListener(navListenner);

        //Fazer já inicializar com a Home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListenner =
           new BottomNavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                   Fragment selectedFragment =null;

                    //Verificando qual é o item selecionado
                   switch(menuItem.getItemId()){



                       case R.id.favorito_nav:
                           selectedFragment = new FavoritoFragment();
                           break;

                       case R.id.configuracao_nav:
                           selectedFragment = new ConfiguracaoFragment();
                           break;

                       case R.id.agendamento_nav:
                           selectedFragment = new AgendamentoFragment();
                           break;

                   }

                    //Trocando layout fragment com replace
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                   return true;
               }
           };
}
