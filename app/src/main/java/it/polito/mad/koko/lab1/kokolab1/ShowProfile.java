package it.polito.mad.koko.lab1.kokolab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.Preferences;


public class ShowProfile extends AppCompatActivity {

   /* String saved_user_name;
    String saved_user_email;
    String saved_user_location;
    String saved_user_bio;*/
    private static String user_photo_uri=null;
    private TextView tv_name;
    private TextView tv_email;
    private TextView tv_location;
    private TextView tv_bio;
    private String MY_PREFS_NAME="MySharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_profile);

        tv_name=findViewById(R.id.user_name);
        tv_email=findViewById(R.id.user_email);
        tv_location=findViewById(R.id.user_location);
        tv_bio=findViewById(R.id.user_bio);

        /*if(savedInstanceState!=null) {

            Log.d("debug",savedInstanceState.toString());
            tv_name.setText(savedInstanceState.getString("user_name"));
            tv_email.setText(savedInstanceState.getString("user_email"));
            tv_location.setText(savedInstanceState.getString("user_location"));
            tv_bio.setText(savedInstanceState.getString("user_bio"));

            if(user_photo_uri!=null){
                Picasso.get().load(user_photo_uri).fit().centerCrop().into((ImageView)findViewById(R.id.user_photo));
            }
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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

        tv_name.setText(savedInstanceState.getString("user_name"));
        tv_email.setText(savedInstanceState.getString("user_email"));
        tv_location.setText(savedInstanceState.getString("user_location"));
        tv_bio.setText(savedInstanceState.getString("user_bio"));

        if(savedInstanceState.getString("user_photo")!=null){
            Picasso.get().load(savedInstanceState.getString("user_photo")).fit().centerCrop().into((ImageView)findViewById(R.id.user_photo));
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

            tv_name.setText(user_name);
            tv_email.setText(user_email);
            tv_location.setText(user_location);
            tv_bio.setText(user_bio);

            if(data.getStringExtra("uri")!=null) {
                user_photo_uri=data.getStringExtra("uri");
                ImageView user_photo = findViewById(R.id.user_photo);
                Picasso.get().load(user_photo_uri).fit().centerCrop().into(user_photo);
            }
        }
    }

    /**
     * Ho inserito il menu nella barra superiore
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent i = new Intent(getApplicationContext(), EditProfile.class);
                i.putExtra("user_name", tv_name.getText().toString());
                i.putExtra("user_email", tv_email.getText().toString());
                i.putExtra("user_location", tv_location.getText().toString());
                i.putExtra("user_bio", tv_bio.getText().toString());

                if (user_photo_uri != null) {
                    i.putExtra("user_photo_uri", user_photo_uri);
                }

                startActivityForResult(i, 1);
            default:
                return super.onOptionsItemSelected(item);

        }
    }

   /* @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("user_name", tv_name.getText().toString());
        editor.putString("user_email", tv_email.getText().toString());
        editor.putString("user_location", tv_location.getText().toString());
        editor.putString("user_bio", tv_bio.getText().toString());

        if(user_photo_uri!=null)
            editor.putString("user_photo",user_photo_uri);
        editor.commit();

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);

        tv_name.setText(sharedPreferences.getString("user_name",null));
        tv_email.setText(sharedPreferences.getString("user_email",null));
        tv_location.setText(sharedPreferences.getString("user_location",null));
        tv_bio.setText(sharedPreferences.getString("user_bio",null));

        ImageView user_photo = findViewById(R.id.user_photo);

        if(sharedPreferences.getString("user_photo",null)==null){
            Picasso.get().load(R.mipmap.ic_launcher_round).fit().centerCrop().into(user_photo);
        }

        else
            Picasso.get().load(sharedPreferences.getString("user_photo",null)).fit().centerCrop().into(user_photo);


    }


}
