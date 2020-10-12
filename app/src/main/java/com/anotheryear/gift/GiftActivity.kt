package com.anotheryear.gift

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R
import com.anotheryear.birthDate.BirthDateActivity
import com.anotheryear.wishes.WishesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Activity for all Gift Suggestion functionality
 */
class GiftActivity : AppCompatActivity(), SurveyFragment.Callbacks, GenderSurveyFragment.Callbacks, AgeSurveyFragment.Callbacks, InterestsSurveyFragment.Callbacks {

    private var executeNav: Boolean = true

    // Declare ViewModel for holding gift survey info across fragments
    private val giftViewModel: GiftViewModel by lazy {
        ViewModelProviders.of(this).get(GiftViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift)

        initializeBottomNavBar()

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.gift_fragment_container)

        if (currentFragment == null) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigationView.selectedItemId = R.id.nav_gift
        }
    }

    /**
     * Companion object that creates a new intent to start this activity.
     */
    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, GiftActivity::class.java).apply {
            }
        }
    }

    override fun proceedToGender() {
        val fragment: Fragment = GenderSurveyFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.gift_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun proceedToAgeGroup() {
        val fragment: Fragment = AgeSurveyFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.gift_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun proceedToInterests() {
        val fragment: Fragment = InterestsSurveyFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.gift_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun findGifts() {
        TODO("Not yet implemented")
    }

    /**
     * Function that takes in a string of a fragment and sets the nav bar icon selection accordingly
     */
    override fun selectNavIcon(navIcon: String) {
        executeNav = false // make sure the screen switching functionality is disabled
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        when(navIcon){
            "Gift" -> {
                bottomNavigationView.selectedItemId = R.id.nav_gift
            }
        }
        executeNav = true // turn on screen switching again
    }

    /**
     * Function that adds specific listeners to the icons in the bottom nav bar for WishActivity
     */
    private fun initializeBottomNavBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            enableDisableNavButtons(item)
            if (executeNav) {
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
                        val intent = WishesActivity.newIntent(this)
                        startActivity(intent)
                    }
                    R.id.nav_gift -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.gift_fragment_container, SurveyFragment.newInstance())
                            .commit()
                    }
                }
            }
            true
        }
    }

    /**
     * Function that ensure a nav button cannot be clicked if it is already selected
     */
    private fun enableDisableNavButtons(view: MenuItem){
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        when(view.itemId) {
            R.id.nav_home -> {
                view.isEnabled = false
                bottomNavigationView.menu.getItem(1).isEnabled = true
                bottomNavigationView.menu.getItem(2).isEnabled = true
                bottomNavigationView.menu.getItem(3).isEnabled = true
            }
            R.id.nav_calendar -> {
                view.isEnabled = false
                bottomNavigationView.menu.getItem(0).isEnabled = true
                bottomNavigationView.menu.getItem(2).isEnabled = true
                bottomNavigationView.menu.getItem(3).isEnabled = true
            }
            R.id.nav_wish -> {
                view.isEnabled = false
                bottomNavigationView.menu.getItem(0).isEnabled = true
                bottomNavigationView.menu.getItem(1).isEnabled = true
                bottomNavigationView.menu.getItem(3).isEnabled = true
            }
            R.id.nav_gift -> {
                view.isEnabled = false
                bottomNavigationView.menu.getItem(0).isEnabled = true
                bottomNavigationView.menu.getItem(1).isEnabled = true
                bottomNavigationView.menu.getItem(2).isEnabled = true
            }
        }
    }
}