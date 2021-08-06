package com.desuzed.clocknweather
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.desuzed.clocknweather.mvvm.WeatherViewModel

class WeatherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherViewModel: WeatherViewModel = ViewModelProvider(this,
            ViewModelProvider
                .AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(WeatherViewModel::class.java)

        val textView = view.findViewById <TextView>(R.id.tvWeather)
        val etCity = view.findViewById<EditText>(R.id.etCity)
        val btnGetCityWeather = view.findViewById<Button>(R.id.bthGetWeather)
        btnGetCityWeather.setOnClickListener {
//            if (etCity.text.toString().trim() == "") {
//                Toast.makeText(requireContext(), "Пустое поле", Toast.LENGTH_SHORT)
//            } else {
//                val city = etCity.text.toString().trim()
//                weatherViewModel.setCurrentWeatherByCity(city)
//            }
            weatherViewModel.getForecastOnecall("55.751244", "37.618423")
        }

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, {
            textView.text = it
        })

    }
//
//    private fun getWeather(city: String) {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
//        val client = OkHttpClient.Builder()
//            .addInterceptor(interceptor)
//            .build()
//        val retrofit = Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//        val wService = retrofit.create(WeatherService::class.java)
//        val call = wService.getCurrentWeatherDataByCity(city, appId, "metric", "ru")
//
//
//        call.enqueue(object : Callback<WeatherResponse?> {
//            override fun onResponse(
//                call: Call<WeatherResponse?>,
//                response: Response<WeatherResponse?>
//            ) {
//                var text: String = ""
//                if (response.code() == 200) {
//                    val weatherResponse = response.body()
//                    if (weatherResponse != null) {
//                        //        val temp = weatherResponse.main.temp - 273
//                        text = "Страна: ${weatherResponse.sys.country}\n" +
//                                "Температура: ${weatherResponse.main.temp}\n" +
//                                "Влажность: ${weatherResponse.main.humidity}\n" +
//                                "Давление: ${weatherResponse.main.pressure}\n" +
//                                "Место: ${weatherResponse.name}\n" +
//                                "Sunrise: ${SimpleDateFormat("hh:mm:ss").format(weatherResponse.sys.sunrise)}\n" +
//                                "Sunset: ${SimpleDateFormat("hh:mm:ss").format(weatherResponse.sys.sunset)}\n"
//                        // val date = SimpleDateFormat("hh:mm:ss").format(weatherResponse.sys.sunrise)
//
//                    }
//
//                }
//                textView?.text = text
//            }
//
//            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//
//    }
    companion object {
        fun newInstance(): WeatherFragment {
            return WeatherFragment()
        }
    }
}