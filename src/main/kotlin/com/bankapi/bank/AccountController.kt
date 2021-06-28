package com.bankapi.bank

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/accounts")
class AccountController (private val repository: AccountRepository) {

    @PostMapping
    fun create (@RequestBody account: Account) = repository.save(account)

    @GetMapping
    fun getAll(): List<Account> = repository.findAll()

    @GetMapping("/{id}")
    fun getbyID(@PathVariable id: Long): ResponseEntity<Account> =
            repository.findById(id).map { ResponseEntity.ok(it)
            }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody account: Account) : ResponseEntity<Account> =
            repository.findById(id).map {
                val accountToUpdater = it.copy(
                        name = account.name,
                        document = account.document,
                        phone = account.phone
                )
                ResponseEntity.ok(repository.save(accountToUpdater))
            }.orElse(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) : ResponseEntity<Void> =
            repository.findById(id).map {
                repository.delete(it)
                ResponseEntity<Void>(HttpStatus.OK)
            }.orElse(ResponseEntity.notFound().build())


}