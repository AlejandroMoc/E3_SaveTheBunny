package mx.a01736353.savethebunny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

    Bitmap background, ground, dumpster, dumpster2, dumpster3, dumpster4, heart;
    Rect rectBackground, rectGround, rectHeart;
    Drawable heartDrawable = getResources().getDrawable(R.drawable.logo_heart);
    Context context;
    Handler handler;
    //Milisegundos para actualizar
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint pointsNumber = new Paint();
    Paint livesColor = new Paint();
    Paint lifeNumber = new Paint();
    float pointsTextSize = 120;
    float lifeTextSize = 70;
    int points = 0;
    int life = 5;
    static int dWidth, dHeight;
    Random random;
    float dumpsterX, dumpsterY;
    float oldX;
    float oldDumpsterX;
    ArrayList<Trash> trashes;
    ArrayList<Explosion> explosions;
    public GameView(
        Context context){
            super(context);
            this.context = context;

            background = BitmapFactory.decodeResource(getResources(), R.drawable.background_tiles);
            ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
            dumpster = BitmapFactory.decodeResource(getResources(), R.drawable.trashcan_1);
            dumpster = Bitmap.createScaledBitmap(dumpster, dumpster.getWidth()-dumpster.getWidth()/3, dumpster.getHeight()-dumpster.getHeight()/3, true);
            heart = BitmapFactory.decodeResource(getResources(), R.drawable.logo_heart);

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

            //Color inicial de barra de vida
            livesColor.setColor(Color.GREEN);
            random = new Random();
            dumpsterX = dWidth/2-dumpster.getWidth()/2;
            dumpsterY = dHeight - ground.getHeight() - dumpster.getHeight();
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
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(dumpster, dumpsterX, dumpsterY, null);
        canvas.drawBitmap(ground, null, rectGround, null);

        //Dibujar basuras
        for (int i = 0; i< trashes.size(); i++){
            canvas.drawBitmap(trashes.get(i).getTrash(trashes.get(i).spikeFrame), trashes.get(i).spikeX, trashes.get(i).spikeY, null);
            trashes.get(i).spikeY += trashes.get(i).spikeVelocity;

            //Check if bottom of spikes touch the bottom
            if (trashes.get(i).spikeY + trashes.get(i).getTrashHeigth()>=dHeight-ground.getHeight()){
                points +=10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = trashes.get(i).spikeX;
                explosion.explosionY = trashes.get(i).spikeY;
                explosions.add(explosion);
                trashes.get(i).resetPosition();
            }

        }

        //Checar colisiones
        for (int i = 0; i< trashes.size(); i++){
            if (trashes.get(i).spikeX + trashes.get(i).getTrashWidth()>=dumpsterX
            && trashes.get(i).spikeX <= dumpsterX + dumpster.getWidth()
            && trashes.get(i).spikeY + trashes.get(i).getTrashWidth()>=dumpsterY
            && trashes.get(i).spikeY + trashes.get(i).getTrashWidth()<=dumpsterY + dumpster.getHeight()){
                life--;
                trashes.get(i).resetPosition();
                if(life ==0){
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

        //Actualizar color de pintura de acuerdo con las vidas
        if (life == 3){
            livesColor.setColor(Color.YELLOW);
        } else if (life== 1){
            livesColor.setColor(Color.RED);
        }

        //Dibujar elementos en pantallas
        heartDrawable.draw(canvas);
        canvas.drawRect(dWidth-200,30,dWidth-200+60*life,80, livesColor);
        canvas.drawText(""+points+"/500", dWidth/2-200, dHeight/7-pointsTextSize, pointsNumber);
        canvas.drawText("x"+life, dWidth-150, dHeight/6-lifeTextSize-80, lifeNumber);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Obtener toque
        float touchX = event.getX();
        float touchY = event.getY();

        if(touchY>=dumpsterY){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldDumpsterX = dumpsterX;
            }
            if (action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newDumpsterX = oldDumpsterX-shift;
                if(newDumpsterX<=0)
                    dumpsterX=0;
                else if (newDumpsterX >= dWidth - dumpster.getWidth())
                    dumpsterX = dWidth - dumpster.getWidth();
                else
                    dumpsterX=newDumpsterX;
            }
        }
        return true;
    }
}
