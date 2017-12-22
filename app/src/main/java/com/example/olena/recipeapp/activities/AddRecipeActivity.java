package com.example.olena.recipeapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.olena.recipeapp.R;
import com.example.olena.recipeapp.models.Recipe;
import com.facebook.Profile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AddRecipeActivity extends AppCompatActivity {

    private ImageButton selectImageBtn;
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private Uri imageUri;
    private EditText firstIngredientEdit;
    private EditText titleEditTxt;
    private ArrayList<EditText> listOfEditTexts;
    private View.OnClickListener onClickListener;

    public AddRecipeActivity() {
        listOfEditTexts = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        selectImageBtn = findViewById(R.id.imageBtn);
        titleEditTxt = findViewById(R.id.titleEdit);
        firstIngredientEdit = findViewById(R.id.ingr1Text);
        listOfEditTexts.add(firstIngredientEdit);
        ImageButton firstAddButton = findViewById(R.id.add1Btn);
        Button submitBtn = findViewById(R.id.submitBtn);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog();
            }
        });
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLastPlusSign();
                addNewEditText("");

            }
        };
        firstAddButton.setOnClickListener(onClickListener);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe();
            }
        });

        changedRotationSaved(savedInstanceState);

    }

    private void changedRotationSaved(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable("image");
            ArrayList<String> listOfIngredients = savedInstanceState.getStringArrayList("listIngr");
            hideLastPlusSign();
            for (int i = 2; i < listOfIngredients.size(); i++) {
                addNewEditText(listOfIngredients.get(i));
                if (i != (listOfIngredients.size() - 1)) {
                    hideLastPlusSign();
                }
            }
            selectImageBtn.setImageURI(imageUri);
        }
    }

    private void addRecipe() {
        Recipe recipe = new Recipe("0", titleEditTxt.getText().toString(), Profile.getCurrentProfile().getName(), imageUri.toString(), "", 0);
        recipe.setListOfIngredients(getIngredients());
        Intent intent = new Intent(AddRecipeActivity.this, MainNavActivity.class);
        intent.putExtra("new_recipe", recipe);
        startActivity(intent);
    }

    private void createAlertDialog() {
        final AlertDialog alertDialog = getDialogBuilder().create();
        alertDialog.show();
        ImageButton galleryImageBtn = alertDialog.findViewById(R.id.openGalleryBtn);
        assert galleryImageBtn != null;
        galleryImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageGallery();
                alertDialog.hide();
            }
        });
        ImageButton cameraImageBtn = alertDialog.findViewById(R.id.openCameraBtn);
        assert cameraImageBtn != null;
        cameraImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCamera();
                alertDialog.hide();
            }
        });
    }

    private void addNewEditText(String str) {
        LinearLayout linearLayout = findViewById(R.id.ingrLayout);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout horLinearLayout = new LinearLayout(this);
        horLinearLayout.setLayoutParams(params);
        horLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(horLinearLayout);
        EditText editText = buildEditText();
        if (!str.equals("")) {
            editText.setText(str);
        }
        listOfEditTexts.add(editText);
        ImageButton imageButton = buildImageButton();
        horLinearLayout.addView(editText);
        horLinearLayout.addView(imageButton);
    }

    private void hideLastPlusSign() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        EditText editText = listOfEditTexts.get(listOfEditTexts.size() - 1);
        editText.setLayoutParams(params);
        LinearLayout horLinearLayout = (LinearLayout) editText.getParent();
        horLinearLayout.removeAllViews();
        horLinearLayout.addView(editText);
    }

    private EditText buildEditText() {
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.95f);
        editText.setLayoutParams(params);
        editText.setBackgroundResource(R.drawable.rounded_edittext2);
        editText.setPadding(16, 16, 16, 16);
        editText.requestFocus();
        return editText;
    }

    private ImageButton buildImageButton() {
        ImageButton imageButton = new ImageButton(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButton.setLayoutParams(params);
        imageButton.setImageResource(R.mipmap.ic_add_black_24dp);
        imageButton.setPadding(16, 16, 16, 16);
        imageButton.setBackgroundColor(Color.WHITE);
        imageButton.setOnClickListener(onClickListener);
        return imageButton;
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
            photoFile = createImageFile();
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ff", "fff");
            return null;
        }
    }


    private AlertDialog.Builder getDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
        LayoutInflater inflater = AddRecipeActivity.this.getLayoutInflater();
        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setTitle("Choose image");
        builder.setView(dialogView);
        builder.setCancelable(true);
        return builder;
    }

    private ArrayList<String> getIngredients() {
        ArrayList<String> listOfIngredients = new ArrayList<>();
        listOfIngredients.add(firstIngredientEdit.getText().toString());
        for (int i = 0; i < listOfEditTexts.size(); i++) {
            if (!listOfEditTexts.get(i).getText().toString().equals("")) {
                listOfIngredients.add(listOfEditTexts.get(i).getText().toString());
            }
        }
        return listOfIngredients;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("image", imageUri);
        outState.putStringArrayList("listIngr", getIngredients());
        super.onSaveInstanceState(outState);
    }
}


