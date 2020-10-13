package com.anotheryear.gift

import androidx.lifecycle.ViewModel

/**
 * View model to hold gift survey information across multiple fragments to be used when searching for a gift
 */
class GiftViewModel: ViewModel() {

    var budgetMin: Float = 0.0F
    var budgetMax: Float = 100F
    var noBudget: Boolean = false
    var gender: String = "" // "M" = Male, "F" = Female, "" = Skip/other
    var age: String = ""
    var keywords: HashMap<String, String> = hashMapOf()

    /**
     * Return list of keywords based on interests
     */
    fun getKeywords(): List<String> {
        return keywords.values.toList()
    }

    /**
     * Return string for type of recipient based on age and gender
     */
    fun getRecipient(): String {
        when (age) {
            "Baby" -> {
                return when (gender) {
                    "M" -> {
                        "baby_boys"
                    }
                    "F" -> {
                        "baby_girls"
                    }
                    else -> {
                        "babies"
                    }
                }
            }
            "Child" -> {
                return when (gender) {
                    "M" -> {
                        "boys"
                    }
                    "F" -> {
                        "girls"
                    }
                    else -> {
                        "children"
                    }
                }
            }
            "Teen" -> {
                return when (gender) {
                    "M" -> {
                        "teen_boys"
                    }
                    "F" -> {
                        "teen_girls"
                    }
                    else -> {
                        "teens"
                    }
                }
            }
            "Adult" -> {
                return when (gender) {
                    "M" -> {
                        "men"
                    }
                    "F" -> {
                        "women"
                    }
                    else -> {
                        "unisex_adults"
                    }
                }
            }
            else -> {
                return when (gender) {
                    "M" -> {
                        "men"
                    }
                    "F" -> {
                        "women"
                    }
                    else -> {
                        "not_specified"
                    }
                }
            }
        }
    }
}