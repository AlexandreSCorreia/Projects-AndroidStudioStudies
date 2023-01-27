package com.example.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.example.whatsapp.adapter.GrupoSelecionadoAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.modal.Grupo;
import com.example.whatsapp.modal.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private TextView textViewTotalParticipantes;
    private EditText editTextNomeGrupo;
    private FloatingActionButton fabSalvarGrupo;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private RecyclerView recyclerViewMembrosGrupo;
    private CircleImageView circleImageViewFotoGrupo;
    private static final int SELECAO_GALERIA=200;

    private StorageReference storageReference;
    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);

        //configurações iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        textViewTotalParticipantes = findViewById(R.id.textViewTotalParticipantes);
        recyclerViewMembrosGrupo = findViewById(R.id.recyclerViewMembrosGrupo);
        editTextNomeGrupo = findViewById(R.id.editTextNomeGrupo);
        circleImageViewFotoGrupo = findViewById(R.id.circleImageViewFotoGrupo);
        fabSalvarGrupo = findViewById(R.id.fabSalvarGrupo);
        grupo = new Grupo();

        //Configurar evento de clique
        circleImageViewFotoGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //vaitestar se o usuario possui camera ou algum software de galeria de fotos
                //então está testando se é possivel instanciar essa intent
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent,SELECAO_GALERIA);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Recuperar lista de membros passada
        if(getIntent().getExtras() != null){
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            listaMembrosSelecionados.addAll(membros);


            textViewTotalParticipantes.setText("Participantes: " + listaMembrosSelecionados.size());
        }


        //Configurar adapter membrosSelecionados
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados,getApplicationContext());

        //Configurar recyclerView para membros Selecionados

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false //se colocar como true irá inverter a ordem que
                // vai mostrar os itens tipo os do fim vai mostrar no começo
        );
        recyclerViewMembrosGrupo.setLayoutManager(layoutManagerHorizontal);
        recyclerViewMembrosGrupo.setHasFixedSize(true);
        recyclerViewMembrosGrupo.setAdapter(grupoSelecionadoAdapter);

        //configurar float action buttom
        fabSalvarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeGrupo = editTextNomeGrupo.getText().toString();

                if(!nomeGrupo.isEmpty()){
                    //Adicionar usuario logado na lista pois ele faz parte do grupo
                    listaMembrosSelecionados.add(UsuarioFirebase.getDadosUsuarioLogado());
                    grupo.setNome(nomeGrupo);
                    grupo.setMembros(listaMembrosSelecionados);
                    grupo.salvar();

                    Intent intent = new Intent(CadastroGrupoActivity.this, ChatActivity.class);
                    intent.putExtra("chatGrupo", grupo);
                    startActivity(intent);

                }else{
                    Toast.makeText(CadastroGrupoActivity.this,
                            "Defina um nome para o grupo"
                            ,Toast.LENGTH_SHORT).show();
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
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                        break;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            if(imagem != null){
                circleImageViewFotoGrupo.setImageBitmap(imagem );
                //recuperar dados da imagem para o firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                byte[] dadosImagem = baos.toByteArray();


                //Salvar imagem no firebase
                StorageReference imagemRef = storageReference
                        .child("imagens")
                        .child("grupos")
                        .child(grupo.getId() +".jpeg");


                UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                ////este para saber se foi feito mesmo o upuload da imagem
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CadastroGrupoActivity.this,"Erro ao fazer upload da imagem",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(CadastroGrupoActivity.this,"Sucesso ao fazer upload da imagem",Toast.LENGTH_SHORT).show();
                        String url = taskSnapshot.getDownloadUrl().toString();
                        grupo.setFoto(url);

                    }
                });
            }

        }
    }



}
