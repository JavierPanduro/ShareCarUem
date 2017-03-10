package com.aja.proyectointegrado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN = "login";
    EditText etUsuario, etContra;
    Button btnLogin, btnSign;
    CheckBox checkBox;
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static String TAG = "RegisterDEbug";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContra = (EditText) findViewById(R.id.etContra);
        checkBox.setChecked(true);
        if(checkBox.isChecked()==true){
            SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
            etUsuario.setText(prefe.getString("mail", "prueba@gmail.com"));
            etContra.setText(prefe.getString("contraseña", "prueba"));
        }
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSign = (Button) findViewById(R.id.btnSign);

        mAuth = FirebaseAuth.getInstance();     // conectamos con firebase
    }

    public void login(View v) { // Metodo que valida los campos de logueo
        if (etUsuario.getText().toString().equals("")) {    // elusuario no puede estr vacio
            Toast.makeText(this, getString(R.string.controlCorreoLog), Toast.LENGTH_SHORT).show();
        } else if (etContra.getText().toString().equals("")) {  // La contraseña no puede estar vacía
            Toast.makeText(this, getString(R.string.controlContrasenaLog), Toast.LENGTH_SHORT).show();
        } else {    // Si todo va bien...
            String email = etUsuario.getText().toString();
            String contraseña = etContra.getText().toString();
            if(checkBox.isChecked() == true) {
                SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("mail", email);
                editor.putString("contraseña", contraseña);
                editor.commit();
                etUsuario.setText("");
                etContra.setText("");
                checkBox.setChecked(false);
            }
            signIn(email, contraseña);  // metodo para iniciar serion a traves del correo y la contraseña
        }

    }


    public void signup (View v) {   // Metodo para el boton de sign up para cambiar de actividad a la de sig up
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void signIn(String email, String password) {    // Metodo para iniciar sesion
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { // iniciamos sesion con los datos introducidos
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) { // si los datos no son correctos...
                            Toast.makeText(LoginActivity.this, getString(R.string.autentificacionFallida), Toast.LENGTH_SHORT).show();
                        }else{  // si los datos son correctos pasamos a nuestra main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)   // Metodo para cambiar el boton de atras por la funcion de salir
    {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            // Preguntamosal usuario si quiere salir
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.salir))
                    .setMessage(getString(R.string.exit))
                    .setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // Si le damos al boton de "SI" nos salimos de la aplicación si no, no pasa nada
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void restablecer(String s) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = s;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Se ha enviado el mensaje");
                        }else {
                            Log.d(TAG, "No se ha enviado el mensaje");
                        }
                    }
                });
    }

    public void showChangeLangDialog(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_custom, null);
        dialogBuilder.setView(dialogView);
        final EditText etrestar = (EditText) dialogView.findViewById(R.id.etrestar);
        dialogBuilder.setTitle("Restablecer Contraseña");
        dialogBuilder.setMessage("Introduzca su correo");
        dialogBuilder.setPositiveButton("Hecho", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = etrestar.getText().toString().trim();
                restablecer(email);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(LoginActivity.this, "Se ha cancelado la operación", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}

