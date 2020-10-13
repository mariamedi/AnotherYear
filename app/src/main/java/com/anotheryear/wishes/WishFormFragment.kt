package com.anotheryear.wishes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R
import com.anotheryear.birthDate.BirthdayListViewModel
import com.anotheryear.birthDate.CalendarFragment
import com.anotheryear.birthDate.HomeFragment
import java.util.*

private const val TAG = "WishFormFragment"

class WishFormFragment : Fragment() {

    private lateinit var birthPersonName: EditText
    private lateinit var relationshipBar: SeekBar
    private lateinit var yourName: EditText
    private lateinit var noSignature: CheckBox
    private lateinit var generateWishButton: Button
    private var wishViewModel: WishesViewModel? = null

    companion object {
        fun newInstance(): WishFormFragment {
            return WishFormFragment()
        }
    }

    /**
     * Callback interface to access and send data to the WishesActivity
     */
    interface Callbacks {
        fun generateWish()
        val getWishViewModel : WishesViewModel
    }
    private var callbacks: Callbacks? = null

    /**
     * Override for the onCreateView method that initializes elements that need to be manipulated
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wish_form, container, false)

        // Get the wishViewModel from the WishesActivity via the callback val
        wishViewModel = callbacks!!.getWishViewModel

        // Initialization of UI Elements
        birthPersonName = view.findViewById(R.id.PW_their_name) as EditText
        relationshipBar = view.findViewById(R.id.PW_simpleSeekBar) as SeekBar
        yourName = view.findViewById(R.id.PW_your_name) as EditText
        noSignature = view.findViewById(R.id.PW_no_signature) as CheckBox
        generateWishButton = view.findViewById(R.id.PW_generate_wish_button) as Button

        //listener for SeekBar
        relationshipBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                wishViewModel?.relationship = relationshipBar.progress
            }
        })

        //listener for GenerateWish Button
        generateWishButton.setOnClickListener{view: View->
            //check that all field have been filled out
            if(wishViewModel?.readyForWish()!!){
                wishViewModel?.generateWish()
                callbacks?.generateWish()
            } else {
                //tell user to fill out all fields
                Toast.makeText(
                    activity,
                    R.string.PW_error,
                    Toast.LENGTH_SHORT).show()
            }
        }

        // listener for birthday person name
        birthPersonName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                wishViewModel?.theirName = s.toString()
            }
        })

        //listener for birthday person name
        yourName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                wishViewModel?.yourName = s.toString()
            }
        })

        // checking if the user does not want a Signature
        noSignature.setOnClickListener{view: View->
            onCheckboxClicked(view)
        }

        return view
    }

    /**
     * Overriding onStart
     */
    override fun onStart() {
        super.onStart()

        //EditText listeners
        val nameWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val name = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        // Listeners
        birthPersonName.addTextChangedListener(nameWatcher)
        yourName.addTextChangedListener(nameWatcher)

        // Adding values to ViewModel
        wishViewModel?.theirName = birthPersonName.text.toString()
        wishViewModel?.yourName = yourName.text.toString()
    }

    /**
     * Identify if No Signature checkbox was selected
     */
    fun onCheckboxClicked(view: View){
        if(view is CheckBox){
            val checked: Boolean = view.isChecked

            //disable yourName EditText if checked
            when(view.id){
                R.id.PW_no_signature ->{
                    yourName.isEnabled = !checked
                    wishViewModel?.noSignature = checked
                }
            }
        }
    }

    /**
     * Overrides the onAttach method to initialize the callbacks
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach() called")
        callbacks = context as Callbacks?
    }

    /**
     * Overrides the onDetach method to set the callbacks to null
     */
    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
        callbacks = null
    }
}

