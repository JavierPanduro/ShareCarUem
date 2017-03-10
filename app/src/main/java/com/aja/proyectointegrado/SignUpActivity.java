package com.aja.proyectointegrado;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {
    CheckBox terminos;
    Button btnRegistro;
    EditText etUsuario, etContra1, etContra2, etEmail;
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static String TAG = "RegisterDEbug";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContra1 = (EditText) findViewById(R.id.etContra1);

        etContra1.addTextChangedListener(new TextValidator(etContra1) { // validador de texto de android (saldra un mensajito encima del EdiTtext
            @Override
            public void validate(EditText editText, String text) {
                //Implementamos la validación que queramos
                if( text.length() < 8 ) // Comprobamos que la contraseña tenga mas de 7 caracteres
                    etContra1.setError( getString(R.string.controlContrasena1Sig) );
            }
        });
        etEmail = (EditText) findViewById(R.id.etEmail);
        terminos = (CheckBox) findViewById(R.id.terminos);
        mAuth = FirebaseAuth.getInstance();
        btnRegistro = (Button) findViewById(R.id.btnRegistro);


    }

    public void registro (View v) {
        String email = etEmail.getText().toString().trim();
        String contraseña = etContra1.getText().toString().trim();
        if (emailValidator(email) == false) {   // metodo que devuelve booleano para comprobar el correo
            Toast.makeText(this, getString(R.string.controlEmailSig), Toast.LENGTH_SHORT).show();
        } else if (etUsuario.getText().toString().equals("")) { // Comprobar que el usuario no este vacio
            Toast.makeText(this, getString(R.string.controlUsuarioSig), Toast.LENGTH_SHORT).show();
        }else if (nombreValidator(etUsuario.getText().toString()) == false) { // Comprobar que el usuario no tenga caracteres especiales
            Toast.makeText(this, getString(R.string.controlUsuarioSig), Toast.LENGTH_SHORT).show();
        } else if (passwordValidator(contraseña) == false) {    // Metodo que devuelve booleano para comprobar la contraseña
            Toast.makeText(this, getString(R.string.controlContrasena2Sig), Toast.LENGTH_LONG).show();
        } else if (terminos.isChecked() == false) { // Comprueba que la caja de terminos y condiciones tenga que estar marcada
            Toast.makeText(this, getString(R.string.terminos), Toast.LENGTH_SHORT).show();
        }else{  // si todo va bien llega al else

            createAccount(email, contraseña);
        }
    }

    private void createAccount(String email, String password) {     // Con los datos que tenemos creamos un usuario en fireBase con correo y contraseña
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) { // si sale mal nos quedamos en el sigin
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        }else{     // Si sale bien nos vamos al login para meternos con nuestro nuevo usuario
                            createFirebaseUserProfile(task.getResult().getUser());
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void createFirebaseUserProfile(final FirebaseUser user) {
        String name = etUsuario.getText().toString().trim();
        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, user.getDisplayName());
                        }
                    }

                });
    }


    /**
     * validate your email address format. Ex-akhi@mani.com
     */
    //"|^[a-zA-Z]+(\s*[a-zA-Z]*)*[a-zA-Z]+$|"
    public boolean nombreValidator(String nombre) {       // Metodo que valida el correo usando una expresión regular de javaScript
        Pattern pattern;
        Matcher matcher;
        final String NOMBRE_PATTERN = "^[a-zA-Z]+(\\s*[a-zA-Z]*)*[a-zA-Z]{2,}+$";
        pattern = Pattern.compile(NOMBRE_PATTERN);
        matcher = pattern.matcher(nombre);
        return matcher.matches();
    }

    public boolean emailValidator(String email) {       // Metodo que valida el correo usando una expresión regular de javaScript
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean passwordValidator(String contraseña) {   // Metodo que valida la contraseña haciendo que tenga que ser alfanumerica usando una expresion regular de javaScript
        Pattern pattern;
        Matcher matcher;
        final String Contra = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        pattern = Pattern.compile(Contra);
        matcher = pattern.matcher(contraseña);
        return matcher.matches();
    }

    public abstract class TextValidator implements TextWatcher {    // metodo que recoge el validador del largo de la contraseña
        private final EditText editText;

        public TextValidator(EditText editText) {
            this.editText = editText;
        }

        public abstract void validate(EditText editText, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = editText.getText().toString();
            validate(editText, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
    }
}
