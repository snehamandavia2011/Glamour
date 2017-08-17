package entity;

import android.graphics.Bitmap;

/**
 * Created by SAI on 8/17/2017.
 */

public class ProductImage {
    String image_thumb, image;
    Bitmap bmpThumb;

    public ProductImage(String image_thumb, String image) {
        this.image_thumb = image_thumb;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public Bitmap getBmpThumb() {
        return bmpThumb;
    }

    public void setBmpThumb(Bitmap bmpThumb) {
        this.bmpThumb = bmpThumb;
    }

}


