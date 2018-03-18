package it.polito.mad.koko.lab1.kokolab1;

/**
 * Created by Francesco on 18/03/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditProfile extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        Button b = findViewById(R.id.save_button);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                Intent i = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(i);
            }
        });

    }
}
