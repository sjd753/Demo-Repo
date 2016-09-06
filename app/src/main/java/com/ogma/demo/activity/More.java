package com.ogma.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ogma.demo.R;
import com.ogma.demo.utility.BitmapDecoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class More extends AppCompatActivity {

    public static final String EXTRA_TEXT = "extra_text";
    public static final String[] items = {"Select an option", "Take Photo", "Send Email", "Open Browser"};
    private static final int REQUEST_TAKE_PHOTO = 133;
    private String mCapturedPhotoPath = "";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        //Check if there is an extra data incoming
        if (getIntent().getStringExtra(EXTRA_TEXT) != null) {
            //If it's coming with the name extra_text get it
            String text = getIntent().getStringExtra(EXTRA_TEXT);
            TextView textView = (TextView) findViewById(R.id.tv_extra_text);
            textView.setText(text);
        }

        imageView = (ImageView) findViewById(R.id.iv_demo);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //On item selected listener for the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //when an otem is selected onItemSelected will be called
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Since we know there will only be three items and they are static
                switch (position) {
                    case 1:
                        dispatchTakePictureIntent();
                        break;
                    case 2:
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@domain.com");
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email text here");
                        //Checking if there is an app that can handle this intent
                        if (emailIntent.resolveActivity(getPackageManager()) != null)
                            startActivity(Intent.createChooser(emailIntent, "Send Email"));
                        else
                            Toast.makeText(More.this, "No app installed to send an email", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.co.in"));
                        //Checking if there is an app that can handle this intent
                        if (browserIntent.resolveActivity(getPackageManager()) != null)
                            startActivity(Intent.createChooser(browserIntent, "View site"));
                        else
                            Toast.makeText(More.this, "No app installed to view this site", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Create the adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //Set the adapter to the spinner yahooooooooo ;)
        spinner.setAdapter(adapter);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCapturedPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File saveCompressedBitmap(Bitmap bitmap, String path) {
        File imageFile = new File(path);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                //Use to view the captured image in gallery app
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(mCapturedPhotoPath), "image/*");
//                startActivity(intent);

                Log.e("Captured Size:", new File(mCapturedPhotoPath).length() / 1024 + "");
                Bitmap bitmap = BitmapDecoder.decodeBitmapFromFile(mCapturedPhotoPath);
                saveCompressedBitmap(bitmap, mCapturedPhotoPath);
                Log.e("Compressed Size:", new File(mCapturedPhotoPath).length() / 1024 + "");
                preview(mCapturedPhotoPath);
            } else {
                if (new File(mCapturedPhotoPath).delete()) {
                    Log.e("File Operation", "File deleted successfully!");
                } else {
                    Log.e("File Operation", "Unable to delete file!");
                }
            }
        }
    }

    private void preview(String filePath) {
        Bitmap bitmap = BitmapDecoder.decodeSampledBitmapFromFile(filePath, 600, 480);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
