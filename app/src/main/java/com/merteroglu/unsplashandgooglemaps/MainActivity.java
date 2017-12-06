package com.merteroglu.unsplashandgooglemaps;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.merteroglu.unsplashandgooglemaps.Models.Images;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private String API_URL = "https://api.unsplash.com/";

    ImageView imgAna;
    TextView txtIcerik,txtBaslik,txtTarih,txtTitle;
    Button btnPrev,btnNext;
    Services services;
    List<Images> imageList;

    ProgressDialog mDialog;

    ViewPager viewPager;
    ViewPageAdapter adapter;
    int pagerIndex = 0;

    private GoogleMap mMap;


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
        txtTarih = findViewById(R.id.txtTarih);
        txtTitle = findViewById(R.id.txtTitle);
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
                    txtTarih.setText(imageList.get(pagerIndex).getCreated_at().substring(0,10));
                    txtTitle.setText(imageList.get(pagerIndex).getLocation().getTitle());
                    Double lat = Double.parseDouble(imageList.get(pagerIndex).getLocation().getPosition().getLatitude());
                    Double lng = Double.parseDouble(imageList.get(pagerIndex).getLocation().getPosition().getLongitude());
                    updateMaps(new LatLng(lat,lng));
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
                    txtTarih.setText(imageList.get(pagerIndex).getCreated_at().substring(0,10));
                    txtTitle.setText(imageList.get(pagerIndex).getLocation().getTitle());
                    Double lat = Double.parseDouble(imageList.get(pagerIndex).getLocation().getPosition().getLatitude());
                    Double lng = Double.parseDouble(imageList.get(pagerIndex).getLocation().getPosition().getLongitude());
                    updateMaps(new LatLng(lat,lng));
                }
            }
        });


        getImages();


        Button btnTest = findViewById(R.id.testButton);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();


            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




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



                for (int i = 0; i < imageList.size() ; i++) {
                    if(imageList.get(i).getLocation() == null){
                        imageList.remove(i);
                        i--;
                    }else if(imageList.get(i).getLocation().getPosition() == null){
                        imageList.remove(i);
                        i--;
                    }
                }

                if(imageList.size() == 0){
                    mDialog.dismiss();
                    return;
                }

                adapter = new ViewPageAdapter(MainActivity.this,imageList);
                viewPager.setAdapter(adapter);

                txtIcerik.setText(imageList.get(pagerIndex).getDescription());
                txtTarih.setText(imageList.get(pagerIndex).getCreated_at().substring(0,10));
                txtTitle.setText(imageList.get(pagerIndex).getLocation().getTitle());

                for (int i = 0; i < imageList.size() ; i++) {
                    Double lat,lng;
                    lat = Double.parseDouble(imageList.get(i).getLocation().getPosition().getLatitude());
                    lng = Double.parseDouble(imageList.get(i).getLocation().getPosition().getLongitude());
                          mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat,lng))
                                .icon(getMarkerIcon(imageList.get(i).getColor()))
                                .title(imageList.get(i).getLocation().getTitle()));
                }


                for (int i = 0; i < imageList.size() -1 ; i++) {
                    Double lat = Double.parseDouble(imageList.get(i).getLocation().getPosition().getLatitude());
                    Double lng = Double.parseDouble(imageList.get(i).getLocation().getPosition().getLongitude());
                    Double lat2 = Double.parseDouble(imageList.get(i + 1).getLocation().getPosition().getLatitude());
                    Double lng2 = Double.parseDouble(imageList.get(i + 1).getLocation().getPosition().getLongitude());
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(lat, lng), new LatLng(lat2, lng2))
                            .width(5)
                            .color(Color.BLACK));

                    if (i == imageList.size() - 2) {
                        Polyline linex = mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(lat2 ,lng2 ), new LatLng(Double.parseDouble(imageList.get(0).getLocation().getPosition().getLatitude()), Double.parseDouble(imageList.get(0).getLocation().getPosition().getLongitude())))
                                .width(4)
                                .color(Color.BLACK));
                    }

                }


               mDialog.dismiss();
                Toast.makeText(MainActivity.this, "Bilgiler alındı", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {
                mDialog.dismiss();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("marker clicked",marker.getId());
                updateImageView(marker.getId());
                return false;
            }
        });

    }

    public void updateImageView(String markerTag){
        String split = markerTag.substring(1,markerTag.length());
        int clickedID = Integer.parseInt(split);

        pagerIndex = clickedID;
        viewPager.setCurrentItem(pagerIndex);
        txtIcerik.setText(imageList.get(pagerIndex).getDescription());

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if(!drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(GravityCompat.START);
        }

    }

    public void updateMaps(LatLng position){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(5f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        //return BitmapDescriptorFactory.defaultMarker(hsv[0]);
        return BitmapDescriptorFactory.fromResource(R.drawable.marker);
    }



}
