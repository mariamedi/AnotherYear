package com.anotheryear.gift

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GiftResultListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var subject: GiftResultListViewModel

    @Before
    fun setUp() {
        subject = GiftResultListViewModel()
    }

    @Test
    fun loadKeywordsList_listOfKeywords_populatesKeywords() {
        val keywordList: List<String> = listOf("Toy", "unisex", "Art")
        subject.loadKeywords((keywordList))
        val value = subject.giftKeywordsLiveData.getOrAwaitValue()

        assertEquals(keywordList, value)
    }

    @Test
    fun loadKeywordsArrayList_listOfKeywords_populatesKeywords() {
        val keywordArrayList: ArrayList<Keyword> = arrayListOf(Keyword("Toy"), Keyword("unisex"), Keyword("Art"))
        subject.loadKeywords((keywordArrayList))
        val expectedKeywordList: List<String> = listOf("Toy", "unisex", "Art")
        val value = subject.giftKeywordsLiveData.getOrAwaitValue()

        assertEquals(expectedKeywordList, value)
    }

    @Test
    fun getKeywords_noEmpty_returnsAll() {
        val keywordList: List<String> = listOf("Toy", "unisex", "Art")
        subject.giftKeywordsLiveData.value = keywordList

        assertEquals(keywordList, subject.getKeywords())
    }

    @Test
    fun getKeywords_oneEmpty_returnsAllButEmpty() {
        val keywordList: List<String> = listOf("Toy", "", "Art")
        subject.giftKeywordsLiveData.value = keywordList

        val expectedFilteredKeywordList: List<String> = listOf("Toy", "Art")

        assertEquals(expectedFilteredKeywordList, subject.getKeywords())
    }
}