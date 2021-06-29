package com.bankapi.bank.service

import com.bankapi.bank.model.Account
import com.bankapi.bank.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class AccountServiceImpl(private val repository: AccountRepository) : AccountService {
    override fun create(account: Account): Account {
        Assert.hasLength(account.name, "Ops! [nome] esta em branco!")
        return repository.save(account)
    }

    override fun getAll(): List<Account> {
        return repository.findAll()
    }

    override fun getbyID(id: Long): Optional<Account> {
       return repository.findById(id)
    }

    override fun update(id: Long, account: Account): Optional<Account> {
        val optional = getbyID(id)

        if(optional.isPresent) Optional.empty<Account>()

        return optional.map {
            val accountToUpdater = it.copy(
                name = account.name,
                document = account.document,
                phone = account.phone
            )
            repository.save(accountToUpdater)
        }



    }

    override fun delete(id: Long) {
        repository.findById(id).map {
            repository.delete(it)
        }.orElseThrow { throw RuntimeException("Id not found $id")}
    }


}