package com.desuzed.clocknweather.ui

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.desuzed.clocknweather.databinding.FragmentClockBinding
import com.desuzed.clocknweather.mvvm.AppViewModelFactory
import com.desuzed.clocknweather.mvvm.CheckBoxStates
import com.desuzed.clocknweather.mvvm.ClockViewModel
import com.desuzed.clocknweather.mvvm.Repository
import com.desuzed.clocknweather.util.ArrowImageView
import com.desuzed.clocknweather.util.CheckBoxManager
import com.desuzed.clocknweather.util.MusicPlayer
import com.desuzed.clocknweather.util.TimeGetter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat

class ClockFragment : Fragment() {
    private lateinit var watchesImage: ImageView
    private lateinit var arrowSeconds: ArrowImageView
    private lateinit var arrowMin: ArrowImageView
    private lateinit var arrowHours: ArrowImageView
    private lateinit var tvTopClock: TextView
    private lateinit var tvHeader: TextView
    private lateinit var tvBottomClock: TextView
    private lateinit var viewModel: ClockViewModel
    private lateinit var mCheckBoxManager: CheckBoxManager
    private var fragmentClockBinding: FragmentClockBinding? = null

    @SuppressLint("SimpleDateFormat")
    private val sdfBottomClock = SimpleDateFormat("hh:mm:ss.S")

    @SuppressLint("SimpleDateFormat")
    private val sdfTopClock = SimpleDateFormat("hh:mm")
    private lateinit var musicPlayer: MusicPlayer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentClockBinding = FragmentClockBinding.inflate(inflater, container, false)
        return fragmentClockBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        //Листенер получения размеров вьюх, чтобы изменить шрифт текста, ибо при попытке получения размеров в onViewCreated получаешь 0
        tvHeader.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val textViewHeight = tvHeader.height
                val textSize = 0.7.toFloat() * textViewHeight
                tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                tvBottomClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                tvTopClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                watchesImage.maxWidth = watchesImage.height
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        observeLiveData()
    }


    private fun init() {
        viewModel = ViewModelProvider(
            this, AppViewModelFactory(
                repository = Repository(requireActivity().application)
            )
        ).get(ClockViewModel::class.java)
        val b = fragmentClockBinding!!.button
        b.setOnClickListener { view1: View? ->
            throw RuntimeException("Test Crash")
        }
        tvHeader = fragmentClockBinding!!.tvHeader
        watchesImage = fragmentClockBinding!!.watchesImage
        tvTopClock = fragmentClockBinding!!.tvTopClock
        tvBottomClock = fragmentClockBinding!!.tvBottomClock
        arrowSeconds = fragmentClockBinding!!.arrowSeconds
        arrowMin = fragmentClockBinding!!.arrowMin
        arrowHours = fragmentClockBinding!!.arrowHours
        arrowRotations()
        val checkBoxMin = fragmentClockBinding!!.checkboxMin
        val checkBox15min = fragmentClockBinding!!.checkbox15min
        val checkBoxHour = fragmentClockBinding!!.checkboxHour
        mCheckBoxManager = CheckBoxManager(checkBoxMin, checkBox15min, checkBoxHour)
        mCheckBoxManager.setOnCheckedChangeListeners(viewModel)
        musicPlayer = MusicPlayer(mCheckBoxManager, requireContext())
        initObservers()
    }

    private fun observeLiveData() {
        viewModel.checkBoxLiveData
            .observe(viewLifecycleOwner,
                { checkBoxStates: CheckBoxStates? -> mCheckBoxManager.updateStates(checkBoxStates!!) })
        viewModel.hourLiveData
            .observe(viewLifecycleOwner, { hour: Int -> turnHourArrow(hour) })
        viewModel.minLiveData
            .observe(viewLifecycleOwner, { min: Int ->
                turnMinuteArrow(min)
                setTextTopClock(sdfTopClock.format(System.currentTimeMillis()))
            })
        viewModel.secLiveData
            .observe(viewLifecycleOwner, { sec: Int -> turnSecondArrow(sec) })
        viewModel.mSecLiveData
            .observe(viewLifecycleOwner, { mSec: Int? ->
                setTextBotClock(
                    sdfBottomClock.format(
                        System.currentTimeMillis()
                    )
                )
            })
    }

    private lateinit var job: Job
    private fun initObservers() {
        val coroutineScope = viewLifecycleOwner.lifecycleScope
        job = coroutineScope.launch {
            Log.i("TAG", "launched on ${Thread.currentThread().name}")
            viewModel.flowEmitter()
                .onEach {
                    viewModel.hourLiveData.value = TimeGetter().hour
                    viewModel.minLiveData.value = TimeGetter().minute
                    viewModel.secLiveData.value = TimeGetter().sec
                    viewModel.mSecLiveData.value = TimeGetter().mSec

                    //Log.i("TAG", "init: $it ${Thread.currentThread().name}")
                }
                .flowOn(Dispatchers.Main)
                .launchIn(coroutineScope)
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun arrowRotations() {
        arrowHours.rotation = (30 * TimeGetter().hour).toFloat()
        arrowMin.rotation = (6 * TimeGetter().minute).toFloat()
        arrowSeconds.rotation = (6 * TimeGetter().sec).toFloat()
    }


    private fun turnHourArrow(hour: Int) {
        val rotation = (30 * hour).toFloat()
        val rotChanged = arrowHours.rotateArrow(rotation)
        if (rotChanged) {
            viewModel.playMusic(rotation, ClockViewModel.ARROW_HOUR, musicPlayer)
        }
    }

    private fun turnSecondArrow(sec: Int) {
        arrowSeconds.rotateArrow((6 * sec).toFloat())
    }

    private fun turnMinuteArrow(min: Int) {
        val rotation = (6 * min).toFloat()
        val rotChanged = arrowMin.rotateArrow(rotation)
        if (rotChanged) {
            viewModel.playMusic(rotation, ClockViewModel.ARROW_MIN, musicPlayer)
        }
    }

    private fun setTextTopClock(time: String) {
        tvTopClock.text = time
    }

    private fun setTextBotClock(time: String) {
        tvBottomClock.text = time
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentClockBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(): ClockFragment {
            return ClockFragment()
        }
    }
}