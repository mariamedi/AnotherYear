package com.anotheryear.wishes

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class WishesViewModelTest {

    private lateinit var subject: WishesViewModel

    @Before
    fun setUp() {
        subject = WishesViewModel()
    }

    /**
     * Check that generating new wish will not return the currentWish
     */
    @Test
    fun testValidIndex(){
        subject.currentIndex = 2
        assertNotEquals(2, subject.validIndex())
    }

    /**
     * Check we get the valid range for the wish lists based on the
     * progress (from seekbar)
     */
    @Test
    fun testWishRange(){
        // range 0 -10
        assertEquals(1, subject.getRange(7))

        // range  11 - 50
        assertEquals(2, subject.getRange(30))

        //range 51 - 89
        assertEquals(3, subject.getRange(69))

        //range 90 - 100
        assertEquals(4, subject.getRange(98))
    }

    /**
     * Check that it is properly checking if we are ready to generate wush
     */
    @Test
    fun testReadyForWish(){
        // testing without name for birthday person
        subject.noSignature = true
        assertEquals(false, subject.readyForWish())

        // testing without your name (no subject set to true)
        subject.theirName = "Joseph Person"
        assertEquals(true, subject.readyForWish())

        // testing without your name (no subject set to false)
        subject.theirName = "Joseph Person"
        subject.noSignature = false
        assertEquals(false, subject.readyForWish())

        // testing with your name
        subject.yourName = "Me"
        assertEquals(true, subject.readyForWish())
    }

    /**
     * Test that a wish is generated
     */
    @Test
    fun testGenerateWish(){
        subject.theirName = "Joseph Person"
        subject.noSignature = true
        subject.generateWish()
        assertNotEquals("", subject.currentWish)
    }

}