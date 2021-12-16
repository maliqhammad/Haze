package com.hammad.iphoneringtones.ui.wallpapers;

import android.graphics.Bitmap;
import android.net.Uri;

public class WallpaperModel {
    private Integer image = 0;
    private String imageName = "";
    private Bitmap bitmap;
    private Uri uri;

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
