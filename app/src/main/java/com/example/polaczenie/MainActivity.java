package com.example.polaczenie;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
                        List<pytanie> pytania = response.body();

                    }

                    @Override
                    public void onFailure(Call<List<pytanie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT);

                    }
                }
        );


    }
}