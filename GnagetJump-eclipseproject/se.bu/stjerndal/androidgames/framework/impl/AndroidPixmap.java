package se.stjerndal.androidgames.framework.impl;

import se.stjerndal.androidgames.framework.Pixmap;
import se.stjerndal.androidgames.framework.Graphics.PixmapFormat;
import android.graphics.Bitmap;


public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    PixmapFormat format;
    
    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public PixmapFormat getFormat() {
        return format;
    }

    public void dispose() {
        bitmap.recycle();
    }      
}
