package com.aloy.ClassiFish.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aloy.ClassiFish.Classification.Classify;
import com.aloy.ClassiFish.R;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.theartofdev.edmodo.cropper.CropImage;


public class Selection extends AppCompatActivity {

    //Declare URI
    Uri mImageUri;

    //Declare Camera Button
    ImageView toPhotoSelection;
    ImageView toPhotoSelection2;

    //Declare Youtube Tutorial Button
    ImageView youtubeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        //initialise buttons
        toPhotoSelection = (ImageView) findViewById(R.id.toCameraBtn);
        toPhotoSelection2 = (ImageView) findViewById(R.id.toCameraBtn2);
        youtubeButton = (ImageView) findViewById(R.id.youtubeTutorial);

        //create animation using YOYO external library for magnifying glass
        YoYo.with(Techniques.Wobble)
                .duration(10000)
                .repeat(-1)
                .playOn(findViewById(R.id.toCameraBtn2));

        //create animation using Android's inbuilt anim library for rotation
        toPhotoSelection.startAnimation(
                AnimationUtils.loadAnimation(Selection.this, R.anim.rotation) );

        //set onClickListener for button to open choose image
        toPhotoSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseFile(v);
            }
        });

        //set onClickListener for youtube tutorial
        youtubeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://youtu.be/yFeqrdbBPqw")));
                Log.i("Video", "Video Playing....");

            }
        });

    }


    //choose image class
    public void onChooseFile(View v) {

        //start CropImage activity
        CropImage.activity().start(Selection.this);
    }


    //CropImage class
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get CropImage request code
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            //check if no errors
            if(resultCode == RESULT_OK) {

                //get chosen image URI
                mImageUri = result.getUri();

                // create an intent to linking to classification page
                Intent intent = new Intent(Selection.this, Classify.class);

                // put image data in extras to send
                intent.putExtra("resID_uri", mImageUri);

                // put tflitefile in extras
                String chosenModel = "fish_species_InceptionV3.tflite";
                intent.putExtra("chosen", chosenModel);

                // put model type (quantised or not) in extras (currently only one unquantised)
                boolean quant = false;
                intent.putExtra("quant", quant);

                // start intent
                startActivity(intent);
            }

            //on error
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                //get error and create message
                Exception e  = result.getError();
                Toast.makeText(this,"Possible Error: " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

}