package com.merteroglu.unsplashandgooglemaps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class SliderScreen extends TutorialActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        boolean firstTime = sharedPreferences.getBoolean("firstTime",true);

        if(!firstTime){
            startActivity(new Intent(this,MainActivity.class));
        }


        addFragment(new Step.Builder()
                .setTitle("Harita Üzerinde Gezinme")
                .setContent("Haritada işaretli bölgelere tıklayarak orada çekilmiş fotoğrafları görüntüleyebilirsiniz.")
                .setBackgroundColor(Color.parseColor("#0a92cc"))
                .setDrawable(R.drawable.ss1)
                .setSummary("")
                .build()
        );

        addFragment(new Step.Builder()
                .setTitle("Menu Gezintisi")
                .setContent("Menude fotoğrafları görüntüleyebilir ve sırada ki fotoğrafa butonlar yardımıyla geçebilirsiniz.Yeni" +
                        "resmin konumu harita üzerinde gösterilir")
                .setBackgroundColor(Color.parseColor("#0a92cc"))
                .setDrawable(R.drawable.ss2)
                .setSummary("")
                .build()
        );

        addFragment(new Step.Builder()
                .setTitle("Fotoğraf Hakkında Bilgiler")
                .setContent("Açılır menude aşağı kaydırarak fotoğraf hakkında çekildiği tarih, yer ve açıklamaları görüntüleyebilirsiniz.")
                .setBackgroundColor(Color.parseColor("#0a92cc"))
                .setDrawable(R.drawable.ss3)
                .setSummary("")
                .build()
        );

    }

    @Override
    public void finishTutorial() {
        editor.putBoolean("firstTime",false);
        editor.commit();
        startActivity(new Intent(this,MainActivity.class));
    }
}
