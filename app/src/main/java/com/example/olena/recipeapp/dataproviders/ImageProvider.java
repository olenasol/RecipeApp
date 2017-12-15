package com.example.olena.recipeapp.dataproviders;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageProvider {

    public static File getImageFile(Bitmap bitmap, Context context){
        File  storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String imageFileName = "JPEGImage";
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        }catch (IOException e){
            e.printStackTrace();
        }

        assert file != null;
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        try {

            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }

    public static class GetImageAsync extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String...str) {

            return getBitmapFromURL(str[0]);
        }
        Bitmap getBitmapFromURL(String src) {

            try {
                URL url = new URL(src);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
        }

    }

}
