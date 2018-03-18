package it.polito.mad.koko.lab1.kokolab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ShowProfile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_profile);

        Button button=findViewById(R.id.edit_button);

        Intent i=getIntent();

        final TextView tv_name=findViewById(R.id.user_name);
        final TextView tv_email=findViewById(R.id.user_email);
        final TextView tv_location=findViewById(R.id.user_location);
        final TextView tv_bio=findViewById(R.id.user_bio);

        if(i.getExtras()!=null){

            String user_name=i.getExtras().get("user_name").toString();
            String user_email=i.getExtras().get("user_email").toString();
            String user_location=i.getExtras().get("user_location").toString();
            String user_bio=i.getExtras().get("user_bio").toString();

            tv_name.setText(user_name);
            tv_email.setText(user_email);
            tv_location.setText(user_location);
            tv_bio.setText(user_bio);
        }
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(getApplicationContext(),EditProfile.class);
                i.putExtra("user_name", tv_name.getText().toString() );
                i.putExtra("user_email", tv_email.getText().toString() );
                i.putExtra("user_location", tv_location.getText().toString() );
                i.putExtra("user_bio", tv_bio.getText().toString() );

                startActivity(i);
            }
        });


    }


}
