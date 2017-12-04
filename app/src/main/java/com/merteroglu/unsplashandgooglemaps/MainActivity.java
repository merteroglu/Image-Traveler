package com.merteroglu.unsplashandgooglemaps;


import android.app.ProgressDialog;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.merteroglu.unsplashandgooglemaps.Models.Images;

import java.lang.reflect.Field;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private String API_URL = "https://api.unsplash.com/";

    ImageView imgAna;
    TextView txtIcerik,txtBaslik;
    Button btnPrev,btnNext;
    Services services;
    List<Images> imageList;

    ProgressDialog mDialog;

    ViewPager viewPager;
    ViewPageAdapter adapter;
    int pagerIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        imgAna = findViewById(R.id.imgAna);
        txtIcerik = findViewById(R.id.txtIcerik);
        txtBaslik = findViewById(R.id.txtBaslik);
        viewPager = findViewById(R.id.viewPager);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        mDialog = new ProgressDialog(MainActivity.this);

        services = RetrofitClient.getClient(API_URL).create(Services.class);


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagerIndex > 0){
                    pagerIndex--;
                    viewPager.setCurrentItem(pagerIndex);
                    txtIcerik.setText(imageList.get(pagerIndex).getDescription());
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagerIndex < imageList.size()-1){
                    pagerIndex++;
                    viewPager.setCurrentItem(pagerIndex);
                    txtIcerik.setText(imageList.get(pagerIndex).getDescription());
                }
            }
        });


        Button btnTest = findViewById(R.id.testButton);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();


            }
        });


    }

    public void getImages(){

        mDialog.setMessage("Loading ...");
        mDialog.show();

        services.getImagesList(20).enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {

                if(response.body() == null){
                   mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Veri alınamadı.", Toast.LENGTH_SHORT).show();
                    return;
                }

                imageList = response.body();

                if(imageList.size() <= 0){
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Resim bilgisi alınamadı. Sınır dolmuş olabilir.", Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter = new ViewPageAdapter(MainActivity.this,imageList);
                viewPager.setAdapter(adapter);


               mDialog.dismiss();
                Toast.makeText(MainActivity.this, "Bilgiler alındı", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {
                Log.d("MainActivity","Response failed");
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
