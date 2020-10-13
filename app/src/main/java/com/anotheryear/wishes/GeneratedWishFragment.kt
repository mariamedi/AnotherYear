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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R

private const val TAG = "GeneratedWishFragment"

/**
 * Generated Wish Fragment
 */
class GeneratedWishFragment : Fragment() {
    private lateinit var birthdayWish: TextView
    private lateinit var newWishButton: Button
    private lateinit var changeSettButton: Button
    private var wishViewModel: WishesViewModel? = null

    companion object {
        fun newInstance(): GeneratedWishFragment {
            return GeneratedWishFragment()
        }
    }

    /**
     * Callback interface to access and send data to the WishesActivity
     */
    interface Callbacks {
        // fun changeSettings()
        val getWishViewModel : WishesViewModel
    }
    private var callbacks: Callbacks? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_generated_wish, container, false)

        // Initialization of UI Elements
        birthdayWish = view.findViewById(R.id.PW_birthday_wish) as TextView
        newWishButton = view.findViewById(R.id.PW_generate_new_wish_button) as Button
        changeSettButton = view.findViewById(R.id.PW_change_settings_button) as Button

        // Get the wishViewModel from the WishesActivity via the callback val
        wishViewModel = callbacks!!.getWishViewModel

        // Add current wish to TextView
        birthdayWish.text = wishViewModel?.currentWish

        // listener for Generate New Wish Button
        newWishButton.setOnClickListener{view: View ->
            // generate new wish
            wishViewModel?.generateWish()

            //update textView
            // Add wish to current TextView
            birthdayWish.text = wishViewModel?.currentWish
        }

        // go back to settings page

        return view
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