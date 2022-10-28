package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{BankAccountEntity, BankAccounts, CreditCardEntity, CreditCards}

/** 
 *  Created by Nursultan on 7/18/2022
 */

class BankAccountService {

    val bankAccounts = BankAccounts.query

    def create(x: BankAccountEntity) = DB.write { implicit session =>
        bankAccounts.insert(x)    
    }

}