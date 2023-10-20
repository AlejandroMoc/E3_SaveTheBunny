package mx.a01736353.savethebunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Trash {
    Bitmap trashB[]=new Bitmap[3];
    Random random;
    int trashBFrame = new Random().nextInt(3);
    float trashBX,trashBY, trashBVelocity, oldTrashesBX, oldTrashesBY, oldX, oldY, shifX, shiftY;

    public Trash(Context context){
        //Falta cambiar esto por las basuras
        trashB[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b1);
        trashB[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b2);
        trashB[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b3);
        random = new Random();
        resetPosition();
    }

    public Bitmap getTrash(int trashBFrame){
        return trashB[trashBFrame];
    }

    public int getTrashWidth(){
        return trashB[0].getWidth();
    }

    public int getTrashHeight(){
        return trashB[0].getHeight();
    }

    public void resetPosition() {
        trashBX = random.nextInt(GameView.dWidth - getTrashWidth());
        trashBY = -200 + random.nextInt(600) * -1;
        trashBVelocity = 10 + random.nextInt(16);
        trashBFrame = new Random().nextInt(3);
    }
}
