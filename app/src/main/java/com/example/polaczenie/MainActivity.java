package com.example.polaczenie;

import android.os.Bundle;
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
    private List<pytanie> pytania;
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
        //pobieramy dane z kad
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/laska01/polaczenie1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<pytanie>> call = jsonPlaceHolderApi.getPytania();
        call.enqueue(
                new Callback<List<pytanie>>() {
                    @Override
                    public void onResponse(Call<List<pytanie>> call, Response<List<pytanie>> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT);
                            return;
                        }
                        pytania = response.body();
                        Toast.makeText(MainActivity.this, pytania.get(0).getTrescPytania(), Toast.LENGTH_SHORT).show();
                        wyswietlPytanie(0);

                    }

                    @Override
                    public void onFailure(Call<List<pytanie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT);

                    }
                }
        );


    }
    private void wyswietlPytanie(int ktore){
        pytanie pytanie = pytania.get(ktore);
        textviewPytanie.setText(pytanie.getTrescPytania());
        radioButton_a.setText(pytanie.getOdpa());
        radioButton_b.setText(pytanie.getOdpb());
        radioButton_c.setText(pytanie.getOdpc());

    }
}