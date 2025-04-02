package com.example.newsliderview

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var tvLoanAmount: TextView
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var labelLoanPeriod: TextView
    private lateinit var seekBarLoanAmount: SeekBar
    private lateinit var seekBarLoanPeriod: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)

        // Initialize views
        tvLoanAmount = view.findViewById(R.id.tvLoanAmount)
        tvStartDate = view.findViewById(R.id.tvStartDate)
        tvEndDate = view.findViewById(R.id.tvEndDate)
        labelLoanPeriod = view.findViewById(R.id.labelLoanPeriod)
        seekBarLoanAmount = view.findViewById(R.id.seekBarLoanAmount)
        seekBarLoanPeriod = view.findViewById(R.id.seekBarLoanPeriod)

        // Set SeekBar progress to max initially
        seekBarLoanAmount.progress = seekBarLoanAmount.max
        seekBarLoanPeriod.progress = seekBarLoanPeriod.max

        // Set the default Loan Amount and Loan Period
        tvLoanAmount.text = "LKR ${seekBarLoanAmount.max}"
        updateLoanDates(seekBarLoanPeriod.max)

        // Set listeners for SeekBars
        seekBarLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val step = 1000
                val roundedProgress = (progress / step) * step  // Round to nearest 1000
                tvLoanAmount.text = "LKR $roundedProgress"
                seekBar?.progress = roundedProgress // Ensure SeekBar snaps to 1000s
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarLoanPeriod.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val adjustedProgress = if (progress < 1) 1 else progress
                updateLoanDates(adjustedProgress)
                seekBar?.progress = adjustedProgress
                updateLoanPeriodLabel(adjustedProgress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // When user clicks on End Date, open the DatePickerDialog
        tvEndDate.setOnClickListener {
            openDatePicker()
        }

        // Return the view for the BottomSheet
        return view
    }

    // Function to update Start Date and End Date based on Loan Period
    private fun updateLoanDates(months: Int) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Set start date to today
        val startDate = dateFormat.format(calendar.time)
        tvStartDate.text = startDate

        // Add months to get end date
        calendar.add(Calendar.MONTH, months)
        val endDate = dateFormat.format(calendar.time)
        tvEndDate.text = endDate
    }

    // Function to update the Loan Period label based on the months
    private fun updateLoanPeriodLabel(months: Int) {
        val periodText = if (months == 1) {
            "1 Month"
        } else {
            "$months Months"
        }
        labelLoanPeriod.text = periodText
    }

    // Open Date Picker Dialog to select End Date
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            // Set the selected date in tvEndDate
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            tvEndDate.text = selectedDate

            // Update Loan Period based on the selected end date
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)

            val currentCalendar = Calendar.getInstance()

            val monthsDifference = getMonthsDifference(currentCalendar, selectedCalendar)
            val adjustedMonths = if (monthsDifference < 1) 1 else monthsDifference

            seekBarLoanPeriod.progress = adjustedMonths
            updateLoanPeriodLabel(adjustedMonths)
        }, currentYear, currentMonth, currentDay)

        datePickerDialog.show()
    }

    // Function to calculate months difference between two dates
    private fun getMonthsDifference(startCalendar: Calendar, endCalendar: Calendar): Int {
        val yearDiff = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)
        val monthDiff = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
        return (yearDiff * 12) + monthDiff
    }
}
