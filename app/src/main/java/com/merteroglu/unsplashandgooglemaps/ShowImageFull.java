package com.merteroglu.unsplashandgooglemaps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

public class ShowImageFull extends AppCompatActivity{

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_full);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        image = findViewById(R.id.imgMain);


        Intent i = getIntent();

        String URL = i.getStringExtra("link");
        String color = i.getStringExtra("color");

        RelativeLayout rLayout = findViewById(R.id.imageLayout);
        rLayout.setBackgroundColor(Color.parseColor(color));

        try{
            Picasso.with(getApplicationContext())
                    .load(URL)
                    .into(image);
        }catch (Exception e){

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
