package com.example.sarah.labb3;


/*  Sarah Fosse sarfo265
    Malin Wetterskog malwe794
    TDDC73
    Labb 3
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new InteractiveSearcher(this));

    }
}

