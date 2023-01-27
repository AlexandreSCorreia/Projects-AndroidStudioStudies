package com.easyway.cloneorganize.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easyway.cloneorganize.R;
import com.easyway.cloneorganize.config.ConfiguracaoFirebase;
import com.easyway.cloneorganize.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class LoginActivity extends AppCompatActivity {


    private EditText campoEmail, campoSenha;
    private Button botao_entrar;
    private Usuario usuario;

    //Recuperar uma instancia do firebaseAuth
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editText_Email_Login);
        campoSenha = findViewById(R.id.editText_Senha_Login);
        botao_entrar = findViewById(R.id.btn_Entrar_Login);


        botao_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();


                //Validar se os campos foram preenchidos

                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){
                            usuario = new Usuario();
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);

                            //Validar o login
                            validarLogin();

                        }else{
                            Toast.makeText(LoginActivity.this,
                                    "Preencha a senha",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this,
                                "Preencha o email",
                                Toast.LENGTH_SHORT).show();
                    }

                }
        });
    }


    //Metodo para validar o login
    public void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //Tenta entrar em um usuario com email e senha fornecido
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
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

    //Metodo responsavel por abrir a tela principal
    public void abriTelaPrincipal(){
            startActivity(new Intent(this,PrincipalActivity.class));
            finish();
    }
}
