package com.anotheryear.wishes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
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

    /**
     * Callback interface to access and send data to the WishesActivity
     */
    interface Callbacks {
        fun changeSettings()
        val getWishViewModel : WishesViewModel
        fun selectNavIcon(navIcon: String)
    }
    private var callbacks: Callbacks? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        newWishButton.setOnClickListener{
            // generate new wish
            wishViewModel?.generateWish()

            //update textView
            // Add wish to current TextView
            birthdayWish.text = wishViewModel?.currentWish
        }

        // go back to settings page
         changeSettButton.setOnClickListener{
             // go back to settings page
             callbacks?.changeSettings()
         }
        return view
    }

    companion object {
        fun newInstance(): GeneratedWishFragment {
            return GeneratedWishFragment()
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
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
        // set the proper nav icon selection when this fragment starts
        callbacks?.selectNavIcon("Wish")
    }
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