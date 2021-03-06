package com.anotheryear.gift

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R

private const val TAG = "InterestsSurveyFrag"
class InterestsSurveyFragment: Fragment() {

    // Declare view objects
    private lateinit var sportsCheckBox: CheckBox
    private lateinit var artCheckBox: CheckBox
    private lateinit var cookingCheckBox: CheckBox
    private lateinit var toysCheckBox: CheckBox
    private lateinit var electronicsCheckBox: CheckBox
    private lateinit var fashionCheckBox: CheckBox
    private lateinit var findGiftButton: Button

    // Declare local version of ViewModel for holding gift survey info across fragments
    private val giftViewModel: GiftViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(GiftViewModel::class.java)
    }

    // Include callback for going to next fragment
    interface Callbacks {
        fun findGifts()
        fun selectNavIcon(navIcon: String)
    }

    private var callbacks: Callbacks? = null

    companion object {
        fun newInstance(): InterestsSurveyFragment {
            return InterestsSurveyFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_interests_survey, container, false)

        // Initialize view objects
        sportsCheckBox = view.findViewById(R.id.sports_checkbox)
        artCheckBox = view.findViewById(R.id.art_checkbox)
        cookingCheckBox = view.findViewById(R.id.cooking_checkbox)
        toysCheckBox = view.findViewById(R.id.toys_checkbox)
        electronicsCheckBox = view.findViewById(R.id.electronics_checkbox)
        fashionCheckBox = view.findViewById(R.id.fashion_checkbox)
        findGiftButton = view.findViewById(R.id.find_gifts_button)

        // Set check boxes to be checked based on view model
        sportsCheckBox.isChecked = giftViewModel.keywords.containsKey("Sports")
        artCheckBox.isChecked = giftViewModel.keywords.containsKey("Art")
        cookingCheckBox.isChecked = giftViewModel.keywords.containsKey("Cooking")
        toysCheckBox.isChecked = giftViewModel.keywords.containsKey("Toys")
        electronicsCheckBox.isChecked = giftViewModel.keywords.containsKey("Electronics")
        fashionCheckBox.isChecked = giftViewModel.keywords.containsKey("Fashion")

        sportsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                giftViewModel.keywords["Sports"] = "Sports"
            } else {
                giftViewModel.keywords.remove("Sports")
            }

        }

        artCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                giftViewModel.keywords["Art"] = "Art"
            } else {
                giftViewModel.keywords.remove("Art")
            }
        }

        cookingCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                giftViewModel.keywords["Cooking"] = "Cooking"
            } else {
                giftViewModel.keywords.remove("Cooking")
            }
        }

        toysCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                giftViewModel.keywords["Toys"] = "Toys"
            } else {
                giftViewModel.keywords.remove("Toys")
            }
        }

        electronicsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                giftViewModel.keywords["Electronics"] = "Electronics"
            } else {
                giftViewModel.keywords.remove("Electronics")
            }
        }

        fashionCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                giftViewModel.keywords["Fashion"] = "Fashion"
            } else {
                giftViewModel.keywords.remove("Fashion")
            }
        }

        findGiftButton.setOnClickListener {
            callbacks?.findGifts()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
        callbacks?.selectNavIcon("Gift")
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
     * Adding logs messages for onResume, onPause, onStop and onDestroy
     */
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