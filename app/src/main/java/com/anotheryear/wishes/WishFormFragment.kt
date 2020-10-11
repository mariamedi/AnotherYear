package com.anotheryear.wishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import androidx.fragment.app.Fragment
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

    /**
     * Override for the onCreateView method that initializes elements that need to be manipulated
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wish_form, container, false)

        // Initialization of UI Elements
        birthPersonName = view.findViewById(R.id.PW_their_name) as EditText
        relationshipBar = view.findViewById(R.id.PW_simpleSeekBar) as SeekBar
        yourName = view.findViewById(R.id.PW_your_name) as EditText
        noSignature = view.findViewById(R.id.PW_no_signature) as CheckBox
        generateWishButton = view.findViewById(R.id.PW_generate_wish_button) as Button

        return view
    }
}