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
    Drawable heartDrawable = getResources().getDrawable(R.drawable.logo_heart);
    Context context;
    Handler handler;
    //Milisegundos para actualizar
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint pointsNumber = new Paint();
    Paint lifeNumber = new Paint();
    float pointsTextSize = 120;
    float lifeTextSize = 70;
    int points = 0;
    int minPoints = 500;
    int life = 5;
    static int dWidth, dHeight;
    Random random;
    float dumpsterAX, dumpsterBX, dumpsterCX, dumpsterDX, dumpstersY;
    float oldX;
    float olddumpsterAX, olddumpsterAY, olddumpsterBX, olddumpstersY, olddumpsterCX, olddumpsterCY, olddumpsterDX, olddumpsterDY;
    ArrayList<Trash> trashes;
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
            //100 es el margen, 120 es el tamaño del corazón
            heartDrawable.setBounds(dWidth - 120 - 100, 100, dWidth - 100, 100 + 120);

            random = new Random();

            //Posición de los botes
            dumpsterAX= dWidth/20;
            dumpsterBX = dWidth/2-dumpsterB.getWidth()/2;
            dumpsterCX = dWidth-dumpsterB.getWidth()-dWidth/20;
            //AQUI FALTA LO DE D
            dumpstersY = dHeight - ground.getHeight() - dumpsterB.getHeight();

            //Arrays para elementos
            trashes = new ArrayList<>();
            explosions = new ArrayList<>();

            //Generar basuras iniciales
            for (int i=0; i<3; i++){
                Trash trash = new Trash(context);
                trashes.add(trash);
            }
        }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Dibujar elementos fijos
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(dumpsterA, dumpsterAX, dumpstersY, null);
        canvas.drawBitmap(dumpsterB, dumpsterBX, dumpstersY, null);
        canvas.drawBitmap(dumpsterC, dumpsterCX, dumpstersY, null);

        //Dibujar basura A
        for (int i = 0; i< 1; i++){
            canvas.drawBitmap(trashes.get(i).getTrash(trashes.get(i).trashAFrame), trashes.get(i).trashAX, trashes.get(i).trashAY, null);
            trashes.get(i).trashAY += trashes.get(i).trashAVelocity;

            //Checar si las basuras no chocan con el bote (Falta cambiar)
            if (trashes.get(i).trashAY + trashes.get(i).getTrashHeight()>=dHeight-ground.getHeight()){
                points +=10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = trashes.get(i).trashAX;
                explosion.explosionY = trashes.get(i).trashAY;
                explosions.add(explosion);
                trashes.get(i).resetPosition();
            }
        }

        //Checar colisiones
        for (int i = 0; i< trashes.size(); i++){
            if (trashes.get(i).trashAX + trashes.get(i).getTrashWidth()>=dumpsterBX
            && trashes.get(i).trashAX <= dumpsterBX + dumpsterB.getWidth()
            && trashes.get(i).trashAY + trashes.get(i).getTrashWidth()>=dumpstersY
            && trashes.get(i).trashAY + trashes.get(i).getTrashWidth()<=dumpstersY + dumpsterB.getHeight()){
                //life--;
                trashes.get(i).resetPosition();
                if(life == 0 || points == minPoints){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        }

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
        heartDrawable.draw(canvas);
        canvas.drawText(""+points+"/500", dWidth/2-200, dHeight/7-pointsTextSize, pointsNumber);
        canvas.drawText("x"+life, dWidth-150, dHeight/6-lifeTextSize-80, lifeNumber);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Obtener toque
        float touchX = event.getX();
        float touchY = event.getY();

/*        if(touchY>=dumpstersY){
            //Verificar toque
            int action = event.getAction();
            //Si la acción es estar presionando la pantalla
            if(action == MotionEvent.ACTION_DOWN){
                //Obtener el toque del dedo en X
                oldX = event.getX();
                //Obtener posición del cubo de basura
                olddumpsterBX = dumpsterBX;
            }
            //Si la acción es mover la pantalla
            if (action == MotionEvent.ACTION_MOVE){
                //Calcular shift en base al toque
                float shift = oldX - touchX;
                float newdumpsterBX = olddumpsterBX-shift;
                //Mover bote
                if(newdumpsterBX<=0)
                    dumpsterBX=0;
                else if (newdumpsterBX >= dWidth - dumpsterB.getWidth())
                    dumpsterBX = dWidth - dumpsterB.getWidth();
                else
                    dumpsterBX=newdumpsterBX;
            }
        }*/
        return true;
    }
}
