package com.anotheryear.wishes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R

class WishFormFragment : Fragment() {

    private lateinit var birthPersonName: EditText
    private lateinit var relationshipBar: SeekBar
    private lateinit var yourName: EditText
    private lateinit var noSignature: CheckBox
    private lateinit var generateWishButton: Button

    companion object {
        fun newInstance(): WishFormFragment {
            return WishFormFragment()
        }
    }

    private val wishViewModel: WishesViewModel by lazy {
        ViewModelProviders.of(this).get(WishesViewModel::class.java)
    }

    /**
     * Override for the onCreateView method that initializes elements that need to be manipulated
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wish_form, container, false)

//        // Initialization of UI Elements
//        birthPersonName = view.findViewById(R.id.PW_their_name) as EditText
//        relationshipBar = view.findViewById(R.id.PW_simpleSeekBar) as SeekBar
//        yourName = view.findViewById(R.id.PW_your_name) as EditText
//        noSignature = view.findViewById(R.id.PW_no_signature) as CheckBox
//        generateWishButton = view.findViewById(R.id.PW_generate_wish_button) as Button
//
//        //listener for SeekBar
//        relationshipBar.setOnClickListener { view: View ->
//            wishViewModel.relationship = relationshipBar.progress
//        }
//
//        //listener for GenerateWish Button
//        generateWishButton.setOnClickListener{view: View->
//            //check that all field have been filled out
//            if(wishViewModel.readyForWish()){
//                //generate wish activity
//                Toast.makeText(
//                    activity,
//                    "Time to generate wish!",
//                    Toast.LENGTH_SHORT).show()
//            } else {
//                //tell user to fill out all fields
//                Toast.makeText(
//                    activity,
//                    R.string.PW_error,
//                    Toast.LENGTH_SHORT).show()
//            }
//
//        }
        return view
    }

}