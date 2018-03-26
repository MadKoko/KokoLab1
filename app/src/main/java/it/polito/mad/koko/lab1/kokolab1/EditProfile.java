package it.polito.mad.koko.lab1.kokolab1;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditProfile extends AppCompatActivity{

    private static final int GALLERY = 1;
    private static final int CAMERA_REQUEST = 0;
    private String user_photo_profile;

    private String MY_PREFS_NAME="MySharedPreferences";

    private EditText et_name;
    private EditText et_email;
    private EditText et_location;
    private EditText et_bio;
    private ImageView user_photo;
    private SharedPreferences sharedPreferences;

    /**
     * IN ONCREATE METHOD WE SET THE EDIT TEXT AND CREATE THE CONTEXT.
     * WE TAKE THE CURRENT PICTURE SHOWN IN SHOWPROFILE FROM THE FIELD OF SHARED PREFERENCES "USER_PHOTO_TEMP"
     * WE SET THE BUTTON "USER_PHOTO_BUTTON" TO TAKE PICTURES , CHOOSING FROM CAMERA OR GALLERY
     * WE SET THE SAVE BUTTON TO SAVE THE CURRENT MODIFICATIONS IN THE SHARED PREFERENCES
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d("debug","onCreate");

        setContentView(R.layout.activity_edit_profile);

        et_name=findViewById(R.id.edit_user_name);
        et_email=findViewById(R.id.edit_user_email);
        et_location=findViewById(R.id.edit_user_location);
        et_bio=findViewById(R.id.edit_user_bio);

        sharedPreferences=getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);

        et_name.setText(sharedPreferences.getString("user_name",null));
        et_email.setText(sharedPreferences.getString("user_email",null));
        et_location.setText(sharedPreferences.getString("user_location",null));
        et_bio.setText(sharedPreferences.getString("user_bio",null));

        user_photo= findViewById(R.id.user_photo);

        ImageButton user_photo_button = findViewById(R.id.user_photo_button);
        user_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog();
            }
        });

        Button save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("user_name", et_name.getText().toString());
                editor.putString("user_email", et_email.getText().toString());
                editor.putString("user_location", et_location.getText().toString());
                editor.putString("user_bio", et_bio.getText().toString());


                editor.putString("user_photo",sharedPreferences.getString("user_photo_temp",null));
                editor.apply();

                finish();

            }
        });

    }


    /**
     * STARTDIALOG() METHOD CREATE AN ALERTDIALOG TO CHOOSE CAMERA OR GALLERY TO TAKE PICTURES
     * IF GALLERY: THE IMAGE IS CHOSEN FROM THE ONES SAVED LOCALLY ON THE MOBILE
     * IF CAMERA: A NEW FILE IS CREATED AND THE PICTURE IS SAVED THERE
     *
     */
    private void startDialog() {
        Log.d("debug","startDialog");
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Select Profile Picture");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent pictureActionIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(pictureActionIntent, GALLERY);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f=null;
                        try{

                        f = createImageFile();

                        } catch (IOException ex){
                            Log.d("debug",ex.getMessage());
                        }

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                        startActivityForResult(intent, CAMERA_REQUEST);


                    }
                });
        myAlertDialog.show();
    }

    /**
     *ONACTIVITYRESULT() METHOD IS CALLED AS THE RESULT OF THE CHOSEN ACTIVITY TO TAKE THE PICTURE
     * IF GALLERY: THE DATA IS TAKEN FROM THE INTENT AND SET IN THE SHARED PREFERENCES WITH THE KEY "USER_PHOTO_TEMP"
     * IF CAMERA: THE URI IS TAKEN FROM THE VARIABLE "USER_PHOTO_PROFILE", WHICH IS SET IN THE METHOD "CREATEIMAGEFILE()" AND SET THE SHARED PREFERENCES WITH THE KEY "USER_PHOTO_TEMP"
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("debug","onActivityResult");

        sharedPreferences=getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (requestCode == GALLERY && resultCode != 0) {

            editor.putString("user_photo_temp",data.getData().toString());
            editor.apply();

        }
        if (requestCode == CAMERA_REQUEST && resultCode != 0) {

            editor.putString("user_photo_temp",user_photo_profile);
            Log.d("debug",user_photo_profile);
            editor.apply();


        }

    }

    /**
     * ONRESUME() METHOD IS USED TO SET ALL THE VALUES IN THE RIGHT FIELDS, TAKEN FROM SHARED PREFERENCES
     */

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "onResume");

        et_name.setText(sharedPreferences.getString("user_name", null));
        et_email.setText(sharedPreferences.getString("user_email", null));
        et_location.setText(sharedPreferences.getString("user_location", null));
        et_bio.setText(sharedPreferences.getString("user_bio", null));

        sharedPreferences=getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);

        Picasso.get().load(sharedPreferences.getString("user_photo_temp", null)).fit().centerCrop().into(user_photo);


    }


    /**
     *CREATEIMAGEFILE() METHOD CREATES THE FILE WHERE THE IMAGES TAKEN FROM CAMERA WILL BE STORED.
     * THE NAME IS COMPOSED BY "JPEG + TIMESTAMP + _"
     * THE URI IS SAVED IN THE VARIABLE USER_PHOTO_PROFILE
     */
    private File createImageFile() throws IOException {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="JPEG"+timeStamp+"_";
        File storgeDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storgeDir);
        user_photo_profile="file:"+image.getAbsolutePath();
        Log.d("debug",user_photo_profile);
        return image;
    }


}
