package mx.a01736353.savethebunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {
    final Bitmap[] explosion = new Bitmap[4];
    int explosionFrame = 0;
    float explosionX, explosionY;

    //Constructor
    public Explosion(Context context){
        explosion[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explote0);
        explosion[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explote1);
        explosion[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explote2);
        explosion[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explote3);
    }

    public Bitmap getExplosion(int explosionFrame){
        return explosion[explosionFrame];
    }
}
