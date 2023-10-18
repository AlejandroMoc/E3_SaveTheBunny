package mx.a01736353.savethebunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Trash {
    Bitmap trashA[]=new Bitmap[3];
    Random random;
    int trashAFrame = new Random().nextInt(3);
    float trashAX,trashAY, trashAVelocity;

    public Trash(Context context){
        //Falta cambiar esto por las basuras
        trashA[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b1);
        trashA[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b2);
        trashA[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b3);
        random = new Random();
        resetPosition();
    }

    public Bitmap getTrash(int trashAFrame){
        return trashA[trashAFrame];
    }

    public int getTrashWidth(){
        return trashA[0].getWidth();
    }

    public int getTrashHeight(){
        return trashA[0].getHeight();
    }

    public void resetPosition() {
        trashAX = random.nextInt(GameView.dWidth - getTrashWidth());
        trashAY = -200 + random.nextInt(600) * -1;
        trashAVelocity = 10 + random.nextInt(16);
        trashAFrame = new Random().nextInt(3);
    }
}
