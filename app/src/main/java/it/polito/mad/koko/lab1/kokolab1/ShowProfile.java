package it.polito.mad.koko.lab1.kokolab1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowProfile extends AppCompatActivity {

    private static String user_photo_uri=null;
    private TextView tv_name;
    private TextView tv_email;
    private TextView tv_location;
    private TextView tv_bio;
    private ImageView user_photo;
    private String MY_PREFS_NAME="MySharedPreferences";


    /**
     * ONCREATE() METHOD CREATES THE TEXT VIEWS AND SET THE CONTENT VIEW
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_profile);

        tv_name=findViewById(R.id.user_name);
        tv_email=findViewById(R.id.user_email);
        tv_location=findViewById(R.id.user_location);
        tv_bio=findViewById(R.id.user_bio);

        user_photo=findViewById(R.id.user_photo);

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
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (user_photo_uri != null) {
                    editor.putString("user_photo_temp", user_photo_uri);
                }
                else{
                    editor.putString("user_photo_temp", String.valueOf(R.mipmap.ic_launcher_round));
                }
                editor.apply();
                startActivity(i);
               // startActivityForResult(i, 1);
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    /**
     * ONRESUME() METHOD SET ALL THE FIELDS WITH VALUES TAKEN FROM SHARED PREFERENCES
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);

        tv_name.setText(sharedPreferences.getString("user_name",null));
        tv_email.setText(sharedPreferences.getString("user_email",null));
        tv_location.setText(sharedPreferences.getString("user_location",null));
        tv_bio.setText(sharedPreferences.getString("user_bio",null));

        if(sharedPreferences.getString("user_photo",null)==null){
            Picasso.get().load(R.mipmap.ic_launcher_round).fit().centerCrop().into(user_photo);
        }

        else {
            user_photo_uri=sharedPreferences.getString("user_photo", null);
            Picasso.get().load(sharedPreferences.getString("user_photo", null)).fit().centerCrop().into(user_photo);
        }


    }

    /**
     * ONPAUSE() METHOD SAVES ALL THE CURRENT VALUES IN FIELDS IN SHARED PREFERENCES
     */

   @Override
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
        editor.apply();

    }

}
