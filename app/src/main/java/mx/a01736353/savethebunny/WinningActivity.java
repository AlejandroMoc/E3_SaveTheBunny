package mx.a01736353.savethebunny;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

public class WinningActivity extends Activity {

    int winningState;

    //Crear pantalla de estado (ganado/perdido)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recuperar datos
        winningState = Objects.requireNonNull(getIntent().getExtras()).getInt("winningState");

        //Decidir a qué pantalla enviar
        if (winningState==1)
            setContentView(R.layout.activity_winning);
        else
            setContentView(R.layout.activity_losing);
    }

    //Función para ir a la pantalla de tutorial de nivel
    public void goToTutorial(View v){
        Intent intent = new Intent(this, TutorialActivity.class);
        //intent.putExtra("points", points);
        //AQUI AHORA ADD EXTRA
        startActivity(intent);
    }
}