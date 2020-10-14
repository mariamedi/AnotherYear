package com.anotheryear.wishes

import android.util.Log
import androidx.lifecycle.ViewModel
import com.anotheryear.BirthdayRepository
import com.anotheryear.R

private const val TAG =  "PersonalWishVM"

/**
 * PersonalWishesViewModel
 */
class WishesViewModel : ViewModel() {

    /**
     * Log that a PersonalWish ViewModel instance was created
     */
    init{
        Log.d(TAG, "ViewModel instance created")
    }

    //important information that will be saved in the VM
    var theirName = ""
    var yourName = ""
    var noSignature = false
    var relationship = 50
    var currentWish = ""
    var currentIndex = -1

    /**
     * Lists of wishes (based on relationship)
     */
    // range 0 - 10
    val wishes1 = listOf(
        "Wishing you the best on your Birthday and everything good in the year ahead!",
        "Wishing you great happiness and a joy that never ends! Happy Birthday!",
        "Wishing you another year of great accomplishments! Happy Birthday!",
        "Enjoy your special day! Happy Birthday!",
        "May all your wishes come true! Happy Birthday!"
    )

    //range 11 - 50
    val wishes2 = listOf(
        "Have an awesome day filled with extra special memories! Happy Birthday!",
        "Wishing you the biggest slice of happy today! Happy Birthday!",
        "Hope all your birthday wishes come true! Happy Birthday!",
        "Warmest wishes and love on your birthday and always!",
        "Wishing you an amazing day! May all your wishes come true! Happy Birthday!"
    )

    //range 51 - 89
    val wishes3 = listOf(
        "The world is lucky to have you in it! Here's to a wonderful year ahead!",
        "Thinking of you on your birthday and wishing you everything happy!",
        "There's something really special about your birthday...and I'm pretty sure it's you, Happy Birthday!",
        "Hope your birthday is as special as you are. Have an amazing day!",
        "You deserve everything happy. Wishing you that all year long! Happy Birthday!"
    )

    //range 90 - 100
    val wishes4 = listOf(
        "Happy Birthday! Thank you for always being there for me, I hope you have an amazing day and even a more amazing year!",
        "You've put a smile on my face so many times, I hope I can give you one in return on your big dreams. I am so lucky to have you in my life, happy birthday!",
        "If there's anyone who should have a perfect day, it's you! Happy Birthday!",
        "If it were up to me, you wishes would all come true. Have the best birthday ever!",
        "Today’s the perfect day to tell you how much you’re appreciated for all you do, and how much you’re loved for the amazing person you are. I hope you have an amazing day!"
    )

    /**
     * Generate wish based on range
     */
    fun generateWish(){

        // reset current wish
        currentWish = ""

        // identify range
        val range = getRange(relationship)

        //add birthday person's name
        currentWish = "Dear $theirName,\n\n"

        //get random list index
        val index = validIndex()

        //choose from appropriate list
        when(range){
            1->currentWish += wishes1.get(index)
            2->currentWish += wishes2.get(index)
            3->currentWish += wishes3.get(index)
            4->currentWish += wishes4.get(index)
            else->{ //assume they are not close
                currentWish += wishes1.get(index)
            }
        }

        //add signature if apprpriate
        if(!noSignature){
            currentWish += "\n\nBest,\n$yourName"
        }
    }

    /**
     *  Make sure we are not getting the same wish as last time
     */
    fun validIndex(): Int{
        // get random index
        var index = (0..4).random()

        while(index == currentIndex){
            index = (0..4).random()
        }

        // update currentIndex
        currentIndex = index

        return index
    }

    /**
     * Identify range based on Seek bar's progress
     */
    fun getRange(progress: Int) : Int{
        // range 0 - 10
        if(progress < 11){
            return 1
        } else if (progress in 11..50){ //range 11 - 50
            return 2
        } else if (progress in 51..89) {//range 51 - 89
            return 3
        } else{ //90 - 100
            return 4
        }
    }

    /**
     * Check if we are ready to fill out another wish
     */
    fun readyForWish(): Boolean{
        if(theirName == "Name of Birthday Person" || theirName == ""){
            return false
        } else if (!noSignature){
            if(yourName == "Your signature for the wish" || yourName == ""){
                return false
            }
        }
        return true
    }

}