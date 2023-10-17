package mx.a01736353.savethebunny;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Variables pasadas
        int levelNumber = getIntent().getIntExtra("levelNumber", 0);

        //BORRAR ESTO CUANDO SE PASE AL CODIGO FINAL
        levelNumber = 1;

        //Ocultar barra de status
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Dependiendo de levelNumber, enviar a tutorial correspondiente
        if (levelNumber==1 || levelNumber==2 || levelNumber==3){
            setContentView(R.layout.activity_tutorialbasic);
            //Mantener la pantalla encendida
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
/*        else if (levelNumber== 4){
            setContentView(R.layout.activity_tutorialadvanced);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }*/
        else{
            Log.d("Error en TutorialActivity", "Número de nivel inválido");
        }

    }

    //Para regresar
/*    public void goToSelectScreen(View v){
        Intent intent = new Intent(this, SelectLevelActivity.class);
        startActivity(intent);
    }*/

    //Para ir a dialog_selectlevel.xml
    public void dialogSelectLevel(View v) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TutorialActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_selectlevel, viewGroup, false);
        Button buttonBack = dialogView.findViewById(R.id.buttonBack);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        buttonBack.setOnClickListener(v1 -> alertDialog.dismiss());
        alertDialog.show();
    }

    //Para ir a dialog_trashcan1.xml
    public void dialog_trashcan1(View v) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TutorialActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_trashcan1, viewGroup, false);
        Button buttonBack = dialogView.findViewById(R.id.buttonBack);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        buttonBack.setOnClickListener(v1 -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void dialog_trashcan2(View v) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TutorialActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_trashcan2, viewGroup, false);
        Button buttonBack = dialogView.findViewById(R.id.buttonBack);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        buttonBack.setOnClickListener(v1 -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void dialog_trashcan3(View v) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TutorialActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_trashcan3, viewGroup, false);
        Button buttonBack = dialogView.findViewById(R.id.buttonBack);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        buttonBack.setOnClickListener(v1 -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void dialog_trashcan4(View v) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TutorialActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_trashcan4, viewGroup, false);
        Button buttonBack = dialogView.findViewById(R.id.buttonBack);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        buttonBack.setOnClickListener(v1 -> alertDialog.dismiss());
        alertDialog.show();
    }

    //Mandar a respectivos niveles
    public void playLevel_basic(View view) {
        Log.d("1", "Se envia a nivel básico");
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }




}