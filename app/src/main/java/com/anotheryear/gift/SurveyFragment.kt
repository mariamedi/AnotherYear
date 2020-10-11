package com.anotheryear.gift

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R

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
    }

    private var callbacks: Callbacks? = null

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

        minBudgetEditText = view.findViewById(R.id.budget_min_edit_text)
        maxBudgetEditText = view.findViewById(R.id.budget_max_edit_text)
        noBudgetCheckBox = view.findViewById(R.id.no_budget_checkbox)
        nextButton = view.findViewById(R.id.next_button)

        minBudgetEditText.setText(giftViewModel.budgetMin.toString())
        maxBudgetEditText.setText(giftViewModel.budgetMax.toString())

        noBudgetCheckBox.isChecked = giftViewModel.noBudget


        noBudgetCheckBox.setOnCheckedChangeListener { _, isChecked ->
            giftViewModel.noBudget = isChecked
        }

        nextButton.setOnClickListener {
            callbacks?.proceedToGender()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // Inner class for listening on budget edit text
        class BudgetWatcher (val budgetPart: String) : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

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


}