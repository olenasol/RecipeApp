package com.example.olena.recipeapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.models.Recipe;
import com.facebook.Profile;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;

public class AddRecipeActivity extends AppCompatActivity {

    private ImageButton selectImageBtn;
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private Uri imageUri;
    private EditText firstIngredientEdit;
    private ImageButton firstAddButton;
    private EditText titleEditTxt;
    private Button submitBtn;
    private ArrayList<EditText> listOfEditTexts = new ArrayList<>();
    String mCurrentPhotoPath;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        selectImageBtn = findViewById(R.id.imageBtn);
        titleEditTxt = findViewById(R.id.titleEdit);
        firstIngredientEdit = findViewById(R.id.ingr1Text);
        firstAddButton = findViewById(R.id.add1Btn);
        submitBtn = findViewById(R.id.submitBtn);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog();
            }
        });
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addNewEditText();

            }
        };
        firstAddButton.setOnClickListener(onClickListener);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe();
            }
        });

    }
    private void addRecipe(){
        Recipe recipe = new Recipe("0", titleEditTxt.getText().toString(), Profile.getCurrentProfile().getName(), imageUri.toString(), "", 0);
        recipe.setListOfIngredients(getIngredients());
        Intent intent = new Intent(AddRecipeActivity.this,MainNavActivity.class);
        intent.putExtra("new_recipe",recipe);
        startActivity(intent);
    }
    private void createAlertDialog(){
        final AlertDialog alertDialog = getDialogBuilder().create();
        alertDialog.show();
        ImageButton galleryImageBtn = alertDialog.findViewById(R.id.openGalleryBtn);
        galleryImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageGallery();
                alertDialog.hide();
            }
        });
        ImageButton cameraImageBtn = alertDialog.findViewById(R.id.openCameraBtn);
        cameraImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCamera();
                alertDialog.hide();
            }
        });
    }

    private void addNewEditText() {
        LinearLayout linearLayout = findViewById(R.id.ingrLayout);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout horLinearLayout = new LinearLayout(this);
        horLinearLayout.setLayoutParams(params);
        horLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(horLinearLayout);
        EditText editText = new EditText(this);
        params = new ViewGroup.LayoutParams(680, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        editText.setBackgroundResource(R.drawable.rounded_edittext);
        editText.setPadding(16, 16, 16, 16);
        editText.requestFocus();
        listOfEditTexts.add(editText);
        ImageButton imageButton = new ImageButton(this);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButton.setLayoutParams(params);
        imageButton.setImageResource(R.mipmap.ic_add_black_24dp);
        imageButton.setPadding(16, 16, 16, 16);
        imageButton.setBackgroundColor(Color.WHITE);
        imageButton.setOnClickListener(onClickListener);
        horLinearLayout.addView(editText);
        horLinearLayout.addView(imageButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == GALLERY_REQUEST) && resultCode == RESULT_OK) {
            imageUri = data.getData();

            selectImageBtn.setImageURI(imageUri);
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            selectImageBtn.setImageURI(imageUri);
        }
        selectImageBtn.setBackgroundColor(Color.WHITE);

    }

    private void getImageGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    private void getImageCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private AlertDialog.Builder getDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
        LayoutInflater inflater = AddRecipeActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setTitle("Choose image");
        builder.setView(dialogView);
        builder.setCancelable(true);
        return builder;
    }

    private ArrayList<String> getIngredients(){
        ArrayList<String> listOfIngredients = new ArrayList<>();
        listOfIngredients.add(firstIngredientEdit.getText().toString());
        for(int i=0;i<listOfEditTexts.size();i++){
            if(!listOfEditTexts.get(i).getText().toString().equals("")) {
                listOfIngredients.add(listOfEditTexts.get(i).getText().toString());
            }
        }
        return listOfIngredients;
    }
}


