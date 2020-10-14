package com.anotheryear.gift

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class GiftViewModelTest {

    private lateinit var subject: GiftViewModel

    @Before
    fun setUp() {
        subject = GiftViewModel()
    }

    @Test
    fun listsKeywords() {
        val map = hashMapOf<String, String>("Art" to "Art", "Other" to "Video Games")
        subject.keywords = map
        assertEquals(listOf("Art", "Video Games"), subject.getKeywords())
    }

    @Test
    fun recipientTest() {
        subject.age = "Child"
        subject.gender = "M"
        assertEquals("boys", subject.getRecipient())

        subject.age = "Adult"
        subject.gender = ""
        assertEquals("unisex_adults", subject.getRecipient())

        subject.age = ""
        subject.gender = "F"
        assertEquals("women", subject.getRecipient())

        subject.age = ""
        subject.gender = ""
        assertEquals("not_specified", subject.getRecipient())
    }
}