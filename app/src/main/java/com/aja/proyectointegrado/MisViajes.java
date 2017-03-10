package com.aja.proyectointegrado;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MisViajes extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    private final String TAG = MisViajes.class.getSimpleName();
    private ListView lista;
    ArrayList<Viaje> viajes = new ArrayList<>();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private ChildEventListener childEvent;
    private Viaje viaje;
    private Query query;
    public static final String LIST = "list";
    public static final String USUARIO = "usuario";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_mis_viajes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtUsuario = (TextView) view.findViewById(R.id.txtUsuario);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        String name = null;
        String uid = null;
        lista = (ListView) view.findViewById(R.id.list);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
            uid = user.getUid();
            txtUsuario.setText(name);
            String photoUrl = String.valueOf(user.getPhotoUrl());
            if (photoUrl != null && !photoUrl.isEmpty() && !photoUrl.equals("null")) {
                Picasso
                        .with(getActivity())
                        .load(photoUrl)
                        .into(imageView);
            } else {
                //String photoUrl2 = String.valueOf(imageView.getResources());
                imageView.setImageResource(R.drawable.user_default);
                //Picasso.with(getActivity()).load().into(imageView);
            }
        } else {                                // Si no encuentra usuario
            // Pasamos a la actividad del login
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
        }

        query = database.getReference("Viajes").orderByChild("uidConductor").equalTo(uid);

        childEvent = new ChildEventListener() {
            ListAdapter adapt;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                viaje = dataSnapshot.getValue(Viaje.class);
                viajes.add(viaje);
                adapt = new ListAdapter(getContext(), viajes);
                lista.setAdapter(adapt);
                //Log.d(TAG, "Funciona");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                viaje = dataSnapshot.getValue(Viaje.class);
                for(int x=0;x<viajes.size();x++) {
                    viajes.remove(x);
                    viajes.add(viaje);
                    adapt = new ListAdapter(getContext(), viajes);
                    lista.setAdapter(adapt);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                viaje = dataSnapshot.getValue(Viaje.class);
                for (int x = 0; x < viajes.size(); x++) {
                    viajes.remove(x);
                    adapt = new ListAdapter(getContext(), viajes);
                    lista.setAdapter(adapt);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Failed to load Viaje.", Toast.LENGTH_SHORT).show();
            }
        };

        query.addChildEventListener(childEvent);
        ListAdapter adapter = new ListAdapter(getContext(), viajes);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(this);
        lista.setOnItemLongClickListener(this);
        lista.setEmptyView(view.findViewById(R.id.txtninguno));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent in = new Intent();
        in.putExtra(LIST, viajes.get(position));
        showChangeLangDialog(in);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PerfilConductor.class);
        intent.putExtra(LIST, viajes.get(position));
        startActivity(intent);
        return false;
    }

    public void showChangeLangDialog(Intent in) {
        viaje = in.getParcelableExtra(MisViajes.LIST);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_modificar_viaje, null);
        final Button borrar = (Button) dialogView.findViewById(R.id.btnBorrar);
        final Button actualizar = (Button) dialogView.findViewById(R.id.btnActualizar);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("¿Que desea hacer?");
        dialogBuilder.setMessage("Elija una opción");
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = viaje.getKeyReserva();
                String key2 = viaje.getKeyViaje();
                database.getReference("Reservas").child(key).setValue(null);
                database.getReference("Viajes").child(key2).setValue(null);
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActualizarViaje.class);
                intent.putExtra(LIST, viaje);
                startActivity(intent);
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }


}
