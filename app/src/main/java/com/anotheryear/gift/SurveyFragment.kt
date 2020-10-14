package com.anotheryear.gift

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R

private const val TAG = "SurveyFragment"
class SurveyFragment : Fragment() {

    // Declare view objects
    private lateinit var minBudgetEditText: EditText
    private lateinit var maxBudgetEditText: EditText
    private lateinit var noBudgetCheckBox: CheckBox
    private lateinit var nextButton: Button

    // Declare local version of ViewModel for holding gift survey info across fragments
    private val giftViewModel: GiftViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(GiftViewModel::class.java)
    }

    // Include callback for going to next fragment
    interface Callbacks {
        fun proceedToGender()
        fun selectNavIcon(navIcon: String)
    }

    private var callbacks: Callbacks? = null

    companion object {
        fun newInstance(): SurveyFragment {
            return SurveyFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_survey, container, false)

        minBudgetEditText = view.findViewById(R.id.budget_min_edit_text)
        maxBudgetEditText = view.findViewById(R.id.budget_max_edit_text)
        noBudgetCheckBox = view.findViewById(R.id.no_budget_checkbox)
        nextButton = view.findViewById(R.id.next_button)

        minBudgetEditText.setText(giftViewModel.budgetMin.toString())
        maxBudgetEditText.setText(giftViewModel.budgetMax.toString())

        noBudgetCheckBox.isChecked = giftViewModel.noBudget

        noBudgetCheckBox.setOnCheckedChangeListener { _, isChecked ->
            giftViewModel.noBudget = isChecked

            // disabling budget edit texts when it is clicked
            if (isChecked) {
                minBudgetEditText.isEnabled = false
                maxBudgetEditText.isEnabled = false
            } else {
                minBudgetEditText.isEnabled = true
                maxBudgetEditText.isEnabled = true
            }
        }

        nextButton.setOnClickListener {
            if (!noBudgetCheckBox.isChecked && (minBudgetEditText.text.toString().toFloat() > maxBudgetEditText.text.toString().toFloat())) {
                Toast.makeText(requireContext(), "The left value must be less than the right value", Toast.LENGTH_LONG).show()
            } else {
                callbacks?.proceedToGender()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
        callbacks?.selectNavIcon("Gift")

        // Inner class for listening on budget edit text
        class BudgetWatcher (val budgetPart: String) : TextWatcher {

            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {

                var text = sequence.toString()

                if(text == "")
                    text = "0"

                if(budgetPart == "Min"){
                    giftViewModel.budgetMin = text.toFloat()
                }
                else{
                    giftViewModel.budgetMax = text.toFloat()
                }
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }

        val minBudgetWatcher = BudgetWatcher("Min")
        val maxBudgetWatcher = BudgetWatcher("Max")
        minBudgetEditText.addTextChangedListener(minBudgetWatcher)
        maxBudgetEditText.addTextChangedListener(maxBudgetWatcher)
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