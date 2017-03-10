package com.aja.proyectointegrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublicarViaje extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Coche coche = new Coche();
    Viaje viaje = new Viaje();
    CheckBox cocheyes;
    private Usuario usuario = new Usuario();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.activity_publicar_viaje, container, false);
        final EditText etOrigen = (EditText) rootView.findViewById(R.id.etOrigen);
        final EditText etDestino = (EditText) rootView.findViewById(R.id.etDestino);
        final EditText etFecha = (EditText) rootView.findViewById(R.id.etFecha);
        etFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etFecha.isFocused()) {
                    DialogFragment newFragment = new DateDialog();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
            }
        });
        final EditText etHora = (EditText) rootView.findViewById(R.id.etHora);
        etHora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etHora.isFocused()) {
                    DialogFragment newFragment = new TimeDialog();
                    newFragment.show(getFragmentManager(), "timePicker");
                }
            }
        });
        final EditText etPrecio = (EditText) rootView.findViewById(R.id.etPrecio);
        final EditText etDescripcion = (EditText) rootView.findViewById(R.id.etDescripcion);
        final EditText etPlazas = (EditText) rootView.findViewById(R.id.etPlazas);
        cocheyes = (CheckBox) rootView.findViewById(R.id.cocheyes);
        cocheyes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(cocheyes.isChecked() == true){
                    showChangeLangDialog();
                }
            }
        }
        );

        Button btnpublicar = (Button) rootView.findViewById(R.id.btnPublicar);

        btnpublicar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Recoger el texto de un EditText
                String Fecha = etFecha.getText().toString().trim();
                String Hora = etHora.getText().toString().trim();
                String Origen = etOrigen.getText().toString().trim();
                String Destino = etDestino.getText().toString().trim();
                String Precio = etPrecio.getText().toString().trim();
                String Descripcion = etDescripcion.getText().toString().trim();
                String Plazas = etPlazas.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = user.getDisplayName();
                String uid = user.getUid();
                String email = user.getEmail();
                String foto = String.valueOf(user.getPhotoUrl());
                int num = usuario.getNumViaje();
                usuario.setNombre(name);
                usuario.setEmail(email);
                usuario.setFoto(foto);
                usuario.setNumViaje(num+1);
                usuario.setValoracion(0.0f);
                mDatabase.child("Users").child(uid).setValue(usuario);
                String key = mDatabase.child("posts").push().getKey();
                if (origenValidator(etOrigen.getText().toString()) == false) {
                    Toast.makeText(getActivity(), "El Origen no puede estar vacio", Toast.LENGTH_SHORT).show();
                } else if (destinoValidator(etDestino.getText().toString()) == false) {
                    Toast.makeText(getActivity(), "El Destino no puede estar vacio", Toast.LENGTH_SHORT).show();
                } else if (fechaValidator(Fecha) == false) {
                    Toast.makeText(getActivity(), "La Fecha tiene que ser valida", Toast.LENGTH_SHORT).show();
                } else if (horaValidator(Hora) == false) {
                    Toast.makeText(getActivity(), "La Hora tiene que ser valida", Toast.LENGTH_SHORT).show();
                } else if (etPrecio.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "El Precio no puede estar vacio", Toast.LENGTH_SHORT).show();
                } else if (etDescripcion.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "La Descripcion no puede estar vacia", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(Plazas)<0) {
                    Toast.makeText(getActivity(), "Debes tener algun asiento disponible", Toast.LENGTH_SHORT).show();
                } else if (cocheyes.isChecked() == false) {
                    Toast.makeText(getActivity(), "No puedes publicar si no tienes coche", Toast.LENGTH_SHORT).show();
                }else if (coche.getMarca() == null ) {
                    Toast.makeText(getActivity(), "No puedes publicar si no tienes coche", Toast.LENGTH_SHORT).show();
                    cocheyes.setChecked(false);
                } else {
                    viaje.setOrigen(Origen);
                    viaje.setDestino(Destino);
                    viaje.setFecha(Fecha);
                    viaje.setHora(Hora);
                    viaje.setPrecio(Precio);
                    viaje.setDescripcion(Descripcion);
                    viaje.setNumeroplazas(Integer.parseInt(Plazas));
                    viaje.setPlazasreservadas(0);
                    viaje.setKeyViaje(key);
                    viaje.setKeyReserva("");
                    viaje.setUidConductor(uid);
                    viaje.setUidReserva("");
                    viaje.setValoracion(0.0f);
                    viaje.setUsuario(usuario);
                    viaje.setCoche(coche);
                    mDatabase.child("Viajes").child(key).setValue(viaje);
                    /*mDatabase.child("Viaje").child(uid).child(key).child("Destino").setValue(etDestino.getText().toString());
                    mDatabase.child("Viaje").child(uid).child(key).child("Fecha").setValue(etFecha.getText().toString());
                    mDatabase.child("Viaje").child(uid).child(key).child("Hora").setValue(etHora.getText().toString());
                    mDatabase.child("Viaje").child(uid).child(key).child("Precio").setValue(etPrecio.getText().toString());
                    mDatabase.child("Viaje").child(uid).child(key).child("Descripción").setValue(etDescripcion.getText().toString());
                    mDatabase.child("Viaje").child(uid).child(key).child("Usuario").setValue(name);*/
                    etOrigen.setText("");
                    etDestino.setText("");
                    etFecha.setText("");
                    etHora.setText("");
                    etPrecio.setText("");
                    etDescripcion.setText("");
                    etPlazas.setText("");
                    cocheyes.setChecked(false);
                }

            }
        });
        return rootView;

    }

    public boolean origenValidator(String nombre) {       // Metodo que valida el Origen y El destino usando una expresión regular de javaScript
        Pattern pattern;
        Matcher matcher;
        final String NOMBRE_PATTERN = "^[a-zA-Z]+(\\s*[a-zA-Z]*)*[a-zA-Z]{2,}+$";
        pattern = Pattern.compile(NOMBRE_PATTERN);
        matcher = pattern.matcher(nombre);
        return matcher.matches();
    }

    public boolean destinoValidator(String nombre) {       // Metodo que valida el Origen y El destino usando una expresión regular de javaScript
        Pattern pattern;
        Matcher matcher;
        final String NOMBRE_PATTERN = "^[a-zA-Z]+(\\s*[a-zA-Z]*)*[a-zA-Z]{2,}+$";
        pattern = Pattern.compile(NOMBRE_PATTERN);
        matcher = pattern.matcher(nombre);
        return matcher.matches();
    }



        public boolean fechaValidator(String Fecha) {
            Pattern pattern;
            Matcher matcher;
            final String Contra = "^(?:(?:0?[1-9]|1\\d|2[0-8])(\\/|-)(?:0?[1-9]|1[0-2]))(\\/|-)(?:[1-9]\\d\\d\\d|\\d[1-9]\\d\\d|\\d\\d[1-9]\\d|\\d\\d\\d[1-9])$|^(?:(?:31(\\/|-)(?:0?[13578]|1[02]))|(?:(?:29|30)(\\/|-)(?:0?[1,3-9]|1[0-2])))(\\/|-)(?:[1-9]\\d\\d\\d|\\d[1-9]\\d\\d|\\d\\d[1-9]\\d|\\d\\d\\d[1-9])$|^(29(\\/|-)0?2)(\\/|-)(?:(?:0[48]00|[13579][26]00|[2468][048]00)|(?:\\d\\d)?(?:0[48]|[2468][048]|[13579][26]))$";
            pattern = Pattern.compile(Contra);
            matcher = pattern.matcher(Fecha);
            return matcher.matches();
        }

    /*public boolean validarano(String Fecha){
        String [] splitFecha = fecha.split("/") ;
        if (Fecha.split("/")[0]==String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))
        {
            txt2.requestFocus();}
        if (fecha.split("/")[1]==String.valueOf(Calendar.getInstance().get(Calendar.MONTH)))
        {txt2.requestFocus();}
        if (fecha.split("/")[2]==String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))
        {txt2.requestFocus();}
        return true;

    }*/

        public boolean horaValidator(String Hora) {
        Pattern pattern;
        Matcher matcher;
        final String Contra = "^(([01]\\d)|(2[0-3])):([0-5]\\d)$";
        pattern = Pattern.compile(Contra);
        matcher = pattern.matcher(Hora);
        return matcher.matches();
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.coche_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText marca = (EditText) dialogView.findViewById(R.id.etMarca);
        final EditText anocoche = (EditText) dialogView.findViewById(R.id.etanoCoche);
        final EditText timecarnet = (EditText) dialogView.findViewById(R.id.ettime);
        dialogBuilder.setTitle("Datos informativos del coche");
        dialogBuilder.setMessage("Introduzca datos");
        dialogBuilder.setPositiveButton("Hecho", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (marca.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "La Descripcion no puede estar vacia", Toast.LENGTH_SHORT).show();
                    cocheyes.setChecked(false);
                } else if (anocoche.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Debes tener algun asiento disponible", Toast.LENGTH_SHORT).show();
                    cocheyes.setChecked(false);
                } else if( Integer.parseInt(anocoche.getText().toString()) == 0){
                    Toast.makeText(getActivity(), "Debes tener algun asiento disponible", Toast.LENGTH_SHORT).show();
                    cocheyes.setChecked(false);
                } else if (timecarnet.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.terminos), Toast.LENGTH_SHORT).show();
                    cocheyes.setChecked(false);
                } else if(Integer.parseInt(timecarnet.getText().toString()) == 0){
                    Toast.makeText(getActivity(), getString(R.string.terminos), Toast.LENGTH_SHORT).show();
                    cocheyes.setChecked(false);
                } else {
                    String M = marca.getText().toString().trim();
                    int A = Integer.parseInt(anocoche.getText().toString());
                    int T = Integer.parseInt(timecarnet.getText().toString());
                    InsertarDatos(M, A, T);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getContext(), "Se ha cancelado la operación", Toast.LENGTH_SHORT).show();
                cocheyes.setChecked(false);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    public void InsertarDatos (String M, int A, int T){
        coche.setMarca(M);
        coche.setAnoCoche(A);
        coche.setTimeCarnet(T);
    }


}
