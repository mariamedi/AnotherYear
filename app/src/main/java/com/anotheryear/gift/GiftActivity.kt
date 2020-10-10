package com.anotheryear.gift

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anotheryear.birthDate.BirthDateActivity
import com.anotheryear.R
import com.anotheryear.birthDate.HomeFragment
import com.anotheryear.wishes.WishesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Activity for all Gift Suggestion functionality
 */
class GiftActivity : AppCompatActivity(), GiftDetailFragment.Callbacks, GiftResultListFragment.Callbacks{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift)

        initializeBottomNavBar()

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.gift_fragment_container)

        if (currentFragment == null) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigationView.selectedItemId = R.id.nav_gift

            // TODO change after testing
            val fragment = GiftResultListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.gift_fragment_container, fragment)
                .commit()
        }
    }

    /**
     * Callback from GiftResultListFragment to display a selected gift.
     */
    override fun onGiftSelected(giftID: Int) {
        val fragment = GiftDetailFragment.newInstance(giftID)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.gift_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Callback from GiftDetailFragment to go the site linked to the siteButton.
     */
    override fun onSiteSelected(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)

        try {
            startActivity(Intent.createChooser(intent, "Launch site"))
            finish()
            Log.i("Web.", "Launching site")
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this@GiftActivity,
                "There is no web client installed.", Toast.LENGTH_SHORT
            ).show()
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
                    val intent = WishesActivity.newIntent(this)
                    startActivity(intent)
                }
                R.id.nav_gift ->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.gift_fragment_container, SurveyFragment.newInstance())
                        .commit()
                }
            }
            true
        }
    }
}