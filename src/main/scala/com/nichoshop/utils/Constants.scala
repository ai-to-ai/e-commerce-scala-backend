package com.nichoshop.utils

 /**
  * Created by Nursultan on 6/30/2022
  */

object Constants {

    val categoryDepth : Int = 4
    val tokenSecret : String = "NICHOSHOP2022NUR" // Token secret key
    val tokenExpire : Int = 60 * 60 * 1 // Token expire hour : 1
    val randomNameCheckIteration = 10
    val signupExpire = 7200000
    val itemPhotoSize = 3*1024*1024

    object sucExpire {
        val adminTemp = 600000
        val account = 1000 * 60 *60 * 24
        val addPhone = 3600 * 24 * 1000
        val tempPass = 3600 * 24 * 1000
        val forgotPass = 3600 * 24 * 1000
    }

    object sucType {
        val forgotEmail = 0   
        val forgotText = 1
        val tempEmail = 2
        val tempText = 3
        val addPhone = 4
        val adminTemp = 5
        val account = 6
    }

    object AccountType {
        val personal = 0
        val business = 1
        val system = 2
    }

    object Condition {
        val `new` : String = "New"
        val used : String = "Used"
        val new_other : String = "New other"
        val new_other_see_details : String = "New other (see details)"
        val new_open_box : String = " New-open box"
        val new_with_tags : String = "New with tags"
        val new_without_tags : String = "New without tags"
        val new_with_box : String = "New with box"
        val new_without_box : String = "New without box"
        val new_with_defects : String = "New with defects"
        val brand_new : String = "Brand New"
        val like_new : String = "Like New"
        val open_box : String = "Open box"
        val pre_owned : String = "Pre-owned"
        val very_good : String = "Very Good"
        val good : String = "Good"
        val accessible : String = "Accessible"
        val certified_pre_owned : String = "Certified pre-owned"
        val certified_refurbished : String = "Certified refurbished"
        val seller_refurbished : String = "Seller refurbished"
        val remanufactured : String = "Remanufactured"
        val retread : String = "Retread"
        val for_parts_or_not_working : String = "For parts or not working"
        val damaged : String = "Damaged"
    }
    
    object MessageType {
        val general : Int = 0
        val reportBuyer : Int = 1
        val reportSeller : Int = 2
        val cancelOrder : Int = 3
        val refundOrder : Int = 4
        val contactBuyer : Int = 5
        val cancelItem : Int = 6
    }
    object BankAccountType {
        val IBAN : Int = 0
        val GB : Int = 1
        val US : Int = 2
        val CA : Int = 3
        val OTHER : Int = 4
    }
}