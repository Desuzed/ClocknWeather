package com.desuzed.clocknweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFragment extends Fragment {
    private final String appId = "9154a207f4437c78b9bbfbffeeb98e1a";
    public static String BaseUrl = "https://api.openweathermap.org/";
    private TextView textView;

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.textView);
        Button b = view.findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeather();
            }
        });
    }

    private void getWeather() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
//VLD: 43.10562   131.87353
        //Galenki:
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData("44.04339", "131.7679", appId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse wResponse = response.body();
                    float temp = Float.parseFloat(String.valueOf(wResponse.main.temp)) - 273;
                    String stringBuilder = "Country: " +
                            wResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            temp +
                            "\n" +
                            "Temperature(Min): " +
                            wResponse.main.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            wResponse.main.temp_max +
                            "\n" +
                            "Humidity: " +
                            wResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            wResponse.main.pressure +
                            "\n" +
                            "Name: " + wResponse.name;

                    textView.setText(stringBuilder);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
    }
}
