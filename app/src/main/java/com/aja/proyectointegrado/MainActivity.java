package com.aja.proyectointegrado;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "tag";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private TabLayout tabs;
    private DatabaseReference mDatabase;
    private static String TAGLOG = "database";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    EditText etOrigen, etDestino, etFecha, etHora, etPrecio, etDescricion;
    Button btnpublicar;
    Animation FabOpen, FabClose, FabClockWise, FabNClockWise;
    FloatingActionButton fab_chat, fab_facebook, fab_close, fab_usermod, fab_useropt;
    boolean isOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab_useropt = (FloatingActionButton) findViewById(R.id.fab_useropt);
        fab_chat = (FloatingActionButton) findViewById(R.id.fab_chat);
        fab_close = (FloatingActionButton) findViewById(R.id.fab_close);
        fab_usermod = (FloatingActionButton) findViewById(R.id.fab_usermod);
        fab_facebook = (FloatingActionButton) findViewById(R.id.fab_facebook);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabNClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);
        fab_useropt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    fab_useropt.startAnimation(FabNClockWise);
                    fab_chat.startAnimation(FabClose);
                    fab_usermod.startAnimation(FabClose);
                    fab_facebook.startAnimation(FabClose);
                    fab_close.startAnimation(FabClose);
                    fab_chat.setClickable(false);
                    fab_usermod.setClickable(false);
                    fab_facebook.setClickable(false);
                    fab_close.setClickable(false);
                    isOpen = false;
                }else{
                    fab_useropt.startAnimation(FabClockWise);
                    fab_chat.startAnimation(FabOpen);
                    fab_usermod.startAnimation(FabOpen);
                    fab_facebook.startAnimation(FabOpen);
                    fab_close.startAnimation(FabOpen);
                    fab_chat.setClickable(true);
                    fab_usermod.setClickable(true);
                    fab_facebook.setClickable(true);
                    fab_close.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fab_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Chat.class);
                startActivity(intent);
            }
        });
        fab_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();           // cerramos sesión en nuestro FireBase
                Intent intent = new Intent(MainActivity.this, LoginActivity.class); // Pasamos a la activadad del login
                startActivity(intent);
            }
        });
        fab_usermod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ModificarUsuario.class); // Pasamos a la activadad del modificar viaje
                startActivity(intent);
            }
        });
        fab_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/ShareCarUEM/?fref=ts");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();         // conecta nuestro proyecto con fireBase

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Viajes");

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.addTab(tabLayout.newTab().setText("Publicar Viaje"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.publicar_viajes));
        //tabLayout.addTab(tabLayout.newTab().setText("Mis Viajes"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.road));
        //tabLayout.addTab(tabLayout.newTab().setText("Buscar Viajes"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.buscar_viaje));
        //tabLayout.addTab(tabLayout.newTab().setText("Mis Reservas"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.reservas));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();    // Buscamos si hay un usuario logueado en nuestro dispositivo y guardamos el usuario (correo) en nuestra variable
        if (user != null) {                     // Si encuentra usuario
            // User is signed
            String name = user.getDisplayName();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one.
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        } else {                                // Si no encuentra usuario
            // Pasamos a la actividad del login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {   // creamos las opciones del menú
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_settings) {     // opción de cerrar sesión
            FirebaseAuth.getInstance().signOut();           // cerramos sesión en nuestro FireBase
            Intent intent = new Intent(MainActivity.this, LoginActivity.class); // Pasamos a la activadad del login
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_settings2) {
            Intent intent = new Intent(MainActivity.this, ModificarUsuario.class); // Pasamos a la activadad del login
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_settings4) {

        }
        return super.onOptionsItemSelected(item);
    }*/
}

