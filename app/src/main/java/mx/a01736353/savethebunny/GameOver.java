package mx.a01736353.savethebunny;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class GameOver extends AppCompatActivity {

    TextView tvPoints;
    TextView tvHighest;
    SharedPreferences sharedPreferences;
    ImageView ivNewHighest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        //Recuperar datos
        tvPoints = findViewById(R.id.tvPoints);
        tvHighest = findViewById(R.id.tvHighest);
        ivNewHighest = findViewById(R.id.ivNewHighest);
        int points = Objects.requireNonNull(getIntent().getExtras()).getInt("points");
        int winningState = Objects.requireNonNull(getIntent().getExtras()).getInt("winningState");

        tvPoints.setText(""+points);
        sharedPreferences=getSharedPreferences("my_pref",0);
        int highest = sharedPreferences.getInt("highest",0);

        //Comparar con maayor puntuación
        if (points > highest){
            ivNewHighest.setVisibility(View.VISIBLE);
            highest=points;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highest",highest);
            editor.commit();
        }
        tvHighest.setText("  "+highest);
    }

    public void restart(View view){
        Intent intent = new Intent(GameOver.this, TutorialActivity.class);
        startActivity(intent);
        finish();
    }

    //Seguramente se tenga que pasar el caracter tambien aqui
    public void goToWinningState(View v){

        //Dependiendo de si se ganó o perdió, pasar a distinta pantalla
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    //FALTA CORREGIR ESTA
/*    public void goToWinningState(View v, int winningState){
        Intent intent;
        //Dependiendo de si se ganó o perdió, pasar a distinta pantalla
        if (winningState==0){
            intent = new Intent(this, TutorialActivity.class);
        } else {
            intent = new Intent(this, TutorialActivity.class);
        }
        startActivity(intent);
    }*/

    //BORRAR, SOLO PARA TENERLO DE REFERENCIA
/*    if(life==0){
        if(points >= minPoints){winningState=1;}
        else{winningState=0;}
        Intent intent = new Intent(context, GameOver.class);
        intent.putExtra("points", points);
        intent.putExtra("winningState", winningState);
        ((Activity)context).finish();
        context.startActivity(intent);
        gameOver=true;
    }*/


}
