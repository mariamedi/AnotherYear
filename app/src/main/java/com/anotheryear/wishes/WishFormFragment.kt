package com.anotheryear.wishes

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
import com.anotheryear.R
import java.io.File

private const val TAG = "WishFormFragment"
private const val ARG_IMAGE_FILE = "ImageFile"

class WishFormFragment : Fragment() {

    private lateinit var birthPersonName: EditText
    private lateinit var relationshipBar: SeekBar
    private lateinit var yourName: EditText
    private lateinit var noSignature: CheckBox
    private lateinit var generateWishButton: Button
    private var wishViewModel: WishesViewModel? = null

    /**
     * Callback interface to access and send data to the WishesActivity
     */
    interface Callbacks {
        fun generateWish(passedFile: File?)
        val getWishViewModel : WishesViewModel
        fun selectNavIcon(navIcon: String)
    }
    private var callbacks: Callbacks? = null

    // var for a photoFile passed in from the previous fragment
    private var passedFile: File? = null

    /**
     * Override for the onCreateView method that initializes elements that need to be manipulated
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wish_form, container, false)

        // Get the wishViewModel from the WishesActivity via the callback val
        wishViewModel = callbacks!!.getWishViewModel

        // Initialization of UI Elements
        birthPersonName = view.findViewById(R.id.PW_their_name) as EditText
        relationshipBar = view.findViewById(R.id.PW_simpleSeekBar) as SeekBar
        yourName = view.findViewById(R.id.PW_your_name) as EditText
        noSignature = view.findViewById(R.id.PW_no_signature) as CheckBox
        generateWishButton = view.findViewById(R.id.PW_generate_wish_button) as Button


        // Set everything to its original values (if applicable)
        if(wishViewModel?.theirName != ""){
            birthPersonName.setText(wishViewModel?.theirName)
        }
        if(!wishViewModel?.noSignature!!){
            if(wishViewModel?.yourName != ""){
                yourName.setText(wishViewModel?.yourName)
            }
        } else {
            noSignature.isChecked = true
            yourName.isEnabled = false
        }

        //listener for SeekBar
        relationshipBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                wishViewModel?.relationship = relationshipBar.progress
            }
        })

        //listener for GenerateWish Button
        generateWishButton.setOnClickListener{
            //check that all field have been filled out
            if(wishViewModel?.readyForWish()!!){
                wishViewModel?.generateWish()
                callbacks?.generateWish(passedFile)
            } else {
                //tell user to fill out all fields
                Toast.makeText(
                    activity,
                    R.string.PW_error,
                    Toast.LENGTH_LONG).show()
            }
        }

        // listener for birthday person name
        birthPersonName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                wishViewModel?.theirName = s.toString()
            }
        })

        //listener for birthday person name
        yourName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                wishViewModel?.yourName = s.toString()
            }
        })

        // checking if the user does not want a Signature
        noSignature.setOnClickListener(this@WishFormFragment::onCheckboxClicked)

        // if there is a passed in photoFile, set the var
        passedFile = arguments?.getSerializable(ARG_IMAGE_FILE) as File?

        return view
    }

    /**
     * Overriding onStart
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
        callbacks?.selectNavIcon("Wish")

        //EditText listeners
        val nameWatcher = object : TextWatcher {

            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(sequence: Editable?) {
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
    private fun onCheckboxClicked(view: View){
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
     * Companion object to create new instances of WishFormFragment
     */
    companion object {
        fun newInstance(): WishFormFragment {
            return WishFormFragment()
        }

        fun newInstance(photoFile: File): WishFormFragment {
            val args = Bundle().apply {
                putSerializable(ARG_IMAGE_FILE, photoFile)
            }
            return WishFormFragment().apply {
                arguments = args
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

