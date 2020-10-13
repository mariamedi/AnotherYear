package com.anotheryear.wishes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.birthDate.BirthDateActivity
import com.anotheryear.gift.GiftActivity
import com.anotheryear.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import java.io.File

/**
 * Activity for all Birthday Wishes functionality
 */
class WishesActivity : AppCompatActivity(), WishFormFragment.Callbacks, GeneratedWishFragment.Callbacks {

    private var executeNav = true

    private val wishViewModel: WishesViewModel by lazy {
        ViewModelProviders.of(this).get(WishesViewModel::class.java)
    }

    /**
     * Override for the onCreate method which initializes the bottom nav bar and sets the correct fragment to be displayed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish)

        initializeBottomNavBar()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.wish_fragment_container)

        if (currentFragment == null) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigationView.selectedItemId = R.id.nav_wish
        }
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
     * Val the gets the list of birthdays in the db from BirthdayListViewModel
     */
    override val getWishViewModel: WishesViewModel
        get() = wishViewModel

    /**
     * Function that switches to the WishFormFragment
     */
    override fun generateWish(passedFile: File?) {
        val fragment: Fragment =
            if (passedFile == null) {
                GeneratedWishFragment.newInstance()
            } else {
                GeneratedWishFragment.newInstance(passedFile)
            }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.wish_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Function that switches to the WishesActivity
     */
    override fun changeSettings(photoFile: File) {
        val fragment: Fragment = WishFormFragment.newInstance(photoFile)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.wish_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Function that takes in a string of a fragment and sets the nav bar icon selection accordingly
     */
    override fun selectNavIcon(navIcon: String) {
        executeNav = false // make sure the screen switching functionality is disabled
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        when(navIcon){
            "Wish" -> {
                bottomNavigationView.selectedItemId = R.id.nav_wish
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
            if(executeNav) {
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
                    R.id.nav_gift -> {
                        val intent = GiftActivity.newIntent(this)
                        startActivity(intent)
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