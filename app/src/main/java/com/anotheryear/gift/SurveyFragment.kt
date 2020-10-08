package com.anotheryear.gift

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anotheryear.R

class SurveyFragment : Fragment() {

    companion object {
        fun newInstance(): SurveyFragment {
            return SurveyFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_survey, container, false)

        return view
    }
}