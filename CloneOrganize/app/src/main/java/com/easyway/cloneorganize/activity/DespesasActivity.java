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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {

    private EditText campoValor;
    private TextInputEditText campoData,campoCategoria,campoDescricao;
    private FloatingActionButton fabSalvar;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double depesaTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.campoValor);
        campoData = findViewById(R.id.campoData);
        campoCategoria = findViewById(R.id.campoCategoria);
        campoDescricao = findViewById(R.id.campoDescricao);
        fabSalvar =findViewById(R.id.fabSalvar);

        //Preemcher o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());

        //Recupearr a despesa total antes do usuario lançar outra despesa
        recuperarDespesaTotal();
    }

    public void salvarDespesa(View view){
        if(validarCamposDespesa()){
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setData(data);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setTipo("d");

            //Atualizar despesaTotal no usuario
            Double despesaAtualizada = depesaTotal + valorRecuperado;
            atualizarDespesa(despesaAtualizada);

            //Salvar dados
            movimentacao.salvar(data);
            //Fechar activity
            finish();
        }
    }

    public Boolean validarCamposDespesa(){

        String textoData = campoData.getText().toString();
        String textoCategoria =campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoValor = campoValor.getText().toString();

        if(!textoValor.isEmpty()){
            if(!textoData.isEmpty()){
                if(!textoCategoria.isEmpty()){
                    if(!textoDescricao.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(DespesasActivity.this,"Descrição não foi preenchida!",Toast.LENGTH_LONG).show();
                        return false;
                    }

                }else{
                    Toast.makeText(DespesasActivity.this,"Categoria não foi preenchida!",Toast.LENGTH_LONG).show();
                    return false;
                }

            }else{
                Toast.makeText(DespesasActivity.this,"Data não foi preenchida!",Toast.LENGTH_LONG).show();
                return false;
            }

        }else{
            Toast.makeText(DespesasActivity.this,"Valor não foi preenchido!",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void recuperarDespesaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        //Criar um ouvinte
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //dataSnapshot.getValue(Usuario.class) ele vai converter o objeto no firebase para a classe que foi passada como parametro
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                depesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesa(Double despesa){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        //Atualizar valor da despesa
        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}
