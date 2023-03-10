package com.easyway.cloneorganize.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.easyway.cloneorganize.adapter.AdapterMovimentacao;
import com.easyway.cloneorganize.config.ConfiguracaoFirebase;
import com.easyway.cloneorganize.helper.Base64Custom;
import com.easyway.cloneorganize.model.Movimentacao;
import com.easyway.cloneorganize.model.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.easyway.cloneorganize.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {


    private MaterialCalendarView calendarView;
    private TextView textoSaudacao,textoSaldo;
    private Double receitaTotal = 0.0;
    private Double despesaTotal = 0.0;
    private Double resumoUsuario = 0.0;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;
    private Movimentacao movimentacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        textoSaudacao = findViewById(R.id.textView_Saudacao);
        textoSaldo = findViewById(R.id.textView_Saldo);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView_Movimentos);
        configuraCalendarView();
        swipe();

        //Configurar Adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);

        //Configurar RecycleView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    public void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        //Anequissar no recyclerView
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertaDialog
        alertDialog.setTitle("Excluir movimenta????o da conta");
        alertDialog.setMessage("Voc?? tem certeza que deseja realmente excluir essa movimenta????o de sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Recuperar item que foi deslizado
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);


                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);
                movimentacaoRef = firebaseRef.child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);
                //Excluir movimentacao
                movimentacaoRef.child(movimentacao.getIdMovimentacao()).removeValue();
                //Remover da lista, na verdade falar para lista que o item foi removido
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                //Ir?? atualizar a lista
                adapterMovimentacao.notifyDataSetChanged();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void atualizarSaldo(){

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);


        if(movimentacao.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);

        }

        if(movimentacao.getTipo().equals("d")){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);

        }
    }

    public void recuperarMovimentacoes(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado);


        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar a lista
                movimentacoes.clear();
                //getChildren para percorrer cada uma das movimentacoes
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    //Vamos est?? recuperando uma movimentacao inteira usando o getValue
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setIdMovimentacao(dados.getKey());//Capturando o id da movimentacao
                    movimentacoes.add(movimentacao);


                    //Vai mostrar o for percorrendo e recuperando todos os filhos de movimentacao
                    //Log.i("dados","retorno: "+ dados.toString());
                }

                adapterMovimentacao.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void recuperarResumo(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        //Ouvinte
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //preenchendo o usuario com os dados recuperados
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                //Classe para formatar um numero decimal e aparecer apenas as duas ultimas casa tipo 800.1432421412 vai aparecer 800.14
                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format(resumoUsuario);
                textoSaudacao.setText("Ol??, " + usuario.getNome());
                textoSaldo.setText("R$ " + resultadoFormatado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Criar menu na toobar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Converter um arquivo xml em uma view
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Metodo para tratar as a??oes do menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_Sair:
                //Vai deslogar o usuario
                autenticacao.signOut();
                //Agora irei finalizar essa activity e iniciar o app a partir da activity inicial
                startActivity(new Intent(this,MainActivity.class));
                //Finalizar activity
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarDispesa(View view){
        startActivity(new Intent(this,DespesasActivity.class));
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(this,ReceitasActivity.class));
    }

    public void configuraCalendarView(){
        //Comfigurando o titulo dos meses
        CharSequence meses[] = {"Janeiro","Fevereiro","Mar??o","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendarView.setTitleMonths(meses);
        CalendarDay dataAtual = calendarView.getCurrentDate();
        //% diz que quero criar uma formatacao, 0 diz que irei preecher com zero
        //2 diz que eu quero no maximo 2 digitos e o d de digito ?? numero
        String mesSelecionado = String.format("%02d",(dataAtual.getMonth() + 1));

        mesAnoSelecionado = String.valueOf(mesSelecionado +""+ dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d",(date.getMonth() + 1));
                mesAnoSelecionado = String.valueOf(mesSelecionado +""+ date.getYear());

                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();
            }
        });
    }

    //?? chamado sempre quando iniciar o app
    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        //Fica aqui pois s?? vai recuperar as movimentacoes apos o calendario for configurado
        //E tambem porque ter?? um value event listener que tambem ser?? removido paos sair do app
        recuperarMovimentacoes();
    }

    //Sobrescrever o metodo onStop ele ?? chamado quando o app n??o estiver mais sendo usado
    @Override
    protected void onStop() {
        super.onStop();
        //Remover o evento listener para que o fire base n??o fique atualizando os dados quando o usuario
        //N??o estiver ultilizando o app
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }
}
