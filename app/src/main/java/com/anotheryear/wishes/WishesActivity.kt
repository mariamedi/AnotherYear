package com.anotheryear.wishes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anotheryear.birthDate.BirthDateActivity
import com.anotheryear.gift.GiftActivity
import com.anotheryear.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Activity for all Birthday Wishes functionality
 */
class WishesActivity : AppCompatActivity() {
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
}