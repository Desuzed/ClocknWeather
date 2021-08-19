package com.desuzed.clocknweather
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.desuzed.clocknweather.mvvm.WeatherViewModel
import com.desuzed.clocknweather.util.DailyAdapter
import com.desuzed.clocknweather.util.HourlyAdapter

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
        val tvCommonInfo = view.findViewById <TextView>(R.id.tvCommonInfo)
        val tvCurrentWeather = view.findViewById <TextView>(R.id.tvCurrentWeather)
        val etCity = view.findViewById<EditText>(R.id.etCity)
        val btnGetCityWeather = view.findViewById<Button>(R.id.bthGetWeather)
        val btnGpsWeather = view.findViewById<Button>(R.id.btnGpsWeather)
        btnGpsWeather.setOnClickListener {
//            if (etCity.text.toString().trim() == "") {
//                Toast.makeText(requireContext(), "Пустое поле", Toast.LENGTH_SHORT)
//            } else {
//                val city = etCity.text.toString().trim()
//                weatherViewModel.setCurrentWeatherByCity(city)
//            }
            weatherViewModel.getForecastOnecall("43.119809", "131.886924")
        }

        val rvDaily = view.findViewById<RecyclerView>(R.id.rvDaily)
        val rvHourly = view.findViewById<RecyclerView>(R.id.rvHourly)
        val lmDaily = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val lmHourly = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvDaily.layoutManager = lmDaily
        rvHourly.layoutManager = lmHourly
        val hourlyAdapter = HourlyAdapter(ArrayList(), requireContext())
        val dailyAdapter = DailyAdapter(ArrayList(), requireContext())
        rvHourly.adapter = hourlyAdapter
        rvDaily.adapter = dailyAdapter

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, {
           // textView.text = it
            tvCommonInfo.text = it.toString()
            tvCurrentWeather.text = it.current.toString()
            dailyAdapter.updateList(it.daily!!, it)
            hourlyAdapter.updateList(it.hourly!!, it)

        })

    }
//
    companion object {
        fun newInstance(): WeatherFragment {
            return WeatherFragment()
        }
    }
}