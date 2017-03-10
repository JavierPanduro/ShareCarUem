package com.aja.proyectointegrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.RatingBar;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MisReservas extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
private final String TAG = MisViajes.class.getSimpleName();
private ListView lista, list2;
private ArrayList<Viaje> viajes = new ArrayList<>();
private final FirebaseDatabase database = FirebaseDatabase.getInstance();
private DatabaseReference myRef;
private ChildEventListener childEvent2;
private Viaje viaje;
private Query query2;
public static final String LIST = "list";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_mis_reservas, container, false);
        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            TextView txtUsuario = (TextView) view.findViewById(R.id.txtUsuario);
            TextView ninguna = (TextView) view.findViewById(R.id.txtninguno);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            String name = null;
            String uid = null;
            lista = (ListView) view.findViewById(R.id.list);
            list2 = (ListView) view.findViewById(R.id.list2);
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
                    imageView.setImageResource(R.drawable.user_default);
                }
            } else {                                // Si no encuentra usuario
                // Pasamos a la actividad del login
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                //Toast.makeText(this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
            }

    query2 = database.getReference("Reservas").orderByChild("uidReserva").equalTo(uid);


    childEvent2 = new ChildEventListener() {
        ReservasAdapter adapter;
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            viaje = dataSnapshot.getValue(Viaje.class);
            //for(int x=0;x<viajes.size();x++) {
                //viajes.remove(x);
                viajes.add(viaje);
                adapter = new ReservasAdapter(getContext(), viajes);
                list2.setAdapter(adapter);
            //}
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            viaje = dataSnapshot.getValue(Viaje.class);
            for(int x=0;x<viajes.size();x++) {
                viajes.remove(x);
                viajes.add(viaje);
                adapter = new ReservasAdapter(getContext(), viajes);
                list2.setAdapter(adapter);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            viaje = dataSnapshot.getValue(Viaje.class);
            for (int x = 0; x < viajes.size(); x++) {
                viajes.remove(x);
                adapter = new ReservasAdapter(getContext(), viajes);
                list2.setAdapter(adapter);
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

        query2.addChildEventListener(childEvent2);
        ReservasAdapter adapter = new ReservasAdapter(getContext(), viajes);
        list2.setAdapter(adapter);
        list2.setOnItemClickListener(this);
        list2.setOnItemLongClickListener(this);
        list2.setEmptyView(view.findViewById(R.id.txtninguno));
}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(LIST, viajes.get(position));
        showChangeLangDialog(intent);
    }

    public void showChangeLangDialog(final Intent in) {
        viaje = in.getParcelableExtra(MisViajes.LIST);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_modificar_viaje, null);
        final Button Cancelar = (Button) dialogView.findViewById(R.id.btnBorrar);
        final Button valorar = (Button) dialogView.findViewById(R.id.btnActualizar);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("¿Que desea hacer?");
        dialogBuilder.setMessage("Elija una opción");
        Cancelar.setText("Cancelar la reserva");
        valorar.setText("Valorar el viaje");
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                database.getReference("Reservas").orderByChild("uidReservas").equalTo(uid).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                myRef = database.getInstance().getReference();
                                int plazas = (viaje.getPlazasreservadas());
                                viaje.setNumeroplazas(viaje.getNumeroplazas()+plazas);
                                viaje.setPlazasreservadas(0);
                                String key2 = viaje.getKeyViaje();
                                database.getReference("Viajes").child(key2).setValue(viaje);
                                String key = viaje.getKeyReserva();
                                database.getReference("Reservas").child(key).setValue(null);
                                //dataSnapshot.getRef().removeValue();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });
            }
        });
        valorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValorarViaje(in);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void ValorarViaje(Intent in){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.item_valoracion, null);
        dialogBuilder.setView(dialogView);
        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);
        dialogBuilder.setTitle("Valoración del viaje");
        dialogBuilder.setMessage("Introduzca Valoración");
        dialogBuilder.setPositiveButton("Hecho", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String totalStars = "Total Stars: " + ratingBar.getNumStars();
                float rating = ratingBar.getRating();
                viaje.setNumeroplazas(viaje.getNumeroplazas()-viaje.getPlazasreservadas());
                viaje.setValoracion(rating);
                String key = viaje.getKeyViaje();
                database.getReference("Viajes").child(key).setValue(viaje);
                dialog.dismiss();
                //Toast.makeText(getActivity(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getActivity(), "Se ha cancelado la operación", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PerfilConductor.class);
        intent.putExtra(LIST, viajes.get(position));
        startActivity(intent);
        return false;
    }
}
