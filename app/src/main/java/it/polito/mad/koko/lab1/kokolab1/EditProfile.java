package it.polito.mad.koko.lab1.kokolab1;

/**
 * Created by Francesco on 18/03/2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.getActivity;
import static android.hardware.SensorManager.getOrientation;

public class EditProfile extends AppCompatActivity{

    private  Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static final int GALLERY = 1;
    private static final int CAMERA_REQUEST = 0;
    private String mCurrentPhotoPath;

    private String MY_PREFS_NAME="MySharedPreferences";

    private EditText et_name;
    private EditText et_email;
    private EditText et_location;
    private EditText et_bio;
    private ImageView user_photo;
    private SharedPreferences sharedPreferences;

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

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                                Log.d("debug",ex.getMessage());
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Log.d("debug",Uri.fromFile(photoFile).toString());
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile).toString());
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                            }
                        }


                    }
                });
        myAlertDialog.show();
    }

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

            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                user_photo.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

            editor.putString("user_photo_temp",data.getStringExtra("photo_path"));
            editor.apply();


        }

    }

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


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}
