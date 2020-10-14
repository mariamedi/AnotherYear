package com.anotheryear.birthDate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.Birthday
import com.anotheryear.R
import java.util.*

private const val ARG_BIRTHDAY_ID = "birthdayId"
private const val ARG_BIRTHDAY_DATE = "date"
private const val ARG_FROM_FRAG = "whoCalledMe"

private const val TAG = "BirthdayDetailFrag"

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

@Suppress("DEPRECATION")
class BirthdayDetailFragment : Fragment() {

    private lateinit var birthday: Birthday

    // Declare view objects
    private lateinit var titleTextView: TextView
    private lateinit var firstNameEditText:EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var discardButton: Button
    private lateinit var deleteButton: Button
    private lateinit var dayDropDown: Spinner
    private lateinit var monthDropDown: Spinner

    private var fromFrag: String? = ""


    // Declare ViewModel for accessing and saving birthday
    private val birthdayDetailViewModel: BirthdayDetailViewModel by lazy {
        ViewModelProviders.of(this).get(BirthdayDetailViewModel::class.java)
    }

    // Include callback for sending status message back to previous fragment
    interface Callbacks {
        fun detailAction(message: String)
        fun selectNavIcon(navIcon: String)
    }

    private var callbacks: Callbacks? = null

    companion object {

        // Default fragment creation
        fun newInstance(frag: String): BirthdayDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_FROM_FRAG, frag)
            }
            return BirthdayDetailFragment().apply {
                arguments = args
            }
        }

        // Fragment creation when the birthday exists
        fun newInstance(birthdayId: UUID, frag: String): BirthdayDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_BIRTHDAY_ID, birthdayId)
                putSerializable(ARG_FROM_FRAG, frag)
            }
            return BirthdayDetailFragment().apply {
                arguments = args
            }
        }

        // Fragment creation when user selects date for a new birthday
        fun newInstance(birthdayDate: Date, frag: String): BirthdayDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_BIRTHDAY_DATE, birthdayDate)
                putSerializable(ARG_FROM_FRAG, frag)
            }
            return BirthdayDetailFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        birthday = Birthday()


        // If passed a date, initialize birthday object with the date
        val date: Date? = arguments?.getSerializable(ARG_BIRTHDAY_DATE) as Date?
        if(date != null){
            birthday.birthdate = date
        }

        // If passed an id, load birthday from db
        val id: UUID? = arguments?.getSerializable(ARG_BIRTHDAY_ID) as UUID?
        if(id != null) {
            birthdayDetailViewModel.loadBirthday(id)
        }

        fromFrag = arguments?.getSerializable(ARG_FROM_FRAG) as String?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_birthday_detail, container, false)
        Log.d(TAG, "onCreateView() called")

        // Initialize view objects
        titleTextView = view.findViewById(R.id.detail_title_text_view)
        firstNameEditText = view.findViewById(R.id.first_name_edit_text)
        lastNameEditText = view.findViewById(R.id.last_name_edit_text)
        dayDropDown = view.findViewById(R.id.day_spinner)
        monthDropDown = view.findViewById(R.id.month_spinner)
        saveButton = view.findViewById(R.id.save_button)
        discardButton = view.findViewById(R.id.discard_button)
        deleteButton = view.findViewById(R.id.delete_button)

        // If a new birthday is being added, do not show delete button and update the title test
        val id: UUID? = arguments?.getSerializable(ARG_BIRTHDAY_ID) as UUID?
        if(id == null) {
            deleteButton.visibility = View.INVISIBLE
            titleTextView.setText(R.string.add_birthday)
        }

        // Create array adapter for month drop down
        ArrayAdapter.createFromResource(requireContext(), R.array.Months, R.layout.spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_item)
                monthDropDown.adapter = adapter
        }

        // Month drop down listener
        monthDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                // Create new adapter to update day drop down with new day range
                dayDropDown.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item,
                    MONTH_DAYS[position])

                // Since day is reset to 1, set selection for day drop down to original day unless
                // it is more than the days in that month
                if(birthday.birthdate.date <= MONTH_DAYS[position].size){
                    birthday.birthdate = Date(birthday.birthdate.year, position, birthday.birthdate.date)
                    dayDropDown.setSelection(birthday.birthdate.date - 1)
                } else {
                    birthday.birthdate = Date(birthday.birthdate.year, position, 1)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Listener for day drop down
        dayDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                birthday.birthdate = Date(birthday.birthdate.year, birthday.birthdate.month, position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        saveButton.setOnClickListener {
            // Only save if first name is not blank
            if(birthday.firstName == ""){
                Toast.makeText(requireContext(),
                    getString(R.string.PW_error),
                    Toast.LENGTH_LONG).show()
            }
            else{
                birthdayDetailViewModel.saveBirthday(birthday)
                callbacks?.detailAction("Birthday Saved!")
            }
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
        Log.d(TAG, "onViewCreated() called")

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
        Log.d(TAG, "onStart() called")
        callbacks?.selectNavIcon(fromFrag!!)

        // Inner class for listening on birthday edit text
        class NameWatcher (val namePart: String) : TextWatcher {

            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {

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

        val firstNameWatcher = NameWatcher("First")
        val lastNameWatcher = NameWatcher("Last")
        firstNameEditText.addTextChangedListener(firstNameWatcher)
        lastNameEditText.addTextChangedListener(lastNameWatcher)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach() called")
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
        callbacks = null
    }

    /**
     * Update name and date UI elements
     */
    private fun updateUI(){
        firstNameEditText.setText(birthday.firstName)
        lastNameEditText.setText(birthday.lastName)
        monthDropDown.setSelection(birthday.birthdate.month)
        dayDropDown.setSelection(birthday.birthdate.date - 1)
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