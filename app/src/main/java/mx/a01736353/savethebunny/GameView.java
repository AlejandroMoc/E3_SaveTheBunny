package mx.a01736353.savethebunny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    Bitmap background, ground, dumpsterA, dumpsterB, dumpsterC, dumpsterD, heart;
    Rect rectBackground, rectGround, rectHeart;
    Drawable heartDrawable = ContextCompat.getDrawable(getContext(), R.drawable.logo_heart);
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint pointsNumber = new Paint();
    Paint lifeNumber = new Paint();
    float pointsTextSize = 120;
    float lifeTextSize = 70;
    int points = 0;
    int minPoints = 500;
    int life = 5;
    int heartSize=120;
    int heartMargin=100;

    static int dWidth, dHeight;
    Random random;
    float dumpsterAX, dumpsterBX, dumpsterCX, dumpsterDX, dumpstersY;
    float oldX;
    float olddumpsterAX, olddumpsterAY, olddumpsterBX, olddumpstersY, olddumpsterCX, olddumpsterCY, olddumpsterDX, olddumpsterDY;
    float oldTrashesBX, OldTrashesBY;
    ArrayList<Trash> trashesB;
    ArrayList<Explosion> explosions;
    public GameView(
        Context context){
            super(context);
            this.context = context;

            background = BitmapFactory.decodeResource(getResources(), R.drawable.background_tiles);
            ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground2);
            heart = BitmapFactory.decodeResource(getResources(), R.drawable.logo_heart);

            dumpsterA = BitmapFactory.decodeResource(getResources(), R.drawable.trashcan_1);
            dumpsterB = BitmapFactory.decodeResource(getResources(), R.drawable.trashcan_2);
            dumpsterC = BitmapFactory.decodeResource(getResources(), R.drawable.trashcan_3);
            dumpsterD = BitmapFactory.decodeResource(getResources(), R.drawable.trashcan_4);
            //dumpsterB = Bitmap.createScaledBitmap(dumpsterB, dumpsterB.getWidth()-dumpsterB.getWidth()/3, dumpsterB.getHeight()-dumpsterB.getHeight()/3, true);

            Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            dWidth = size.x;
            dHeight = size.y;

            //Rectangulos para fondo y piso
            rectBackground = new Rect(0,0,dWidth,dHeight);
            rectGround= new Rect(0,dHeight-ground.getHeight(),dWidth,dHeight);
            rectHeart = new Rect(0,0,dWidth,dHeight);
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            };

            pointsNumber.setColor(ContextCompat.getColor(context, R.color.black));
            pointsNumber.setTextSize(pointsTextSize);
            pointsNumber.setTextAlign(Paint.Align.LEFT);
            pointsNumber.setTypeface(Typeface.DEFAULT_BOLD);
            //pointsNumber.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_blocks));

            lifeNumber.setColor(ContextCompat.getColor(context, R.color.black));
            lifeNumber.setTextSize(lifeTextSize);
            lifeNumber.setTextAlign(Paint.Align.LEFT);
            lifeNumber.setTypeface(Typeface.DEFAULT_BOLD);

            heartDrawable.setBounds(rectHeart.left, rectHeart.top, rectHeart.left + rectHeart.width(), rectHeart.top + rectHeart.height());
            heartDrawable.setBounds(dWidth - heartSize - heartMargin, heartMargin, dWidth - heartMargin, heartMargin + heartSize);

            random = new Random();

            //Posición de los botes
            dumpsterAX= dWidth/20;
            dumpsterBX = dWidth/2-dumpsterB.getWidth()/2;
            dumpsterCX = dWidth-dumpsterB.getWidth()-dWidth/20;
            //AQUI FALTA LO DE D
            dumpstersY = dHeight - ground.getHeight() - dumpsterB.getHeight()+100;

            //Arrays para elementos
            trashesB = new ArrayList<>();
            explosions = new ArrayList<>();

            //Generar basuras iniciales
            for (int i=0; i<3; i++){
                Trash trash = new Trash(context);
                trashesB.add(trash);
            }
        }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Dibujar elementos fijos
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);

        //Dibujar basura A
        for (int i = 0; i< 1; i++){
            canvas.drawBitmap(trashesB.get(i).getTrash(trashesB.get(i).trashBFrame), trashesB.get(i).trashBX, trashesB.get(i).trashBY, null);
            trashesB.get(i).trashBY += trashesB.get(i).trashBVelocity;

            //Checar si las basuras no chocan con el bote (Falta cambiar)
            if (trashesB.get(i).trashBY + trashesB.get(i).getTrashHeight()>=dHeight-ground.getHeight()){
                //points +=10;
                life--;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = trashesB.get(i).trashBX;
                explosion.explosionY = trashesB.get(i).trashBY;
                explosions.add(explosion);
                trashesB.get(i).resetPosition();
            }
        }

        //Checar colisión con los botes
