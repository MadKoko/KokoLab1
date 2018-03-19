package it.polito.mad.koko.lab1.kokolab1;

/**
 * Created by Francesco on 18/03/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.hardware.SensorManager.getOrientation;

public class EditProfile extends AppCompatActivity{

   /* private EditText et_name;
    private EditText et_email;
    private EditText et_location;
    private EditText et_bio;
*/
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static final int GALLERY = 1;
    private static Uri myImageUri;

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
        }

        ImageButton user_photo_button = findViewById(R.id.user_photo_button);
        user_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Image != null)
                    Image.recycle();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);

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
                i.putExtra("uri", myImageUri.toString());
                setResult(RESULT_OK, i);

                finish();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode != 0) {
            myImageUri = data.getData();
            try {
                Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myImageUri);
                ImageView user_photo=findViewById(R.id.user_photo);
                user_photo.setImageBitmap(Image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user_name",et_name.getText().toString());
        outState.putString("user_email", et_email.getText().toString());
        outState.putString("user_location", et_location.getText().toString());
        outState.putString("user_bio", et_bio.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        et_name.setText(savedInstanceState.getString("user_name"));
        et_email.setText(savedInstanceState.getString("user_email"));
        et_location.setText(savedInstanceState.getString("user_location"));
        et_bio.setText(savedInstanceState.getString("user_bio"));

    }*/
}
