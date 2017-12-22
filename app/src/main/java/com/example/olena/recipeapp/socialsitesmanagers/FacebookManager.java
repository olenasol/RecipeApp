package com.example.olena.recipeapp.socialsitesmanagers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.olena.recipeapp.dataproviders.ImageProvider;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

public class FacebookManager {

    private Context context;

    public FacebookManager(Context context) {
        this.context = context;
    }

    public static boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null;
    }

    public String getProfileFirstName() {
        return Profile.getCurrentProfile().getFirstName();
    }

    public String getProfileLastName() {
        return Profile.getCurrentProfile().getLastName();
    }

    public Uri getProfilePictureUri() {
        return Profile.getCurrentProfile().getProfilePictureUri(200, 200);
    }

    public void postToFacebook(String imageUrl, String title, String listOfIngr) {

        ImageProvider.GetImageAsync task = new ImageProvider.GetImageAsync();
        task.execute(imageUrl);
        Bitmap image = null;
        try {
            image = task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .setUserGenerated(true)
                    .build();
            ShareOpenGraphObject obj = new ShareOpenGraphObject.Builder()
                    .putString("og:type", "object")
                    .putString("og:title", title)
                    .putString("og:description", listOfIngr)
                    .putPhoto("og:image", photo)
                    .build();

            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                    .setActionType("og.likes")
                    .putObject("object", obj)
                    .build();
            ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                    .setPreviewPropertyName("object")
                    .setAction(action)
                    .build();
            ShareDialog.show((Activity) context, content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
