package com.easyway.cloneorganize.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easyway.cloneorganize.R;
import com.easyway.cloneorganize.config.ConfiguracaoFirebase;
import com.easyway.cloneorganize.helper.Base64Custom;
import com.easyway.cloneorganize.helper.DateCustom;
import com.easyway.cloneorganize.model.Movimentacao;
import com.easyway.cloneorganize.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

    private EditText campoValor_receita;
    private TextInputEditText campoData_receita,campoCategoria_receita,campoDescricao_receita;
    private FloatingActionButton fabSalvar_receita;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);


        campoValor_receita = findViewById(R.id.campoValor_receita);
        campoData_receita = findViewById(R.id.campoData_receita);
        campoCategoria_receita = findViewById(R.id.campoCategoria_receita);
        campoDescricao_receita = findViewById(R.id.campoDescricao_receita);
        fabSalvar_receita =findViewById(R.id.fabSalvar);

        //Preemcher o campo data com a data atual
        campoData_receita.setText(DateCustom.dataAtual());

        //Recupearr a despesa total antes do usuario lançar outra despesa
        recuperarReceitaTotal();
    }

    public Boolean validarCamposReceita(){

        String textoData = campoData_receita.getText().toString();
        String textoCategoria =campoCategoria_receita.getText().toString();
        String textoDescricao = campoDescricao_receita.getText().toString();
        String textoValor = campoValor_receita.getText().toString();

        if(!textoValor.isEmpty()){
            if(!textoData.isEmpty()){
                if(!textoCategoria.isEmpty()){
                    if(!textoDescricao.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(ReceitasActivity.this,"Descrição não foi preenchida!",Toast.LENGTH_LONG).show();
                        return false;
                    }

                }else{
                    Toast.makeText(ReceitasActivity.this,"Categoria não foi preenchida!",Toast.LENGTH_LONG).show();
                    return false;
                }

            }else{
                Toast.makeText(ReceitasActivity.this,"Data não foi preenchida!",Toast.LENGTH_LONG).show();
                return false;
            }

        }else{
            Toast.makeText(ReceitasActivity.this,"Valor não foi preenchido!",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void salvarReceita(View view){
        if(validarCamposReceita()){
            movimentacao = new Movimentacao();
            String data = campoData_receita.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor_receita.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setData(data);
            movimentacao.setCategoria(campoCategoria_receita.getText().toString());
            movimentacao.setDescricao(campoDescricao_receita.getText().toString());
            movimentacao.setTipo("r");

            //Atualizar despesaTotal no usuario
            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(receitaAtualizada);

            //Salvar dados
            movimentacao.salvar(data);
            //Fechar activity
            finish();
        }
    }


    public void recuperarReceitaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        //Criar um ouvinte
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //dataSnapshot.getValue(Usuario.class) ele vai converter o objeto no firebase para a classe que foi passada como parametro
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public void atualizarReceita(Double receita){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        //Atualizar valor da despesa
        usuarioRef.child("receitaTotal").setValue(receita);
    }




}
