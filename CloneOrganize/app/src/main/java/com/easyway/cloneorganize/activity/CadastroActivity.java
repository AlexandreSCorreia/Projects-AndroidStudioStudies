package com.easyway.cloneorganize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easyway.cloneorganize.R;
import com.easyway.cloneorganize.config.ConfiguracaoFirebase;
import com.easyway.cloneorganize.helper.Base64Custom;
import com.easyway.cloneorganize.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;

    private FirebaseAuth autenticacao;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Toobar
        getSupportActionBar().setTitle("Organizze");

        campoNome = findViewById(R.id.editText_Nome_Cadastro);
        campoEmail = findViewById(R.id.editText_Email_Cadastro);
        campoSenha = findViewById(R.id.editText_Senha_Cadastro);

        botaoCadastrar = findViewById(R.id.btn_cadastrar_Cadastro);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();


                //Validar se os campos foram preenchidos
                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);
                            //Cadastrar Usuario
                            cadastrarUsuario();

                        }else{
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha a senha",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,
                                "Preencha o email",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Preencha o nome",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //Metodo para cadastraro usuario
    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                //Validação
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Estring onde vai ser preenchida com o tipo de exceção
                        String excecao = "";
                        //Verificar se realmente deu certo o cadsatro do usuario
                        if(task.isSuccessful()){

                            //Salvar dados do usuario no banco de dados
                            String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                            usuario.setIdUsuario(idUsuario);
                            usuario.salvar();
                           //fechar activity atual
                            finish();
                        }else{

                            //Tratando possiveis erros ao cadastrar usuario
                            try {
                                //Lançar uma exeção
                                throw task.getException();

                                //Exceção quando elá é ativada quando a senha é fraca possui menos de 6 caracteres
                            }catch (FirebaseAuthWeakPasswordException e){
                                    excecao = "Digite uma senha mais forte!";

                             //Essa exceção é ativada quando o email é incorreto foi escrito errado
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Por favor, digite um e-mail válido!";

                                //Essa exceção é ativa quando já existe uma conta com o endereço de email fornecido
                            }catch (FirebaseAuthUserCollisionException e){
                                excecao = "Está conta já foi cadastrada";
                            }//Exceção generica
                            catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                //Para printar esta exceção mo nosso log
                                e.printStackTrace();
                            }



                            Toast.makeText(CadastroActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
