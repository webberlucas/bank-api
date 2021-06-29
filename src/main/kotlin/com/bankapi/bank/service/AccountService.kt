package com.bankapi.bank.service

import com.bankapi.bank.model.Account
import java.util.*

interface AccountService {

    fun create (account: Account): Account

    fun getAll(): List<Account>

    fun getbyID(id: Long): Optional<Account>

    fun update(id: Long, account: Account) : Optional<Account>

    fun delete(id: Long)

}