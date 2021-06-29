package com.bankapi.bank

import com.bankapi.bank.model.Account
import com.bankapi.bank.repository.AccountRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired lateinit var mockMvc: MockMvc

    @Autowired lateinit var repository: AccountRepository

    @Test
    fun `test find all`(){
        repository.save(Account(name = "lucas", document = "123", phone = "99999999999"))

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").isString)
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].document").isString)
                .andExpect(MockMvcResultMatchers.jsonPath("\$[0].phone").isString)
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test find by id`(){
        val account = repository.save(Account(name = "lucas", document = "123", phone = "99999999999"))

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/${account.id}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(account.id))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create account`(){
        val account = Account(name = "Teste Create", document = "12345678901", phone = "99999999999")
        val json = ObjectMapper().writeValueAsString(account)
        repository.deleteAll()
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
                .andDo(MockMvcResultHandlers.print())

        Assertions.assertFalse(repository.findAll().isEmpty())
    }

    @Test
    fun `test update account`(){
        val account = repository
                .save(Account(name = "Teste Create", document = "12345678901", phone = "99999999999"))
                .copy(name = "update")
        val json = ObjectMapper().writeValueAsString(account)
        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/${account.id}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
                .andDo(MockMvcResultHandlers.print())

        val findById = repository.findById(account.id!!)
        Assertions.assertTrue(findById.isPresent)
        Assertions.assertEquals(account.name, findById.get().name)

    }

    @Test
    fun `test delete account`(){
        val account = repository
                .save(Account(name = "Teste Create", document = "12345678901", phone = "99999999999"))
        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/${account.id}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())

        val findById = repository.findById(account.id!!)
        Assertions.assertFalse(findById.isPresent)

    }
}