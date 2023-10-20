package mx.a01736353.savethebunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Trash {
    Bitmap[] trashB =new Bitmap[3];
    Random random = new Random();
    int trashBFrame = new Random().nextInt(3), trashType;
    float trashBX, trashBY, trashBVelocity, oldTrashesBX, oldTrashesBY, oldX, oldY, shifX, shiftY;
    
    //Constructor de listas basuras
    public Trash(Context context, int trashType){

        if (trashType==2){
            //Falta cambiar esto por las basuras
            trashB[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b1);
            trashB[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b2);
            trashB[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b3);

        }


        //Crear posición inicial
        resetPosition(trashType);
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

    public void resetPosition(int trashType) {
        trashBX = random.nextInt(GameView.dWidth - getTrashWidth());
        trashBY = -200 + random.nextInt(600) * -1;
        trashBVelocity = 12 + random.nextInt(10);


        trashBFrame = new Random().nextInt(3);
    }
}
