package com.aja.proyectointegrado;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PerfilConductor extends AppCompatActivity {
    TextView txtNombre, txtEmail, txtViajes;
    RatingBar ratingBar2;
    ImageView imageView2;
    Button btnViajes, btnVolver;
    private Usuario usuario = new Usuario();
    private Viaje viaje;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);
        Intent i = getIntent();
        viaje = i.getParcelableExtra(MisReservas.LIST);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtViajes = (TextView) findViewById(R.id.txtViajes);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        txtNombre.setText("Nombre Usuario: "+viaje.usuario.getNombre());
        txtEmail.setText("Email Usuario: "+viaje.usuario.getEmail());
        txtViajes.setText("Nº Viajes: "+viaje.usuario.getNumViaje());
        String photoUrl = String.valueOf(viaje.usuario.getFoto());
        Picasso
                .with(this)
                .load(photoUrl)
                .into(imageView2);
        btnViajes = (Button) findViewById(R.id.btnViajes);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnViajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalStars = "Total Stars: " + ratingBar2.getNumStars();
                float rating = ratingBar2.getRating();
                usuario.setNombre(viaje.usuario.getNombre());
                usuario.setEmail(viaje.usuario.getEmail());
                usuario.setFoto(viaje.usuario.getFoto());
                usuario.setNumViaje(viaje.usuario.getNumViaje());
                usuario.setValoracion(rating);
                String uid = viaje.getUidConductor();
                database.getReference("Users").child(uid).setValue(usuario);
                verviajesusuario();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
    }

    public void volver(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void verviajesusuario (){
        Intent intent = new Intent(this, ViajesUsuario.class);
        intent.putExtra(MisReservas.LIST, viaje);
        startActivity(intent);
    }
}
