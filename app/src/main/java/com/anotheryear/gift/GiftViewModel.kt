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
        keywords["Recipient"] = getRecipient()
        return keywords.values.toList()
    }

    /**
     * Returns an array of the min and max budget
     */
    fun getBudget(): Array<Float>{
        return if (noBudget)
            arrayOf(
                Float.MIN_VALUE, Float.MAX_VALUE)
        else{
            arrayOf(budgetMin, budgetMax)
        }
    }

    /**
     * Return string for type of recipient based on age and gender
     */
    fun getRecipient(): String {
        if(age == "Baby"){
            if(gender == "M"){
                return "baby_boys"
            } else if(gender == "F"){
                return "baby_girls"
            }
            else{
                return "babies"
            }
        } else if(age == "Child"){
            if(gender == "M"){
                return "boys"
            } else if(gender == "F"){
                return "girls"
            }
            else{
                return "children"
            }
        } else if(age == "Teen"){
            if(gender == "M"){
                return "teen_boys"
            } else if(gender == "F"){
                return "teen_girls"
            }
            else{
                return "teens"
            }
        } else if(age == "Adult"){
            if(gender == "M"){
                return "men"
            } else if(gender == "F"){
                return "women"
            }
            else{
                return "unisex_adults"
            }
        } else {
            if(gender == "M"){
                return "men"
            } else if(gender == "F"){
                return "women"
            }
            else{
                return "not_specified"
            }
        }
    }


}