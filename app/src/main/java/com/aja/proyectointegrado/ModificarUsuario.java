package com.aja.proyectointegrado;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ModificarUsuario extends AppCompatActivity {
    private Usuario usuario = new Usuario();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;
    FirebaseUser user;
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private ImageView mSetImage;
    private Button mOptionButton;
    private RelativeLayout mRlView;
    String mPath;
    private final String TAG = ModificarUsuario.class.getSimpleName();
    EditText etMnombre, etMemail, etMcontra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usuario);
        etMnombre = (EditText) findViewById(R.id.etMnombre);
        etMemail = (EditText) findViewById(R.id.etMemail);
        etMcontra = (EditText) findViewById(R.id.etMcontra);
        etMcontra.addTextChangedListener(new ModificarUsuario.TextValidator(etMcontra) { // validador de texto de android (saldra un mensajito encima del EdiTtext
            @Override
            public void validate(EditText editText, String text) {
                //Implementamos la validación que queramos
                if( text.length() < 8 ) // Comprobamos que la contraseña tenga mas de 7 caracteres
                    etMcontra.setError( getString(R.string.controlContrasena1Sig) );
            }
        });

        mSetImage = (ImageView) findViewById(R.id.set_picture);
        mOptionButton = (Button) findViewById(R.id.show_options_button);

        user = FirebaseAuth.getInstance().getCurrentUser();
        etMnombre.setText(user.getDisplayName());
        etMemail.setText(user.getEmail());

        if (mayRequestStoragePermission())
            mOptionButton.setEnabled(true);
        else
            mOptionButton.setEnabled(false);


        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });

    }


    public void actualizarUsuario() {
        if (usuarioValidator(etMnombre.getText().toString()) == false) {
            Toast.makeText(this, "El usuario introducido no es correcto", Toast.LENGTH_SHORT).show();
        } else if (mPath == null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(etMnombre.getText().toString())
                    .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/proyectointegrador-50af0.appspot.com/o/user_default.png?alt=media&token=45ff8f8c-795c-48b3-be03-3ee17d7f9c8c"))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
        } else {
            user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(etMnombre.getText().toString())
                    .setPhotoUri(Uri.parse(mPath))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
        }
    }

    public void actualizarEmail() {
        String email = etMemail.getText().toString();
        if (emailValidator(email) == false) {
            Toast.makeText(this, "El email no tiene un formato correcto", Toast.LENGTH_SHORT).show();
        } else {
            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });
        }
    }

    public void actualizarContraseña() {
        String newPassword = etMcontra.getText().toString();
        if (passwordValidator(newPassword) == false) {
            Toast.makeText(this, "La contraseña no tiene un formato correcto", Toast.LENGTH_SHORT).show();
        } else {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated.");
                            }
                        }
                    });
        }
    }

    public boolean usuarioValidator(String nombre) {       // Metodo que valida el Origen y El destino usando una expresión regular de javaScript
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

    public void modificarUsuario(View v) {
        if (etMcontra.getText().toString().equals("")) {  // La contraseña no puede estar vacía
            Toast.makeText(this, getString(R.string.controlContrasenaLog), Toast.LENGTH_SHORT).show();
        } else {
            actualizarUsuario();
            actualizarEmail();
            actualizarContraseña();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
            String foto = String.valueOf(user.getPhotoUrl());
            usuario.setNombre(name);
            usuario.setEmail(email);
            usuario.setFoto(foto);
            usuario.setNumViaje(0);
            usuario.setValoracion(0.0f);
            mDatabase.child("Users").child(uid).setValue(usuario);
            notificacion();
        }
    }

    private void notificacion() {
    NotificationManager nManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Usuario Modificado")
                .setContentText("Se ha modificado correctamente el Usuario.")
                .setWhen(System.currentTimeMillis());

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        builder.setAutoCancel(true);

        nManager.notify(1, builder.build());
    }

    public void salir (View v){
        Intent intent = new Intent(ModificarUsuario.this, MainActivity.class);
        startActivity(intent);
    }


    private boolean mayRequestStoragePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ModificarUsuario.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Tomar foto") {
                    openCamera();
                } else if (option[which] == "Elegir de galeria") {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    mSetImage.setImageBitmap(bitmap);
                    mPath = bitmap.toString();
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    mSetImage.setImageURI(path);
                    mPath = path.toString();
                    Log.d(TAG, mPath.toString());
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ModificarUsuario.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                mOptionButton.setEnabled(true);
            }
        } else {
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModificarUsuario.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }
}
