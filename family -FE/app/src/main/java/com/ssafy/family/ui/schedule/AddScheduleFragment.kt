package com.ssafy.family.ui.schedule

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.ScheduleReq
import com.ssafy.family.databinding.CalendarHeaderBinding
import com.ssafy.family.databinding.CalendarSelectingDayBinding
import com.ssafy.family.databinding.FragmentAddScheduleBinding
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.util.CalendarUtil
import com.ssafy.family.util.CalendarUtil.getDrawableCompat
import com.ssafy.family.util.CalendarUtil.dayLocalDateToString
import com.ssafy.family.util.CalendarUtil.isInDateBetween
import com.ssafy.family.util.CalendarUtil.isOutDateBetween
import com.ssafy.family.util.CalendarUtil.makeInVisible
import com.ssafy.family.util.CalendarUtil.makeVisible
import com.ssafy.family.util.CalendarUtil.setTextColorRes
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class AddScheduleFragment : Fragment() {

    private lateinit var binding: FragmentAddScheduleBinding
    private val addScheduleViewModel by activityViewModels<AddScheduleViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val headerDateFormatter = DateTimeFormatter.ofPattern("MMM d'일'")
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    private val startBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(R.drawable.box_calendar_start) as GradientDrawable
    }
    private val endBackground: GradientDrawable by lazy {
        requireContext().getDrawableCompat(R.drawable.box_calendar_end) as GradientDrawable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //헤더 및 parent 텍스트 초기화
        (activity as ScheduleActivity).apply {
            changeHeader("일정 추가","취소", "저장")
            binding.scheduleButtonInclude.button.setOnClickListener {
                requireActivity().finish()
            }
            binding.scheduleTopInclude.backbtn.setOnClickListener {
                requireActivity().finish()
            }
        }

        //일정 추가 버튼
        (activity as ScheduleActivity).binding.scheduleButtonInclude.button2.setOnClickListener {
            if(binding.scheduleTitle.text.length < 2 || binding.scheduleTitle.text.length > 20){
                Toast.makeText(requireContext(), "일정 제목을 2~20자로 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if(binding.scheduleContent.text!!.length < 2){
                Toast.makeText(requireContext(), "일정 내용을 2자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            }else if(startDate == null && endDate == null){
                Toast.makeText(requireContext(), "일정 날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            }else{
                addScheduleViewModel.addSchedule(
                    ScheduleReq(
                        binding.scheduleTitle.text.toString(),
                        binding.scheduleContent.text.toString(),
                        dayLocalDateToString(startDate!!),
                        dayLocalDateToString(endDate!!)
                    )
                )
            }
        }

        //일정 추가 요청 결과 옵저버
        addScheduleViewModel.addRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "일정 등록이 완료되었어요.", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                    requireActivity().finish()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //달력 초기화
        initCalendar()
    }

    private fun initCalendar() {

        //달력 기본 설정
        val daysOfWeek = CalendarUtil.daysOfWeekFromLocale()
        var currentMonth = YearMonth.now()
        binding.calendar.apply {
            setup(currentMonth.minusMonths(100), currentMonth.plusMonths(100), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        //일자 설정(날짜, 일정, 오늘 날짜 등)
        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.dayText
                val roundBgView = container.binding.roundBackground

                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()

                val startDate = startDate
                val endDate = endDate

                when (day.owner) {
                    DayOwner.THIS_MONTH -> {
                        textView.text = day.day.toString()

                        when {
                            day.date == startDate && startDate != endDate -> {
                                textView.setTextColorRes(R.color.white)
                                textView.background = startBackground
                            }
                            startDate == day.date  -> {
                                textView.setTextColorRes(R.color.white)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.box_calendar_single)
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView.setTextColorRes(R.color.white)
                                textView.setBackgroundResource(R.drawable.box_calendar_middle)
                            }
                            day.date == endDate -> {
                                textView.setTextColorRes(R.color.white)
                                textView.background = endBackground
                            }
                            day.date == today -> {
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_today)
                                textView.setTextColorRes(R.color.black)
                            }
                            else -> textView.setTextColorRes(R.color.black)
                        }
                    }

                    // 선택한 날짜가 월이 바뀌었을 경우, 일자가 없는 칸도 색칠 해줌
                    DayOwner.PREVIOUS_MONTH ->
                        if (startDate != null && endDate != null && isInDateBetween(day.date, startDate, endDate)) {
                            textView.setBackgroundResource(R.drawable.box_calendar_middle)
                        }
                    DayOwner.NEXT_MONTH ->
                        if (startDate != null && endDate != null && isOutDateBetween(day.date, startDate, endDate)) {
                            textView.setBackgroundResource(R.drawable.box_calendar_middle)
                        }
                }
            }
        }

        //달력 스크롤 리스너
        binding.calendar.monthScrollListener = {
            selectDate(it.yearMonth.atDay(1))
        }

        //요일 헤더 선언
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout
        }
        binding.calendar.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {}
        }
    }

    //일자 컨테이너 클릭리스너를 일자마다 달아줌
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        val binding = CalendarSelectingDayBinding.bind(view)

        init {
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH) {
                    val date = day.date
                    if (startDate != null) {
                        if (date < startDate || endDate != startDate) {
                            startDate = date
                            endDate = date
                        } else if (date != startDate) {
                            endDate = date
                        }
                    } else {
                        startDate = date
                        endDate = date
                    }
                    this@AddScheduleFragment.binding.calendar.notifyCalendarChanged()
                    bindSummaryViews()
                }
            }
        }
    }

    //날짜를 선택 : 해당 날짜로 텍스트들도 바꿔준다.
    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendar.notifyDateChanged(it) }
            binding.calendar.notifyDateChanged(date)
        }
    }

    // 선택한 일자로 텍스트를 바꿔준다.
    private fun bindSummaryViews() {
        binding.startdate.apply {
            if (startDate != null) {
                text = headerDateFormatter.format(startDate)
            } else {
                text = "시작"
                setTextColor(Color.GRAY)
            }
        }

        binding.enddate.apply {
            if (endDate != null) {
                text = headerDateFormatter.format(endDate)
            } else {
                text = "끝"
                setTextColor(Color.GRAY)
            }
        }
    }

    //일정 로딩바
    private fun setLoading() {
        binding.progressBarAddSLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarAddSLoading.visibility = View.GONE
    }

}
