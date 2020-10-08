package com.anotheryear.birthDate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anotheryear.R

class CalendarFragment : Fragment() {

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        return view
    }
}