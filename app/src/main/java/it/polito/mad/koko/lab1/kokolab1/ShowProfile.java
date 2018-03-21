package it.polito.mad.koko.lab1.kokolab1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;


public class ShowProfile extends AppCompatActivity {

   /* String saved_user_name;
    String saved_user_email;
    String saved_user_location;
    String saved_user_bio;*/
    private static String user_photo_uri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_profile);

        Button button=findViewById(R.id.edit_button);

        final TextView tv_name;
        final TextView tv_email;
        final TextView tv_location;
        final TextView tv_bio;

        tv_name=findViewById(R.id.user_name);
        tv_email=findViewById(R.id.user_email);
        tv_location=findViewById(R.id.user_location);
        tv_bio=findViewById(R.id.user_bio);

        if(savedInstanceState!=null) {
            tv_name.setText(savedInstanceState.getString("user_name"));
            tv_email.setText(savedInstanceState.getString("user_email"));
            tv_location.setText(savedInstanceState.getString("user_location"));
            tv_bio.setText(savedInstanceState.getString("user_bio"));

            if(user_photo_uri!=null){
                Picasso.get().load(user_photo_uri).into((ImageView)findViewById(R.id.user_photo));
            }

        }

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),EditProfile.class);
                i.putExtra("user_name", tv_name.getText().toString() );
                i.putExtra("user_email", tv_email.getText().toString() );
                i.putExtra("user_location", tv_location.getText().toString() );
                i.putExtra("user_bio", tv_bio.getText().toString() );

                if(user_photo_uri!=null){
                    i.putExtra("user_photo_uri",user_photo_uri);
                }

                startActivityForResult(i,1);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView tv_name=findViewById(R.id.user_name);
        TextView tv_email=findViewById(R.id.user_email);
        TextView tv_location=findViewById(R.id.user_location);
        TextView tv_bio=findViewById(R.id.user_bio);

        outState.putString("user_name",tv_name.getText().toString());
        outState.putString("user_email", tv_email.getText().toString());
        outState.putString("user_location", tv_location.getText().toString());
        outState.putString("user_bio", tv_bio.getText().toString());

        if(user_photo_uri!=null){
            outState.putString("user_photo",user_photo_uri);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView tv_name=findViewById(R.id.user_name);
        TextView tv_email=findViewById(R.id.user_email);
        TextView tv_location=findViewById(R.id.user_location);
        TextView tv_bio=findViewById(R.id.user_bio);

        tv_name.setText(savedInstanceState.getString("user_name"));
        tv_email.setText(savedInstanceState.getString("user_email"));
        tv_location.setText(savedInstanceState.getString("user_location"));
        tv_bio.setText(savedInstanceState.getString("user_bio"));

        if(savedInstanceState.getString("user_photo")!=null){
            Picasso.get().load(savedInstanceState.getString("user_photo")).into((ImageView)findViewById(R.id.user_photo));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1&&resultCode == RESULT_OK) {

            String user_name=data.getStringExtra("user_name");
            String user_email=data.getStringExtra("user_email");
            String user_location=data.getStringExtra("user_location");
            String user_bio=data.getStringExtra("user_bio");

            TextView tv_name=findViewById(R.id.user_name);
            TextView tv_email=findViewById(R.id.user_email);
            TextView tv_location=findViewById(R.id.user_location);
            TextView tv_bio=findViewById(R.id.user_bio);

            tv_name.setText(user_name);
            tv_email.setText(user_email);
            tv_location.setText(user_location);
            tv_bio.setText(user_bio);

            if(data.getStringExtra("uri")!=null) {
                user_photo_uri=data.getStringExtra("uri");
                ImageView user_photo = findViewById(R.id.user_photo);
                Picasso.get().load(user_photo_uri).into(user_photo);
            }


        }

    }
}
