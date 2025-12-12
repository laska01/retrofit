package com.example.polaczenie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private List<Pytanie> pytanias;
    TextView textviewPytanie;
    RadioGroup radioGroup;
     int  radioButtonid[] = new int[]{
             R.id.radioButton,
             R.id.radioButton2,
             R.id.radioButton3
    };
     RadioButton radioButton_a;
     RadioButton radioButton_b;
     RadioButton radioButton_c;
     Button buttonDalej;
     Button ButtonPodziel;

     int aktualnePytanie = 0;
     int sumaPunktow = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        textviewPytanie = findViewById(R.id.textviewTrescPytania);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_a = findViewById(R.id.radioButton);
        radioButton_b = findViewById(R.id.radioButton2);
        radioButton_c = findViewById(R.id.radioButton3);
        buttonDalej = findViewById(R.id.buttonDalej);
        ButtonPodziel = findViewById(R.id.buttonPodziel);


        //pobieramy dane z kad
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/laska01/polaczenie1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Pytanie>> call = jsonPlaceHolderApi.getPytania();
        call.enqueue(
                new Callback<List<Pytanie>>() {
                    @Override
                    public void onResponse(Call<List<Pytanie>> call, Response<List<Pytanie>> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT);
                            return;
                        }
                        pytanias = response.body();
                        Toast.makeText(MainActivity.this, pytanias.get(0).getTrescPytania(), Toast.LENGTH_SHORT).show();
                        wyswietlPytanie(0);

                    }

                    @Override
                    public void onFailure(Call<List<Pytanie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT);

                    }
                }
        );
        buttonDalej.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            if(sprawdzOdpowiedz(aktualnePytanie)){
                                Toast.makeText(MainActivity.this, "dobrze", Toast.LENGTH_SHORT);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "zle", Toast.LENGTH_SHORT);
                            }
                            if (aktualnePytanie < pytanias.size() - 1) {
                            aktualnePytanie++;
                            wyswietlPytanie(aktualnePytanie);
                        }else{
                            //TODO:koniec testu
                            //podliczanie punktow znika wszystko wysylamy wynik sms
                            radioGroup.setVisibility(view.INVISIBLE);
                            textviewPytanie.setText("koniec testu, punkty"+sumaPunktow);
                            buttonDalej.setVisibility(view.INVISIBLE);
                            ButtonPodziel.setVisibility(view.VISIBLE);
                        }
                    }
                }
        );
        ButtonPodziel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentWyslij = new Intent();
                        intentWyslij.setAction(Intent.ACTION_SEND);
                        intentWyslij.putExtra(Intent.EXTRA_TEXT,"otrzymano: "+sumaPunktow);
                        intentWyslij.setType("text/plain");
                        Intent intentUdostepniona = Intent.createChooser(intentWyslij, null);
                        startActivity(intentUdostepniona);
                    }
                }
        );

    }

    private boolean sprawdzOdpowiedz(int aktualnePytanie) {
        Pytanie pytanko = pytanias.get(aktualnePytanie);
        if(radioGroup.getCheckedRadioButtonId() == radioButtonid[pytanko.getPoprawna()])
            return true;
        return false;
    }


    private void wyswietlPytanie(int ktore){
        Pytanie pytanie = pytanias.get(ktore);
        textviewPytanie.setText(pytanie.getTrescPytania());
        radioButton_a.setText(pytanie.getOdpa());
        radioButton_b.setText(pytanie.getOdpb());
        radioButton_c.setText(pytanie.getOdpc());

    }
}
