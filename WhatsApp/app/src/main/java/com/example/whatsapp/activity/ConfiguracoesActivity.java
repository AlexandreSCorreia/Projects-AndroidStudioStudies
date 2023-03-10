package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Permissao;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.modal.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton imageButtonCamera, imageButtonGaleria;
    //requestCode
    private static final int SELECAO_CAMERA=100;
    private static final int SELECAO_GALERIA=200;

    private CircleImageView circleImageViewFotoPerfil;
    private EditText editNomePerfil;
    private ImageView imageAtualizaNome;

    private StorageReference storageReference;
    private String identificadorUsuario;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);


        //Configuracoes iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Validar permissoes
        Permissao.validarPermissao(permissoesNecessarias,this,1);

        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleria);

        circleImageViewFotoPerfil = findViewById(R.id.circleImageViewFotoPerfil);
        editNomePerfil = findViewById(R.id.editTextNomePerfil);
        imageAtualizaNome = findViewById(R.id.imageViewAtualizarNome);

        //Configurar Toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configura????es");
        setSupportActionBar(toolbar); //metodo de suporte para a actionBar funcione em versoes anterios do android
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados do usuario
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();
        if (url != null) {
            Glide.with(ConfiguracoesActivity.this)
                    .load(url)
                    .into(circleImageViewFotoPerfil);


        }else{
            circleImageViewFotoPerfil.setImageResource(R.drawable.padrao);
        }

        editNomePerfil.setText(usuario.getDisplayName());

        //Abrir camera
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //vaitestar se o usuario possui camera ou algum software de galeria de fotos
                //ent??o est?? testando se ?? possivel instanciar essa intent
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent,SELECAO_CAMERA);
                }

            }
        });


        //Abrir Galeria
        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //vaitestar se o usuario possui camera ou algum software de galeria de fotos
                //ent??o est?? testando se ?? possivel instanciar essa intent
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent,SELECAO_GALERIA);
                }
            }
        });

        imageAtualizaNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editNomePerfil.getText().toString();
                boolean retorno = UsuarioFirebase.atualizaNomeUsuario(nome);
                if (retorno){
                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualizar();
                    Toast.makeText(ConfiguracoesActivity.this,"Nome alterado com sucesso!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;


            try {

                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                        break;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            if(imagem != null){
                circleImageViewFotoPerfil.setImageBitmap(imagem );
                //recuperar dados da imagem para o firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                byte[] dadosImagem = baos.toByteArray();


                //Salvar imagem no firebase
                StorageReference imagemRef = storageReference
                        .child("imagens")
                        .child("perfil")
                        // .child(identificadorUsuario)
                        // .child("perfil.jpeg")
                        //ou
                        .child(identificadorUsuario +".jpeg");


                UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                ////ste ara saber se foi feito mesmo o upu da imagem
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConfiguracoesActivity.this,"Erro ao fazer upload da imagem",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ConfiguracoesActivity.this,"Sucesso ao fazer upload da imagem",Toast.LENGTH_SHORT).show();
                        Uri url = taskSnapshot.getDownloadUrl();
                        atualizaFotoUsuario(url);
                    }
                });
            }

        }
    }

    public void atualizaFotoUsuario(Uri url){
        boolean retorno = UsuarioFirebase.atualizaFotoUsuario(url);
        if (retorno){
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();

            Toast.makeText(ConfiguracoesActivity.this,
                    "Sua foto foi alterada!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }




    private void alertaValidacaoPermissao(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configurar AlertaDialog
        alertDialog.setTitle("Permiss??es Negadas");
        alertDialog.setMessage("Para utilizar o app ?? necess??rio aceitar as permiss??es");
        //n??o deixar o alert ser cancelado
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //fechar a activity para o usuario n??o usar ela caso n??o de permiss??o
                finish();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}