/*        for (int i = 0; i< trashesB.size(); i++){
            if (trashesB.get(i).trashBX + trashesB.get(i).getTrashWidth()>=dumpsterBX
            && trashesB.get(i).trashBX <= dumpsterBX + dumpsterB.getWidth()
            && trashesB.get(i).trashBY + trashesB.get(i).getTrashWidth()>=dumpstersY
            && trashesB.get(i).trashBY + trashesB.get(i).getTrashWidth()<=dumpstersY + dumpsterB.getHeight()){
                //life--;
                trashesB.get(i).resetPosition();
                if(life == 0 || points == minPoints){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        }*/

        //Dibujar explosiones (cuando chocan con el piso)
        for(int i =0; i<explosions.size();i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                    explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame>3){
                explosions.remove(i);
            }
        }

        //Dibujar interfaz
        canvas.drawBitmap(dumpsterA, dumpsterAX, dumpstersY, null);
        canvas.drawBitmap(dumpsterB, dumpsterBX, dumpstersY, null);
        canvas.drawBitmap(dumpsterC, dumpsterCX, dumpstersY, null);
        heartDrawable.draw(canvas);
        canvas.drawText(""+points+"/500", dWidth/2-200, dHeight/7-pointsTextSize, pointsNumber);
        canvas.drawText("x"+life, dWidth-150, dHeight/6-lifeTextSize-80, lifeNumber);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Obtener toque
        float touchX = event.getX();
        float touchY = event.getY();
        float shift;
        int action;

        //Colisión con las basuras
        /*        for (int i = 0; i< trashesA.size(); i++){
            if (trashesA.get(i).trashAX + trashesA.get(i).getTrashWidth()>=dumpsterBX
            && trashesA.get(i).trashAX <= dumpsterBX + dumpsterB.getWidth()
            && trashesA.get(i).trashAY + trashesA.get(i).getTrashWidth()>=dumpstersY
            && trashesA.get(i).trashAY + trashesA.get(i).getTrashWidth()<=dumpstersY + dumpsterB.getHeight()){
                //life--;
                trashesA.get(i).resetPosition();
                if(life == 0 || points == minPoints){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        }*/

        for (int i = 0; i < trashesB.size(); i++) {
            Trash trashNow = trashesB.get(i);
            // Si el toque es en la basura
            if (touchY >= trashNow.trashBY) {
                action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    // Obtener toque en X
                    oldX = event.getX();
                    // Obtener posición de basura
                    trashNow.oldTrashesBX = trashNow.trashBX;
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    shift = oldX - touchX;
                    float newTrashesBX = trashNow.oldTrashesBX - shift;

                    // Mover elemento
                    if (newTrashesBX <= 0)
                        trashNow.trashBX = 0;
                    else if (newTrashesBX >= dWidth - trashNow.getTrashWidth())
                        trashNow.trashBX = dWidth - trashNow.getTrashWidth();
                    else
                        trashNow.trashBX = newTrashesBX;
                }
            }
        }

        // Mover botes (ya no es necesario)
/*        if (touchY >= dumpstersY) {
            // Verificar accion
            action = event.getAction();
            // Si la acción es estar presionando la pantalla
            if (action == MotionEvent.ACTION_DOWN) {
                // Obtener el toque del dedo en X
                oldX = event.getX();
                // Obtener posición del cubo de basura
                olddumpsterBX = dumpsterBX;
            }
            // Si la acción es mover la pantalla
            if (action == MotionEvent.ACTION_MOVE) {
                // Calcular shift en base al toque
                shift = oldX - touchX;
                float newdumpsterBX = olddumpsterBX - shift;
                // Mover bote
                if (newdumpsterBX <= 0)
                    dumpsterBX = 0;
                else if (newdumpsterBX >= dWidth - dumpsterB.getWidth())
                    dumpsterBX = dWidth - dumpsterB.getWidth();
                else
                    dumpsterBX = newdumpsterBX;
            }
        }*/
        return true;
    }
}
