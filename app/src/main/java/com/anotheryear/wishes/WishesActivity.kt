package com.anotheryear.wishes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.birthDate.BirthDateActivity
import com.anotheryear.gift.GiftActivity
import com.anotheryear.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.SeekBar

/**
 * Activity for all Birthday Wishes functionality
 */
class WishesActivity : AppCompatActivity() {
//    private lateinit var birthPersonName: EditText
//    private lateinit var relationshipBar: SeekBar
//    private lateinit var yourName: EditText
//    private lateinit var noSignature: CheckBox
//    private lateinit var generateWishButton: Button
//
//    private val wishViewModel: WishesViewModel by lazy {
//        ViewModelProviders.of(this).get(WishesViewModel::class.java)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish)

        initializeBottomNavBar()

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.wish_fragment_container)

        if (currentFragment == null) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigationView.selectedItemId = R.id.nav_wish
        }

//        // Initialization of UI Elements
//        birthPersonName = findViewById(R.id.PW_their_name) as EditText
//        relationshipBar = findViewById(R.id.PW_simpleSeekBar) as SeekBar
//        yourName = findViewById(R.id.PW_your_name) as EditText
//        noSignature = findViewById(R.id.PW_no_signature) as CheckBox
//        generateWishButton = findViewById(R.id.PW_generate_wish_button) as Button
//
//        //listener for SeekBar
//        relationshipBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(
//                seek: SeekBar,
//                progress: Int, fromUser: Boolean
//            ) {
//                // write custom code for progress is changed
//            }
//
//            override fun onStartTrackingTouch(seek: SeekBar) {
//                // write custom code for progress is started
//            }
//
//            override fun onStopTrackingTouch(seek: SeekBar) {
//                // write custom code for progress is stopped
//                wishViewModel.relationship = relationshipBar.progress
//            }
//        })
//
//
//
//        //listener for birthday person name
//        birthPersonName.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable) {}
//
//            override fun beforeTextChanged(s: CharSequence, start: Int,
//                                           count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence, start: Int,
//                                       before: Int, count: Int) {
//                wishViewModel.theirName = s.toString()
//            }
//        })
//
//        //listener for birthday person name
//        yourName.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable) {}
//
//            override fun beforeTextChanged(s: CharSequence, start: Int,
//                                           count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence, start: Int,
//                                       before: Int, count: Int) {
//                wishViewModel.yourName = s.toString()
//            }
//        })
//
//        //listener for GenerateWish Button
//        generateWishButton.setOnClickListener{view: View ->
//            //check that all field have been filled out
//            if(wishViewModel.readyForWish()){
//                //generate wish activity
//                Toast.makeText(
//                    this,
//                    "Time to generate wish!",
//                    Toast.LENGTH_SHORT).show()
//            } else {
//                //tell user to fill out all fields
//                Toast.makeText(
//                    this,
//                    R.string.PW_error,
//                    Toast.LENGTH_SHORT).show()
//            }
//
//        }
    }

    /**
     * Companion object that creates a new intent to start this activity.
     */
    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, WishesActivity::class.java).apply {
            }
        }
    }

    /**
     * Function that adds specific listeners to the icons in the bottom nav bar for WishActivity
     */
    private fun initializeBottomNavBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = BirthDateActivity.newIntent(this, "home")
                    startActivity(intent)
                }
                R.id.nav_calendar -> {
                    val intent = BirthDateActivity.newIntent(this, "calendar")
                    startActivity(intent)
                }
                R.id.nav_wish -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.wish_fragment_container, WishFormFragment.newInstance())
                        .commit()
                }
                R.id.nav_gift ->{
                    val intent = GiftActivity.newIntent(this)
                    startActivity(intent)
                }
            }
            true
        }
    }

    /**
     * Overriding onStart
     */
//    override fun onStart() {
//        super.onStart()
//
//        //EditText listeners
//        val nameWatcher = object : TextWatcher {
//
//            override fun beforeTextChanged(
//                sequence: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {
//                // This space intentionally left blank
//            }
//
//            override fun onTextChanged(
//                sequence: CharSequence?,
//                start: Int,
//                before: Int,
//                count: Int
//            ) {
//                val name = sequence.toString()
//            }
//
//            override fun afterTextChanged(sequence: Editable?) {
//                // This one too
//            }
//        }
//
//        // Listeners
//        birthPersonName.addTextChangedListener(nameWatcher)
//        yourName.addTextChangedListener(nameWatcher)
//
//        // Adding values to ViewModel
//        wishViewModel.theirName = birthPersonName.text.toString()
//        wishViewModel.yourName = yourName.text.toString()
//    }
//
//    /**
//     * Identify if No Signature checkbox was selected
//     */
//    fun onCheckboxClicked(view: View){
//        if(view is CheckBox){
//            val checked: Boolean = view.isChecked
//
//            //disable yourName EditText if checked
//            when(view.id){
//                R.id.PW_no_signature ->{
//                    yourName.isEnabled = !checked
//                    wishViewModel.noSignature = checked
//                }
//            }
//        }
//    }
}