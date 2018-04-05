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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EditProfile extends AppCompatActivity{

    /**
     * Profile pic source
     */
    private static final int    CAMERA_REQUEST = 0,
                                GALLERY = 1;
    /**
     * Temporary profile pic file name
     */
    private static final String TEMP_PROFILE_PIC_NAME = "temp_profile_pic.png",
                                DEFINITIVE_PROFILE_PIC_NAME = "definitive_profile_pic.png";
    /**
     * Temporary profile pic URI
     */
    private String temp_profile_pic_URI;

    /**
     * Definitive profile pic URI
     */
    private String definitive_profile_pic_URI;

    /**
     * User profile data is stored in a shared XML file.
     */
    private String MY_PREFS_NAME = "MySharedPreferences";
    private SharedPreferences sharedPreferences;

    /**
     * User profile data.
     */
    private EditText et_name;
    private EditText et_password;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_location;
    private EditText et_bio;
    private ImageView user_photo;

    /**
     * Filling all the UI text fields and the user profile pic with all the
     * previous values shown in the ShowProfile activity.
     * It also adds an edit profile pic button and the save button to save
     * the current modifications in the sharedPreferences XML file.
     * @param savedInstanceState    activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Debugging
        Log.d("debug","onCreate()");

        // Loading the XML layout file
        setContentView(R.layout.activity_edit_profile);

        // Restoring UI fields containing user info
        et_name=findViewById(R.id.edit_user_name);
        et_password=findViewById(R.id.edit_user_password);
        et_email=findViewById(R.id.edit_user_email);
        et_phone=findViewById(R.id.edit_user_phone);
        et_location=findViewById(R.id.edit_user_location);
        et_bio=findViewById(R.id.edit_user_bio);

        // Restore the profile pic from the sharedPreferences data structure
        sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        user_photo = findViewById(R.id.user_photo);
        String definitiveProfilePic = sharedPreferences.getString("user_photo", null);
        if(definitiveProfilePic != null)
            Picasso.get().load(definitiveProfilePic).fit().centerCrop().into(user_photo);
        else
            user_photo.setImageURI(null);

        // TODO debugging
        Log.d("DEBUG", "Default pic: " + String.valueOf(R.drawable.ic_launcher_background));

        // Edit profile pic button
        ImageButton user_photo_button = findViewById(R.id.user_photo_button);
        user_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog();
            }
        });

        // Save button
        Button save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener((View view) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Making the temporary profile picture definitive
            try {
                // Obtaining source file
                File tmpFile = getFile(TEMP_PROFILE_PIC_NAME, false);

                // If there is a temporary profile pic
                if(tmpFile != null) {
                    // Obtaining destination file
                    File defFile = getFile(DEFINITIVE_PROFILE_PIC_NAME, true);

                    // Creating the definite image file copy
                    copyFile(tmpFile, defFile);

                    if(defFile != null)
                        // Setting the definitive profile pic's URI
                        definitive_profile_pic_URI = "file:" + defFile.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Saving all UI fields values in the sharedPreferences XML file.
            editor.putString("user_name", et_name.getText().toString());
            editor.putString("user_password", et_password.getText().toString());
            editor.putString("user_email", et_email.getText().toString());
            editor.putString("user_phone", et_phone.getText().toString());
            editor.putString("user_location", et_location.getText().toString());
            editor.putString("user_bio", et_bio.getText().toString());
            editor.putString("user_photo", definitive_profile_pic_URI);
            editor.apply();

            // Terminating the activity
            finish();
        });
    }

    /**
     * It displays an alert dialog by which the user can choose the camera or the gallery
     * to take his/her new profile pic.
     */
    private void startDialog() {
        // TODO debugging
        Log.d("debug","startDialog()");

        // Alert dialog showing the two possibilities: camera or gallery
        // TODO Implement a context-menu instead, with custom style
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Select Profile Picture");

        // 'From gallery' option
        myAlertDialog.setPositiveButton("Gallery",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                Intent pictureActionIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                // Launching the camera app
                startActivityForResult(pictureActionIntent, GALLERY);
            }
        });

        // 'From camera' option
        myAlertDialog.setNegativeButton("Camera",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                // Requested from Android 7.0 Nougat
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                // Intent for the camera app
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Saving the image file into the file system
                File imageFile = getFile(TEMP_PROFILE_PIC_NAME, true);

                // Saving the new profile pic
                temp_profile_pic_URI = "file:" + imageFile.getAbsolutePath();

                // The image file couldn't be created
                if(imageFile == null)
                    // The camera app won't be launched
                    return;

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));

                // Launching the camera app
                startActivityForResult(intent, CAMERA_REQUEST);
                }
            });

        // Showing the alert dialog
        myAlertDialog.show();
    }

    /**
     * File copying utility function.
     * @param src                       file to be copied.
     * @param dst                       copy destination.
     * @throws IOException              in case it's not possible to copy the file
     *                                  in the destination folder.
     */
    public void copyFile(File src, File dst)  throws IOException {
        // TODO Debugging
        Log.d("debug","copyFile()");

        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    /**
     * Returns a file descriptor given its name.
     * @param fileName              the file name whose descriptor
     *                              should be returned.
     * @param createIfNotExists     if true, the file will be created
     *                              if it does not exist already
     * @return                      the file descriptor.
     */
    private File getFile(String fileName, boolean createIfNotExists) {
        // TODO Debugging
        Log.d("debug","getFile(" + fileName + ")");

        // Pictures directory
        File picturesDir = getPicturesDir();

        // Tempting to open the given file given its name
        File file = new File(picturesDir + "/" + fileName);

        // TODO debugging
        Log.d("debug", "Trying to open file " + picturesDir + "/" + fileName);

        // Stores the outcome of the file creation operation
        boolean fileCreated = false;

        // Checking if the file already exists
        if(!file.exists()) {
            // The file has to be created if it does not exist already
            if(createIfNotExists) {
                /*
                * createNewFile() method is used to create a new, empty file
                * mentioned by given abstract pathname if and only if a file with
                * this name does not exist in given abstract pathname.
                */
                try {
                    // TODO debugging
                    Log.d("DEBUG", "Creating new file...");

                    fileCreated = file.createNewFile();
                } catch (IOException e) {
                    // Creating an alert dialog indicating all possible causes
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                    builder .setMessage(R.string.image_file_creation_error_message)
                            .setTitle(R.string.image_file_creation_error_title)
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    // Showing the dialog to the screen
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return null;
                }
            }
            else {
                // TODO debugging
                Log.d("debug", "The file does not exist and it does not have to be created");

                return null;
            }
        }

        if(fileCreated)
            // TODO debugging
            Log.d("debug", "Empty File successfully created");
        else
            // TODO debugging
            Log.d("debug", "File already existing");

        return file;
    }

    /**
     * Deletes the specified file, if it exists.
     * @param fileName  the file name to be deleted.
     * @return          whether the file has been deleted or not.
     */
    private boolean delFile(String fileName) {
        // TODO Debugging
        Log.d("debug","delFile(" + fileName + ")");

        File file = getFile(fileName, false);

        if(file != null)
            return file.delete();
        else
            return true;
    }

    /**
     * Returns the default Pictures directory.
     * @return  the Pictures directory location.
     */
    private File getPicturesDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    /**
     * Called as a result of the new profile picture activity, whether it is taken from
     * the camera or from the gallery.
     * @param requestCode   where the profile pic has been acquired.
     * @param resultCode    whether the operation has been performed successfully or not.
     * @param data          an Intent that carries the result data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO debugging
        Log.d("debug","onActivityResult()");

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // If the photo has been picked from the gallery
        if(requestCode == GALLERY && resultCode != RESULT_CANCELED) {
            editor.putString("user_photo_temp", data.getData().toString());
            editor.apply();

            // Restoring the profile picture
            Picasso.get().load(sharedPreferences.getString("user_photo_temp", null)).fit().centerCrop().into(user_photo);
        }

        // If the photo has been taken with the camera
        if(requestCode == CAMERA_REQUEST && resultCode != RESULT_CANCELED) {
            editor.putString("user_photo_temp", temp_profile_pic_URI);

            // TODO debugging
            Log.d("debug", temp_profile_pic_URI);

            editor.apply();
            // Restoring the profile picture
            Picasso.get().load(sharedPreferences.getString("user_photo_temp", null)).fit().centerCrop().into(user_photo);

        }

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_CANCELED){
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        // TODO Debugging
        Log.d("debug","onPause()");

        // Invalidating the temporary profile pic cache
        Picasso.get().invalidate(sharedPreferences.getString("user_photo_temp", null));
    }

    /**
     * Filling all the UI fields retrieving all the needed information from the
     * sharedPreferences XML file.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // TODO debugging
        Log.d("debug", "onResume()");

        // Updating the sharedPreferences data structure containing user info
        sharedPreferences=getApplicationContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);

        // Restoring all UI values
        et_name.setText(sharedPreferences.getString("user_name", null));
        et_password.setText(sharedPreferences.getString("user_password", null));
        et_email.setText(sharedPreferences.getString("user_email", null));
        et_phone.setText(sharedPreferences.getString("user_phone", null));
        et_location.setText(sharedPreferences.getString("user_location", null));
        et_bio.setText(sharedPreferences.getString("user_bio", null));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // TODO debugging
        Log.d("debug", "onDestroy()");

        // Deleting the temporary profile pic image file
        delFile(TEMP_PROFILE_PIC_NAME);

        // Deleting the temporary profile pic property
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_photo_temp", null);
        editor.apply();
    }
}
