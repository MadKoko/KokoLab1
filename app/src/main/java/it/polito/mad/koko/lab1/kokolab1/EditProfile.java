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
import android.widget.EditText;
import android.widget.TextView;

public class EditProfile extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        Button b = findViewById(R.id.save_button);

        Intent i=getIntent();

        final EditText et_name=findViewById(R.id.edit_user_name);
        final EditText et_email=findViewById(R.id.edit_user_email);
        final EditText et_location=findViewById(R.id.edit_user_location);
        final EditText et_bio=findViewById(R.id.edit_user_bio);

        if(i.getExtras()!=null){
            String user_name=i.getExtras().get("user_name").toString();
            String user_email=i.getExtras().get("user_email").toString();
            String user_location=i.getExtras().get("user_location").toString();
            String user_bio=i.getExtras().get("user_bio").toString();

            et_name.setText(user_name);
            et_email.setText(user_email);
            et_location.setText(user_location);
            et_bio.setText(user_bio);
        }

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(getApplicationContext(),ShowProfile.class);
                i.putExtra("user_name", et_name.getText().toString() );
                i.putExtra("user_email", et_email.getText().toString() );
                i.putExtra("user_location", et_location.getText().toString() );
                i.putExtra("user_bio", et_bio.getText().toString() );
                startActivity(i);

            }
        });

    }
}
