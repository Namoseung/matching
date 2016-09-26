package com.example.oseung.myapplication;

/**
 * Created by oseung on 2016-09-01.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ExerciseActivity extends Activity {

    ImageView baskitball1,baseball1,footwear1,socer1;

    ViewPager mViewPager;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excercise_activity);

        getWindow().setWindowAnimations(0);


        baseball1 = (ImageView)findViewById(R.id.basebal1);
        baskitball1=(ImageView)findViewById(R.id.baskitball1);
        footwear1 = (ImageView)findViewById(R.id.footwear1);
        socer1 = (ImageView)findViewById(R.id.soccer1);



        baskitball1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this,Baskitball.class);
                startActivity(intent);

            }
        });
        baseball1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this,Baseball.class);
                startActivity(intent);
            }
        });
        footwear1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this,Footwear.class);
                startActivity(intent);
            }
        });

        socer1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, Soccer.class);
                startActivity(intent);
            }
        });
    }



}