package mx.a01736353.savethebunny;

import android.app.Activity;
import android.os.Bundle;
import java.util.Objects;

public class WinningActivity extends Activity {

    int winningState;

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
}