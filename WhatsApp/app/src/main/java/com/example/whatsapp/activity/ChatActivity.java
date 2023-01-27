package com.example.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.whatsapp.adapter.MensagemsAdaper;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.modal.Conversas;
import com.example.whatsapp.modal.Grupo;
import com.example.whatsapp.modal.Mensagem;
import com.example.whatsapp.modal.Usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private EditText editTextMensagemChat;
    private ImageView imageViewMensagemFotoChat;
    private Usuario usuarioDestinatario;
    private Usuario usuarioRemetente;
    private DatabaseReference database;
    private StorageReference storageReference;
    private DatabaseReference mensagemRef;
    private ChildEventListener childEventListenerMensagens;

    //Identificador usuario remetente e destinatario
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private Grupo grupo;

    private RecyclerView recyclerViewMensagems;
    private MensagemsAdaper adaper;
    private List<Mensagem> mensagemsLista = new ArrayList<>();

    //requestCode
    private static final int SELECAO_CAMERA=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageViewFotoChat);
        editTextMensagemChat = findViewById(R.id.editTextMensagemChat);
        recyclerViewMensagems = findViewById(R.id.recyclerViewMensagems);
        imageViewMensagemFotoChat = findViewById(R.id.imageViewMensagemFotoChat);

        //recuperar dados usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();
        usuarioRemetente = UsuarioFirebase.getDadosUsuarioLogado();
        //Recuperar dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            if(bundle.containsKey("chatGrupo")){

                //recuperando grupo
                grupo = (Grupo) bundle.getSerializable("chatGrupo");
                textViewNome.setText(grupo.getNome());
                idUsuarioDestinatario = grupo.getId();

                String foto = grupo.getFoto();
                if(foto != null){
                    Uri url = Uri.parse(foto);
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(circleImageViewFoto);
                }else{
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }


            }else{
                usuarioDestinatario = (Usuario)bundle.getSerializable("chatContato");
                textViewNome.setText(usuarioDestinatario.getNome());

                String foto = usuarioDestinatario.getFoto();
                if(foto != null){
                    Uri url = Uri.parse(usuarioDestinatario.getFoto());
                    Glide.with(ChatActivity.this)
                            .load(url)
                            .into(circleImageViewFoto);
                }else{
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }

                //recuperar dados do usuario destinatario
                idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());
            }
        }

        //Configurar adapter
        adaper = new MensagemsAdaper(mensagemsLista,getApplicationContext());

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMensagems.setLayoutManager(layoutManager);
        recyclerViewMensagems.setHasFixedSize(true);
        recyclerViewMensagems.setAdapter(adaper);


        //Recuperar Mensagens
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        mensagemRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);


        //Evento clique na camera
        imageViewMensagemFotoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //vaitestar se o usuario possui camera ou algum software de galeria de fotos
                //então está testando se é possivel instanciar essa intent
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent,SELECAO_CAMERA);
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
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            if(imagem != null){
                //recuperar dados da imagem para o firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                byte[] dadosImagem = baos.toByteArray();

                //Criar nome da imagem
                String nomeImagem = UUID.randomUUID().toString();

                //Salvar imagem no firebase
                StorageReference imagemRef = storageReference
                        .child("imagens")
                        .child("fotos")
                        .child(nomeImagem +".jpeg");


                UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                ////ste ara saber se foi feito mesmo o upu da imagem
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this,"Erro ao fazer upload da imagem",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ChatActivity.this,"Sucesso ao fazer upload da imagem",Toast.LENGTH_SHORT).show();
                        String url = taskSnapshot.getDownloadUrl().toString();

                        Mensagem mensagem = new Mensagem();
                        mensagem.setIdUsuario(idUsuarioRemetente);
                        mensagem.setMensagem("imagem.jpeg");
                        mensagem.setImagem(url);



                        //salvar mensagem para o remetente
                        salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);

                        //salvar mensagem para o destinatario
                        salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);


                    }
                });
            }

        }
    }


    public void salvarConversa(String idRemetente, String idDestinatario,Usuario usuarioExibicao,Mensagem mensagem, boolean isGroup){

        Conversas conversasRemetente = new Conversas();
        conversasRemetente.setIdRemetente(idUsuarioRemetente);
        conversasRemetente.setIdDestinatario(idUsuarioDestinatario);
        conversasRemetente.setUltimaMensagem(mensagem.getMensagem());

        if(isGroup){//conversa de grupo
            conversasRemetente.setGrupo(grupo);
            conversasRemetente.setIsGroup("true");

        }else{//conversa normal

            //Usuario ara quem, estou enviando a mensagem
            conversasRemetente.setUsuarioExibicao(usuarioDestinatario);
            conversasRemetente.setIsGroup("false");
        }

        conversasRemetente.salvar();
    }


    public void enviarMensagem(View view){

        String textoMensagem = editTextMensagemChat.getText().toString();

        if (!textoMensagem.isEmpty()){
            //Se não for nulo é iuma conversa convencional entre duas pessoas
            //poderia verificar tambem inves do usuarioDestinatario se haveria um grupo configurado
          if(usuarioDestinatario != null){

              Mensagem mensagem = new Mensagem();
              mensagem.setIdUsuario(idUsuarioRemetente);
              mensagem.setMensagem(textoMensagem);

              //salvar mensagem para o remetente
              salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);

              //salvar mensagem para o destinatario
              salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);

              //salvar conversas remetente
              salvarConversa(idUsuarioRemetente,idUsuarioDestinatario,usuarioDestinatario,mensagem,false);

              //salvar conversas destinatario

              salvarConversa(idUsuarioDestinatario,idUsuarioRemetente,usuarioRemetente,mensagem,false);

          }else{
              //enviar a mensagem para todos os membros do grupo
              for(Usuario membro : grupo.getMembros()){

                  String idRemetenteGrupo = Base64Custom.codificarBase64(membro.getEmail());
                  String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();


                  Mensagem mensagem = new Mensagem();
                  mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                  mensagem.setMensagem(textoMensagem);
                  mensagem.setNome(usuarioRemetente.getNome());


                  //salvar mensagem para o membro do grupo
                  //o idUsuarioDEstinatario está configurado com o id do proprio grupo
                  //pois estamos trocando mensagens com o grupo
                  salvarMensagem(idRemetenteGrupo,idUsuarioDestinatario,mensagem);

                  //salvar conversas
                  salvarConversa(idRemetenteGrupo,idUsuarioDestinatario,usuarioDestinatario,mensagem,true);
              }

          }

        }else{
            Toast.makeText(ChatActivity.this,
                    "Digite uma mensagem para enviar!",
                    Toast.LENGTH_SHORT).show();
        }

    }


    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagensRefe = database.child("mensagens");

        mensagensRefe.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        //Limpar texto
        editTextMensagemChat.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mensagemsLista.clear();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagemRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagens(){

        childEventListenerMensagens = mensagemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagemsLista.add(mensagem);
                adaper.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
