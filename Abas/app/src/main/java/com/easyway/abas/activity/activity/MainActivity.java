package com.easyway.abas.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.easyway.abas.R;
import com.easyway.abas.activity.fragment.EmAltaFragment;
import com.easyway.abas.activity.fragment.HomeFragment;
import com.easyway.abas.activity.fragment.InscricoesFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Aplicar configuração na ActionBar
        getSupportActionBar().setTitle("Youtube");
        getSupportActionBar().setElevation(0);

        viewPager = findViewById(R.id.viewpager);
        smartTabLayout = findViewById(R.id.viewpagertab);

        //Configurar Abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),//Gerenciador de fragmentos
                FragmentPagerItems.with(this)
                        .add("HOME", HomeFragment.class)
                        .add("INSCRIÇÕES", InscricoesFragment.class)
                        .add("EM ALTA", EmAltaFragment.class)
                        .create()//Classe com as configurações dpos fragmentos que seram ultilizados em cada aba
        );

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

    }
}
