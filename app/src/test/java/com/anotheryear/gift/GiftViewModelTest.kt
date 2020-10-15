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
    fun getKeywords_listOfKeywords_returnAll() {
        val map = hashMapOf("Art" to "Art", "Electronics" to "Electronics")
        subject.keywords = map

        val expected = listOf("Art", "unisex", "Electronics").sorted()

        assertEquals(expected, subject.getKeywords().sorted())
    }

    @Test
    fun getKeywordsArray_arrayListOfKeywords_returnAllAsKeywordArray() {
        val map = hashMapOf("Art" to "Art", "Recipient" to "unisex", "Electronics" to "Electronics")
        subject.keywords = map

        val expected = arrayListOf(Keyword("Electronics"), Keyword("Art"), Keyword("unisex"))

        assertEquals(expected, subject.getKeywordsArray())
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
        assertEquals("unisex", subject.getRecipient())
    }

    @Suppress("DEPRECATION")
    @Test
    fun getBudget_noBudget_returnDefault(){
        subject.noBudget = true
        val expected = arrayOf(Float.MIN_VALUE, Float.MAX_VALUE)

        assertEquals(expected, subject.getBudget())
    }

    @Suppress("DEPRECATION")
    @Test
    fun getBudget_hasBudget_returnBudget(){
        subject.noBudget = false
        subject.budgetMin = 30f
        subject.budgetMax = 100f

        val expected = arrayOf(30f, 100f)

        assertEquals(expected, subject.getBudget())
    }

    @Test
    fun updateKeywords_keywordArray_updatedKeywordHashmap(){
        // Original keywords
        val originalKeywords = hashMapOf("Art" to "Art", "Electronics" to "Electronics")
        subject.keywords = originalKeywords
        val newKeywords = arrayListOf(Keyword("unisex"), Keyword("Electronics"))

        subject.updateKeywords(newKeywords)

        val expectedKeywords =  hashMapOf("Electronics" to "Electronics")
        assertEquals(expectedKeywords, subject.keywords)
    }
}