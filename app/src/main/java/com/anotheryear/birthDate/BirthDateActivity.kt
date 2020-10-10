package com.anotheryear.birthDate

import android.content.Context
import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.gift.GiftActivity
import com.anotheryear.R
import com.anotheryear.wishes.WishesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

private const val EXTRA_FRAGMENT = "fragment"
private const val TAG = "BirthDateActi"

/**
 * Activity for all Birthday Date related functionality
 */
class BirthDateActivity : AppCompatActivity(), HomeFragment.Callbacks, BirthdayDetailFragment.Callbacks, CalendarFragment.Callbacks {

    private var created: Int = 0

    private val birthdayListViewModel: BirthdayListViewModel by lazy {
        ViewModelProviders.of(this).get(BirthdayListViewModel::class.java)
    }

    /**
     * Override for the onCreate method which initializes the bottom nav bar and sets the correct fragment to be displayed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")
        setContentView(R.layout.activity_birth_date)

        initializeBottomNavBar()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.birth_date_fragment_container)

        if (currentFragment == null) {
            // switch to the correct fragment based off the intent from another activity (if that intent exists)
            if (intent != null && intent.getStringExtra(EXTRA_FRAGMENT) != null) {
                when(intent.getStringExtra(EXTRA_FRAGMENT)){
                    "home" -> {
                        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                        bottomNavigationView.selectedItemId = R.id.nav_home
                    }

                    "calendar" -> {
                        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                        bottomNavigationView.selectedItemId = R.id.nav_calendar
                    }
                }
            }
            // load the home page normally
            else {
                val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.nav_home
            }
        }

        intent = null // remove intents after they have been used
    }

    /**
     * Function that switches from HomeFragment to BirthdayDetailFragment, given the ID or date of the birthday
     */
    override fun viewDetailFragment(id: UUID?, date: Date?) {
        val fragment: Fragment =
            when {
                id != null -> {
                    BirthdayDetailFragment.newInstance(id)
                }
                date != null -> {
                    BirthdayDetailFragment.newInstance(date)
                }
                else -> {
                    BirthdayDetailFragment.newInstance()
                }
            }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.birth_date_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Val the gets the list of birthdays in the db from BirthdayListViewModel
     */
    override val getBirthdayListViewModel: BirthdayListViewModel
        get() = birthdayListViewModel

    /**
     * Function that returns to the last screen and shows a Toast message based on action taken
     */
    override fun detailAction(message: String) {
        Toast.makeText(this@BirthDateActivity, message, Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    /**
     * Companion object that creates a new intent to start this activity with a string representing the specific fragment to start with.
     */
    companion object {
        fun newIntent(packageContext: Context, fragment: String): Intent {
            return Intent(packageContext, BirthDateActivity::class.java).apply {
                putExtra(EXTRA_FRAGMENT, fragment)
            }
        }
    }

    /**
     * Function that adds specific listeners to the icons in the bottom nav bar for BirthDateActivity
     */
    private fun initializeBottomNavBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (created < 1) {
                created++
                when (item.itemId) {
                    R.id.nav_home -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.birth_date_fragment_container, HomeFragment.newInstance())
                            .commit()
                    }
                    R.id.nav_calendar -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.birth_date_fragment_container, CalendarFragment.newInstance())
                            .commit()
                    }
                    R.id.nav_wish -> {
                        val intent = WishesActivity.newIntent(this)
                        startActivity(intent)
                    }
                    R.id.nav_gift -> {
                        val intent = GiftActivity.newIntent(this)
                        startActivity(intent)
                    }
                }
                true
            }
            else {
                when (item.itemId) {
                    R.id.nav_home -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.birth_date_fragment_container, HomeFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }
                    R.id.nav_calendar -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.birth_date_fragment_container, CalendarFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }
                    R.id.nav_wish -> {
                        val intent = WishesActivity.newIntent(this)
                        startActivity(intent)
                    }
                    R.id.nav_gift -> {
                        val intent = GiftActivity.newIntent(this)
                        startActivity(intent)
                    }
                }
                true
            }
        }
    }
}