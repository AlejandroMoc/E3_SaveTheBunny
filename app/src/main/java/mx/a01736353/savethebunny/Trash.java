package mx.a01736353.savethebunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Trash {
    Bitmap spike[]=new Bitmap[3];
    Random random;
    int spikeFrame = new Random().nextInt(3);
    int spikeX,spikeY, spikeVelocity;

    public Trash(Context context){
        //Falta cambiar esto por las basuras
        spike[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash1);
        spike[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash2);
        spike[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash3);

        random = new Random();
        resetPosition();
    }

    public Bitmap getTrash(int spikeFrame){
        return spike[spikeFrame];
    }

    public int getTrashWidth(){
        return spike[0].getWidth();
    }

    public int getTrashHeigth(){
        return spike[0].getHeight();
    }

    public void resetPosition() {
        spikeX = random.nextInt(GameView.dWidth - getTrashWidth());
        spikeY = -200 + random.nextInt(600) * -1;
        spikeVelocity = 35 + random.nextInt(16);

    }
}
