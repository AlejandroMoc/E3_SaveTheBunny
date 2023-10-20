package mx.a01736353.savethebunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Trash {
    Bitmap[] trash =new Bitmap[3];
    Random random = new Random();
    int trashFrame = new Random().nextInt(3), trashType;
    float trashX, trashY, trashVelocity, oldTrashX, oldTrashY, oldX, oldY, shifX, shiftY;

    //Constructor de listas basuras
    public Trash(Context context, int trashType){

        //Declaración universal de imágenes
        trash[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b1);
        trash[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b2);
        trash[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_b3);


        //Crear posición inicial
        resetTrash(trashType);
    }

    public Bitmap getTrash(int trashFrame){
        return trash[trashFrame];
    }

    public int getTrashWidth(){
        return trash[0].getWidth();
    }

    public int getTrashHeight(){
        return trash[0].getHeight();
    }

    public void resetTrash(int trashType) {
        trashX = random.nextInt(GameView.dWidth - getTrashWidth());
        trashY = -200 + random.nextInt(600) * -1;
        trashVelocity = 12 + random.nextInt(10);


        if (trashType == 1){
            trashFrame = new Random().nextInt(3);
        } else if (trashType == 2){
            trashFrame = new Random().nextInt(3);
        } else if (trashType == 3){
            trashFrame = new Random().nextInt(3);
        } else if (trashType == 4){
            trashFrame = new Random().nextInt(3);
        }
    }
}
