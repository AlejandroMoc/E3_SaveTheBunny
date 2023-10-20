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
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameView extends View {
    Bitmap background, ground, dumpsterA, dumpsterB, dumpsterC, dumpsterD, heart;
    Rect rectBackground, rectGround, rectHeart;
    Drawable heartDrawable = ContextCompat.getDrawable(getContext(), R.drawable.logo_heart);
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint pointsNumber = new Paint(), lifeNumber = new Paint();
    float pointsTextSize = 120, lifeTextSize = 70, dumpsterAX, dumpsterBX, dumpsterCX, dumpsterDX, dumpstersY, newTrashesBX, newTrashesBY, touchX, touchY;
    int points, winningState, minPoints = 500, life = 5, trashDensity=3, action, i;
    static int dWidth, dHeight, heartSize=120, heartMargin=100;
    boolean gameOver = false;
    Random random;
    ArrayList<Trash> trashesA, trashesB, trashesC, trashesD;
    ArrayList<Explosion> explosions;
    ArrayList <Trash> trashes;
    Explosion explosion;
    Trash trash;

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
        runnable = this::invalidate;

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
        dumpsterAX= Math.floorDiv(dWidth,20);
        dumpsterBX = Math.floorDiv(dWidth,2)-Math.floorDiv(dumpsterB.getWidth(),2);
        dumpsterCX = dWidth-dumpsterB.getWidth()-dumpsterAX;
        //Falta cambiar para nivel avanzado
        dumpsterDX = dWidth-dumpsterC.getWidth()-dumpsterBX;
        dumpstersY = dHeight - ground.getHeight() - dumpsterB.getHeight()+100;

        //Arrays para elementos
        trashesA= new ArrayList<>();
        trashesB = new ArrayList<>();
        trashesC = new ArrayList<>();
        trashesD = new ArrayList<>();
        explosions = new ArrayList<>();

        //Generar basuras en arreglos
        //Falta ver si se puede convertir a un mapa
        for (i=0; i<trashDensity; i++){
            trash = new Trash(context, 2);
            trashesB.add(trash);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        //Checar si es gameOver
        for (i = 0; i< trashesB.size(); i++)
            setGameOver();
        if (!gameOver){
            super.onDraw(canvas);
            //Dibujar fondo y piso
            canvas.drawBitmap(background, null, rectBackground, null);
            canvas.drawBitmap(ground, null, rectGround, null);

            //Dibujar basurasB
            for (i = 0; i< trashDensity; i++){
                canvas.drawBitmap(trashesB.get(i).getTrash(trashesB.get(i).trashBFrame), trashesB.get(i).trashBX, trashesB.get(i).trashBY, null);
                trashesB.get(i).trashBY += trashesB.get(i).trashBVelocity;

                //Checar si las basuras no chocan con el bote (Falta cambiar)
                if (trashesB.get(i).trashBY + trashesB.get(i).getTrashHeight()>=dHeight-ground.getHeight()){
                    //En caso de caer al suelo, perder vida
                    life--;
                    explosion = new Explosion(context);
                    explosion.explosionX = trashesB.get(i).trashBX;
                    explosion.explosionY = trashesB.get(i).trashBY;
                    explosions.add(explosion);
                    trashesB.get(i).resetPosition(2);
                }
            }

            //Actualizar frames de explosiones
            Iterator<Explosion> iterator = explosions.iterator();
            while (iterator.hasNext()) {
                explosion = iterator.next();
                canvas.drawBitmap(explosion.getExplosion(explosion.explosionFrame), explosion.explosionX, explosion.explosionY, null);
                explosion.explosionFrame++;
                if (explosion.explosionFrame > 3) {
                    iterator.remove();
                }
            }

            //Dibujar interfaz
            canvas.drawBitmap(dumpsterA, dumpsterAX, dumpstersY, null);
            canvas.drawBitmap(dumpsterB, dumpsterBX, dumpstersY, null);
            canvas.drawBitmap(dumpsterC, dumpsterCX, dumpstersY, null);
            heartDrawable.draw(canvas);
            canvas.drawText(""+points+"/500", Math.floorDiv(dWidth,2)-200, Math.floorDiv(dHeight,7)-pointsTextSize, pointsNumber);
            canvas.drawText("x"+life, dWidth-150, Math.floorDiv(dHeight, 6)-lifeTextSize-80, lifeNumber);
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }

    private void setGameOver() {
        //Mandar a pantalla de reinicio o de aceptación
        if(life==0){
            if(points >= minPoints){winningState=1;}
            else{winningState=0;}
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            intent.putExtra("winningState", winningState);
            ((Activity)context).finish();
            context.startActivity(intent);
            gameOver=true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Obtener toque
        touchX = event.getX();
        touchY = event.getY();

        for (Trash trashNow : trashesB) {
            //Si está en el límite con la altura de los botes
            if (touchY >= trashNow.trashBY && touchY <= (trashNow.trashBY + trashNow.getTrashHeight())
            && touchX >= trashNow.trashBX && touchX <= (trashNow.trashBX + trashNow.getTrashWidth()))
            {
                action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    //Obtener toque
                    trashNow.oldX = event.getX();
                    trashNow.oldY = event.getY();
                    //Obtener posición de basura
                    trashNow.oldTrashesBX = trashNow.trashBX;
                    trashNow.oldTrashesBY = trashNow.trashBY;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    trashNow.shifX = trashNow.oldX - touchX;
                    trashNow.shiftY = trashNow.oldY - touchY;
                    newTrashesBX = trashNow.oldTrashesBX - trashNow.shifX;
                    newTrashesBY = trashNow.oldTrashesBY - trashNow.shiftY;

                    //Mover en eje X y Y
                    if (newTrashesBX <= 0)
                        trashNow.trashBX = 0;
                    else if (newTrashesBX >= dWidth - trashNow.getTrashWidth())
                        trashNow.trashBX = dWidth - trashNow.getTrashWidth();
                    else
                        trashNow.trashBX = newTrashesBX;

                    if (newTrashesBY <= 0)
                        trashNow.trashBY = 0;
                    else if (newTrashesBY >= dHeight - trashNow.getTrashHeight())
                        trashNow.trashBY = dHeight - trashNow.getTrashHeight();
                    else
                        trashNow.trashBY = newTrashesBY;

                    //Si choca con bote B es points++
                    //Falta ver si es mejor extraer el método
                    //o probablemente usar t0d0 el touchevent para los 3/4 botes
                    if (trashNow.trashBX + trashNow.getTrashWidth()>=dumpsterBX
                            && trashNow.trashBX <= dumpsterBX + dumpsterB.getWidth()
                            && trashNow.trashBY + trashNow.getTrashWidth()>=dumpstersY
                            && trashNow.trashBY + trashNow.getTrashWidth()<=dumpstersY + dumpsterB.getHeight()){
                        points +=10;
                        trashNow.resetPosition(2);
                    }

                    //FALTA Si choca con otro bote es life--

                }
            }
        }
        return true;
    }
}
