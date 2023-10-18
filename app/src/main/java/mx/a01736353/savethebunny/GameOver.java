package mx.a01736353.savethebunny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        int points = getIntent().getExtras().getInt("points");
        int winningState = getIntent().getExtras().getInt("winningState");

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
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

}
