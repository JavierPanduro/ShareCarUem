package com.aja.proyectointegrado;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViajesUsuario extends AppCompatActivity {
    private final String TAG = ViajesUsuario.class.getSimpleName();
    private ListView lista;
    private ArrayList<Viaje> viajes = new ArrayList<>();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private ChildEventListener childEvent;
    private Viaje viaje;
    private Usuario usuario;
    private Query query;
    public static final String LIST = "list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes_usuario);
        Intent i = getIntent();
        viaje = i.getParcelableExtra(MisReservas.LIST);
        TextView txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String name = viaje.usuario.getNombre();
        String uid = viaje.getUidConductor();
        lista = (ListView) findViewById(R.id.list);
            txtUsuario.setText(name);
            String photoUrl = String.valueOf(viaje.usuario.getFoto());
            Picasso
                    .with(this)
                    .load(photoUrl)
                    .into(imageView);

        query = database.getReference("Viajes").orderByChild("uidConductor").equalTo(uid);

        childEvent = new ChildEventListener() {
            ListAdapter adapt;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                viaje = dataSnapshot.getValue(Viaje.class);
                viajes.add(viaje);
                adapt = new ListAdapter(getBaseContext(), viajes);
                lista.setAdapter(adapt);
                //Log.d(TAG, "Funciona");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                viaje = dataSnapshot.getValue(Viaje.class);
                viajes.add(viaje);
                adapt = new ListAdapter(getBaseContext(), viajes);
                lista.setAdapter(adapt);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getBaseContext(), "Failed to load Viaje.", Toast.LENGTH_SHORT).show();
            }
        };
        query.addChildEventListener(childEvent);
        ListAdapter adapter = new ListAdapter(getBaseContext(), viajes);
        lista.setAdapter(adapter);
    }

    public void salir (View v){
        Intent intent = new Intent(ViajesUsuario.this, MainActivity.class);
        startActivity(intent);
    }
}
