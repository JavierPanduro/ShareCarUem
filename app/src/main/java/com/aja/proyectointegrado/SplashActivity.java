package com.aja.proyectointegrado;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    public static final int segundos = 1;
    public static final int MILISEGUNDOS = segundos * 1000;
    private ImageButton car;
    ImageView t1,t2,t3,t4,t5;
    TextView mytitle;
    Typeface tipography;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mytitle =(TextView)findViewById(R.id.txtmytitle);
        tipography = Typeface.createFromAsset(getAssets(),"fonts/Barrio.ttf");
        mytitle.setTypeface(tipography);
        t1 = (ImageView) findViewById(R.id.t1);
        t2 = (ImageView) findViewById(R.id.t2);
        t3 = (ImageView) findViewById(R.id.t3);
        t4 = (ImageView) findViewById(R.id.t4);
        t5 = (ImageView) findViewById(R.id.t5);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        t4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        empezar_animacion();
        // progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


        //car = (ImageButton) findViewById(R.id.car);
        //Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        //car.startAnimation(pulse);

    }
    private void empezar_animacion() {
        new CountDownTimer(MILISEGUNDOS, 5) {
            @Override
            public void onTick(long millisUntilFinished) {
                t1.setVisibility(View.VISIBLE);
                t1.startAnimation(
                        AnimationUtils.loadAnimation(SplashActivity.this, R.anim.alpha) );
            }

            @Override
            public void onFinish() {
                //empezaria animacion 2

                new CountDownTimer(MILISEGUNDOS, 5) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        t2.setVisibility(View.VISIBLE);
                        t2.startAnimation(
                                AnimationUtils.loadAnimation(SplashActivity.this, R.anim.alpha) );
                    }

                    @Override
                    public void onFinish() {
                        //empezaria animacion 3

                        new CountDownTimer(MILISEGUNDOS, 5) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                t3.setVisibility(View.VISIBLE);
                                t3.startAnimation(
                                        AnimationUtils.loadAnimation(SplashActivity.this, R.anim.alpha) );
                            }

                            @Override
                            public void onFinish() {
                                //empezaria animacion 4

                                new CountDownTimer(MILISEGUNDOS, 5) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        t4.setVisibility(View.VISIBLE);
                                        t4.startAnimation(
                                                AnimationUtils.loadAnimation(SplashActivity.this, R.anim.alpha) );
                                    }

                                    @Override
                                    public void onFinish() {
                                        //empezaria animacion 5

                                        new CountDownTimer(MILISEGUNDOS, 5) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                t5.setVisibility(View.VISIBLE);
                                                t5.startAnimation(
                                                        AnimationUtils.loadAnimation(SplashActivity.this, R.anim.alpha) );
                                            }

                                            @Override
                                            public void onFinish() {
                                                new CountDownTimer(MILISEGUNDOS + 1500, 5) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        }
                                                    @Override
                                                    public void onFinish() {
                                                        Intent loading = new Intent(SplashActivity.this,LoginActivity.class);
                                                        startActivity(loading);
                                                        finish();
                                                    }
                                                }.start();
                                            }
                                        }.start();
                                    }
                                }.start();
                            }
                        }.start();
                    }
                }.start();
            }
        }.start();
    }
}
