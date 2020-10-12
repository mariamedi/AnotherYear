package com.anotheryear.gift

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R

private const val TAG = "GenderSurveyFrag"
class GenderSurveyFragment : Fragment() {

    // Declare view objects
    private lateinit var maleButton: Button
    private lateinit var femaleButton: Button
    private lateinit var skipButton: Button

    // Declare local version of ViewModel for holding gift survey info across fragments
    private val giftViewModel: GiftViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(GiftViewModel::class.java)
    }

    // Include callback for going to next fragment
    interface Callbacks {
        fun proceedToAgeGroup()
        fun selectNavIcon(navIcon: String)
    }

    private var callbacks: Callbacks? = null

    companion object {
        fun newInstance(): GenderSurveyFragment {
            return GenderSurveyFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gender_survey, container, false)

        // Initialize view objects
        maleButton = view.findViewById(R.id.male_button)
        femaleButton = view.findViewById(R.id.female_button)
        skipButton = view.findViewById(R.id.skip_button)


        maleButton.setOnClickListener {
            giftViewModel.gender = "M"
            callbacks?.proceedToAgeGroup()
        }

        femaleButton.setOnClickListener {
            giftViewModel.gender = "F"
            callbacks?.proceedToAgeGroup()
        }

        skipButton.setOnClickListener {
            giftViewModel.gender = ""
            callbacks?.proceedToAgeGroup()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    /**
     * Adding logs messages for onStart, onResume, onPause, onStop and onDestroy
     */
    override fun onStart() {
        super.onStart()
        // set the proper nav icon selection when this fragment starts
        callbacks?.selectNavIcon("Gift")
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}