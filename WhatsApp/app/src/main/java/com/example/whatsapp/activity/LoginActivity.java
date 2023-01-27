package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.modal.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail,campoSenha;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail_login);
        campoSenha = findViewById(R.id.editSenha_login);
    }


    public void logarUsuario(Usuario usuario){
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //Estring onde vai ser preenchida com o tipo de exceção
                String excecao = "";

                if(task.isSuccessful()){
                    //Abrir tela principal
                    abriTelaPrincipal();
                }else{

                    //Tratando possiveis erros ao cadastrar usuario
                    try {
                        //Lançar uma exeção
                        throw task.getException();

                        //Essa exceção é ativa quando o usuario não esta cadastrado oufoi desativado
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não está cadastrado";

                        //Essa exceção é ativada no login ele testa e devolve só se o emnail e senha
                        // forem incorretos não pertecerem a nenhum usuario cadastrado
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";


                    }//Exceção generica
                    catch (Exception e){
                        excecao = "Erro ao fazer login" + e.getMessage();
                        //Para printar esta exceção mo nosso log
                        e.printStackTrace();
                    }




                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void validarAutenticacaoUsuario(View view){
        //recuperar textos campos
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();


        //Validar se os campos foram preenchidos
        if(!textoEmail.isEmpty()){//verifica email
            if(!textoSenha.isEmpty()){//verifica senha

                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);
                //Logar no sistema
                logarUsuario(usuario);
            }else{
                Toast.makeText(this,"Preencha a senha!",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this,"Preencha o email!",Toast.LENGTH_SHORT).show();
        }
    }

    //Ao iniciar aplicação se o usuario já for autentica ira logar automaticamente
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            abriTelaPrincipal();
        }
    }

    public void abriTelaPrincipal(){
        startActivity(new Intent(this,MainActivity.class));
    }

    public void abrirTelaCadastro(View view){
        startActivity(new Intent(this,CadastroActivity.class));
    }
}
