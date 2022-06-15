package com.zanasi.todolistapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class SetTime(textView: TextView) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    var textView_ = textView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, true)

    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        textView_.text = "$hourOfDay:$minute"
    }
}


class SetDate(textView: TextView) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var textView_ = textView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        textView_.text = "$day/$month/$year"
        // Do something with the date chosen by the user
    }
}