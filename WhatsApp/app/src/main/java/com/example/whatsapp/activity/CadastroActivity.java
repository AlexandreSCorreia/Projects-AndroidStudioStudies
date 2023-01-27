package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.modal.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome,campoEmail,campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        campoNome = findViewById(R.id.editNome_cadastro);
        campoEmail = findViewById(R.id.editEmail_cadastro);
        campoSenha = findViewById(R.id.editSenha_cadastro);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    }

    public void cadastrarUsuario(final Usuario usuario){

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                //Estring onde vai ser preenchida com o tipo de exceção
                String excecao = "";

                if(task.isSuccessful()){

                    UsuarioFirebase.atualizaNomeUsuario(usuario.getNome());
                    try {
                        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setIdentificador_Usuario(identificadorUsuario);
                        usuario.salvar();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
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

    public void validarCadastroUsuario(View view){
        //recuperar textos campos
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();


        //Validar se os campos foram preenchidos
        if(!textoNome.isEmpty()){//verifica nome
            if(!textoEmail.isEmpty()){//verifica email
                if(!textoSenha.isEmpty()){//verifica senha

                    //
                    Usuario usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);

                    cadastrarUsuario(usuario);
                }else{
                    Toast.makeText(this,"Preencha a senha!",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this,"Preencha o email!",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this,"Preencha o nome!",Toast.LENGTH_SHORT).show();
        }
    }



}
