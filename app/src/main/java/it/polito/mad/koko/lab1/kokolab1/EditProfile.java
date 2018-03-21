package it.polito.mad.koko.lab1.kokolab1;

/**
 * Created by Francesco on 18/03/2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static android.app.PendingIntent.getActivity;
import static android.hardware.SensorManager.getOrientation;

public class EditProfile extends AppCompatActivity{

    // Test Marco

   /* private EditText et_name;
    private EditText et_email;
    private EditText et_location;
    private EditText et_bio;
*/
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static final int GALLERY = 1;
    private static final int CAMERA_REQUEST = 0;
    private static Uri myImageUri;
    private static String user_photo_profile=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        Intent i=getIntent();

        final EditText et_name=findViewById(R.id.edit_user_name);
        final EditText et_email=findViewById(R.id.edit_user_email);
        final EditText et_location=findViewById(R.id.edit_user_location);
        final EditText et_bio=findViewById(R.id.edit_user_bio);

        if(i.getExtras()!=null){
            String user_name=i.getStringExtra("user_name");
            String user_email=i.getStringExtra("user_email");
            String user_location=i.getStringExtra("user_location");
            String user_bio=i.getStringExtra("user_bio");

            et_name.setText(user_name);
            et_email.setText(user_email);
            et_location.setText(user_location);
            et_bio.setText(user_bio);

            if(i.getStringExtra("user_photo_uri")!=null){
                String user_photo_uri=i.getStringExtra("user_photo_uri");
                ImageView user_photo = (ImageView) findViewById(R.id.user_photo);
                Picasso.get().load(myImageUri).into(user_photo);
            }
        }

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
                // TODO Auto-generated method stub

                Intent i = new Intent();
                i.putExtra("user_name", et_name.getText().toString() );
                i.putExtra("user_email", et_email.getText().toString() );
                i.putExtra("user_location", et_location.getText().toString() );
                i.putExtra("user_bio", et_bio.getText().toString() );

                if(user_photo_profile!=null) {
                    i.putExtra("uri", myImageUri.toString());
                }
                setResult(RESULT_OK, i);

                finish();

            }
        });

    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Select Profile Picture");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent pictureActionIntent = null;
                        pictureActionIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pictureActionIntent, GALLERY);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                        startActivityForResult(intent, CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode != 0) {
            myImageUri = data.getData();
            ImageView user_photo=findViewById(R.id.user_photo);
            Picasso.get().load(myImageUri).into(user_photo);
            user_photo_profile=myImageUri.toString();

        }
        if (requestCode == CAMERA_REQUEST && resultCode != 0) {

            Bitmap bitmap = null;
            String selectedImagePath = null;

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(getBaseContext(),

                        "Error while capturing image", Toast.LENGTH_LONG)

                        .show();

                return;

            }

            myImageUri=Uri.fromFile(f);

            ImageView user_photo=findViewById(R.id.user_photo);
            Picasso.get().load(myImageUri).into(user_photo);
            user_photo_profile=myImageUri.toString();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView tv_name=findViewById(R.id.edit_user_name);
        TextView tv_email=findViewById(R.id.edit_user_email);
        TextView tv_location=findViewById(R.id.edit_user_location);
        TextView tv_bio=findViewById(R.id.edit_user_bio);

        outState.putString("user_name",tv_name.getText().toString());
        outState.putString("user_email", tv_email.getText().toString());
        outState.putString("user_location", tv_location.getText().toString());
        outState.putString("user_bio", tv_bio.getText().toString());

        if(user_photo_profile!=null){
            outState.putString("user_photo",myImageUri.toString());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView tv_name=findViewById(R.id.edit_user_name);
        TextView tv_email=findViewById(R.id.edit_user_email);
        TextView tv_location=findViewById(R.id.edit_user_location);
        TextView tv_bio=findViewById(R.id.edit_user_bio);

        tv_name.setText(savedInstanceState.getString("user_name"));
        tv_email.setText(savedInstanceState.getString("user_email"));
        tv_location.setText(savedInstanceState.getString("user_location"));
        tv_bio.setText(savedInstanceState.getString("user_bio"));

        if(savedInstanceState.getString("user_photo")!=null){
            ImageView user_photo = (ImageView) findViewById(R.id.user_photo);
            Picasso.get().load(savedInstanceState.getString("user_photo")).into(user_photo);
        }
    }

}
