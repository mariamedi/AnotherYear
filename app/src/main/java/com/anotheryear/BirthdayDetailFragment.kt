package com.anotheryear

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val ARG_BIRTHDAY_ID = "birthdayId"
private const val ARG_BIRTHDAY_DATE = "date"

// Days in each month
private val MONTH_DAYS = listOf(
    (1..31).toMutableList(),
    (1..28).toMutableList(),
    (1..31).toMutableList(),
    (1..30).toMutableList(),
    (1..31).toMutableList(),
    (1..30).toMutableList(),
    (1..31).toMutableList(),
    (1..31).toMutableList(),
    (1..30).toMutableList(),
    (1..31).toMutableList(),
    (1..30).toMutableList(),
    (1..31).toMutableList()
)

class BirthdayDetailFragment : Fragment() {

    private lateinit var birthday: Birthday

    // Declare view objects
    private lateinit var firstNameEditText:EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var discardButton: Button
    private lateinit var deleteButton: Button
    private lateinit var dayDropDown: Spinner
    private lateinit var monthDropDown: Spinner


    // Declare ViewModel for accessing and saving birthday
    private val birthdayDetailViewModel: BirthdayDetailViewModel by lazy {
        ViewModelProviders.of(this).get(BirthdayDetailViewModel::class.java)
    }

    // Include callback for sending status message back to previous fragment
    interface Callbacks {
        fun detailAction(message: String)
    }

    private var callbacks: Callbacks? = null

    companion object {

        // Default fragment creation for testing purposes
        fun newInstance(): BirthdayDetailFragment {
            return BirthdayDetailFragment()
        }

        // Fragment creation when the birthday exists
        fun newInstance(birthdayId: UUID): BirthdayDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_BIRTHDAY_ID, birthdayId)
            }
            return BirthdayDetailFragment().apply {
                arguments = args
            }
        }

        // Fragment creation when user selects date for a new birthday
        fun newInstance(birthdayDate: Date): BirthdayDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_BIRTHDAY_DATE, birthdayDate)
            }
            return BirthdayDetailFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        birthday = Birthday()

        val date: Date? = arguments?.getSerializable(ARG_BIRTHDAY_DATE) as Date?

        // If passed a date, initialize birthday object with the date
        if(date != null){
            birthday.birthday = date
        }

        // If passed an id, load birthday from db
        val id: UUID? = arguments?.getSerializable(ARG_BIRTHDAY_ID) as UUID?
        if(id != null){
            birthdayDetailViewModel.loadBirthday(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_birthday_detail, container, false)

        // Initialize view objects
        firstNameEditText = view.findViewById(R.id.first_name_edit_text)
        lastNameEditText = view.findViewById(R.id.last_name_edit_text)
        dayDropDown = view.findViewById(R.id.day_spinner)
        monthDropDown = view.findViewById(R.id.month_spinner)
        saveButton = view.findViewById(R.id.save_button)
        discardButton = view.findViewById(R.id.discard_button)
        deleteButton = view.findViewById(R.id.delete_button)

        // Create array adapter for month drop down
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Months,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            monthDropDown.adapter = adapter
        }

        // Month drop down listener
        monthDropDown.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                // Create new adapter to update day drop down with new day range
                dayDropDown.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
                    MONTH_DAYS[position])

                // Since day is reset to 1, set selection for day drop down to original day unless
                // it is more than the days in that month
                if(birthday.birthday.date <= MONTH_DAYS[position].size){
                    birthday.birthday = Date(birthday.birthday.year, position, birthday.birthday.date)
                    dayDropDown.setSelection(birthday.birthday.date - 1)
                } else {
                    birthday.birthday = Date(birthday.birthday.year, position, 1)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        // Listener for day drop down
        dayDropDown.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                birthday.birthday = Date(birthday.birthday.year, birthday.birthday.month, position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        saveButton.setOnClickListener {
            birthdayDetailViewModel.saveBirthday(birthday)
            callbacks?.detailAction("Birthday Saved!")
        }

        discardButton.setOnClickListener {
            callbacks?.detailAction("Birthday Changes Discarded!")
        }

        deleteButton.setOnClickListener {
            birthdayDetailViewModel.deleteBirthday(birthday.id)
            callbacks?.detailAction("Birthday Deleted!")
        }

        updateUI()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update birthday object with database record if it exists
        birthdayDetailViewModel.birthdayLiveData.observe(
            viewLifecycleOwner,
            Observer { birthday ->
                birthday?.let {
                    this.birthday = birthday
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        // Inner class for listening on birthday edit text
        class nameWatcher (val namePart: String) : TextWatcher {

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

                if(namePart == "First"){
                    birthday.firstName = sequence.toString()
                }
                else{
                    birthday.lastName = sequence.toString()
                }
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }

        val firstNameWatcher = nameWatcher("First")
        val lastNameWatcher = nameWatcher("Last")
        firstNameEditText.addTextChangedListener(firstNameWatcher)
        lastNameEditText.addTextChangedListener(lastNameWatcher)
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
     * Update name and date UI elements
     */
    private fun updateUI(){
        firstNameEditText.setText(birthday.firstName)
        lastNameEditText.setText(birthday.lastName)
        monthDropDown.setSelection(birthday.birthday.month)
        dayDropDown.setSelection(birthday.birthday.date - 1)
    }
}