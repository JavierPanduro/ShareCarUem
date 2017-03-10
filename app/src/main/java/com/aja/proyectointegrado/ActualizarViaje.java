package com.aja.proyectointegrado;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActualizarViaje extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Viaje viaje;
    EditText etOrigen, etDestino, etFecha, etPlazas, etDescripcion, etHora, etPrecio;
    Button btnactualizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_viaje);
        Intent i = getIntent();
        viaje = i.getParcelableExtra(MisViajes.LIST);
        etOrigen = (EditText) findViewById(R.id.etOrigen);
        etDestino = (EditText) findViewById(R.id.etDestino);
        etFecha = (EditText) findViewById(R.id.etFecha);
        etHora = (EditText) findViewById(R.id.etHora);
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        etPlazas = (EditText) findViewById(R.id.etPlazas);
        btnactualizar = (Button) findViewById(R.id.btnPublicar);
        etFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etFecha.isFocused()) {
                    DialogFragment newFragment = new DateDialog();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });
        etHora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etHora.isFocused()) {
                    DialogFragment newFragment = new TimeDialog();
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                }
            }
        });
        etOrigen.setText(viaje.getOrigen());
        etDestino.setText(viaje.getDestino());
        etFecha.setText(viaje.getFecha());
        etHora.setText(viaje.getHora());
        etPrecio.setText(viaje.getPrecio());
        etDescripcion.setText(viaje.getDescripcion());
        int Plazas = viaje.getNumeroplazas();
        etPlazas.setText(String.valueOf(Plazas));
    }

    public void actualizarViaje(View v) {
        String Plazas = etPlazas.getText().toString();
        database.getReference(getString(R.string.mytrips));
        if (origenValidator(etOrigen.getText().toString()) == false) {
            Toast.makeText(this, getString(R.string.or_n), Toast.LENGTH_SHORT).show();
        } else if (destinoValidator(etDestino.getText().toString()) == false) {
            Toast.makeText(this, getString(R.string.de_n), Toast.LENGTH_SHORT).show();
        } else if (fechaValidator(etFecha.getText().toString()) == false) {
            Toast.makeText(this, getString(R.string.da_t), Toast.LENGTH_SHORT).show();
        } else if (horaValidator(etHora.getText().toString()) == false) {
            Toast.makeText(this, getString(R.string.ho_n), Toast.LENGTH_SHORT).show();
        } else if (etPrecio.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.pr_n), Toast.LENGTH_SHORT).show();
        } else if (etDescripcion.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.ds_n), Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(Plazas) < 0) {
            Toast.makeText(this, getString(R.string.as_tr), Toast.LENGTH_SHORT).show();
        } else {
            viaje.setOrigen(etOrigen.getText().toString().trim());
            viaje.setDestino(etDestino.getText().toString().trim());
            viaje.setFecha(etFecha.getText().toString().trim());
            viaje.setHora(etHora.getText().toString().trim());
            viaje.setPrecio(etPrecio.getText().toString().trim());
            viaje.setDescripcion(etDescripcion.getText().toString().trim());
            viaje.setNumeroplazas(Integer.parseInt(etPlazas.getText().toString()));
            String key = viaje.getKeyViaje();
            database.getReference(getString(R.string.mytrips)).child(key).setValue(viaje);
            Intent intent = new Intent(ActualizarViaje.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
}
