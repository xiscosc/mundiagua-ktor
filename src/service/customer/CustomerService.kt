package com.xsc.service.customer

import com.xsc.db.CustomerEntity
import com.xsc.service.model.Customer
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class CustomerService() {

    suspend fun getCustomer(id: UUID): Customer? {
        val entity = newSuspendedTransaction { CustomerEntity.findById(id) }
        return entity?.let { Customer.fromEntity(it) }
    }

    suspend fun getAllCustomers(limit: Int? = null, offset: Int = 0): List<Customer> {
        val customerEntities = newSuspendedTransaction {
            limit?.let { CustomerEntity.all().limit(it, offset.toLong()).toList() } ?: CustomerEntity.all().toList()
        }

        return customerEntities.map { Customer.fromEntity(it) }
    }

    suspend fun getCustomerCount(): Int = newSuspendedTransaction { CustomerEntity.count().toInt() }

    suspend fun saveCustomer(customer: Customer): Customer {
        val customerEntity = newSuspendedTransaction {
            CustomerEntity.new {
                name = customer.name
                internalCode = customer.internalCode
                notes = customer.notes
                paymentMethod = customer.paymentMethod
                nationalId = customer.nationalId
            }
        }

        return Customer.fromEntity(customerEntity)
    }

    suspend fun updateCustomer(id: UUID, customer: Customer): Customer? {
        val customerEntity = newSuspendedTransaction {
            val entity = CustomerEntity.findById(id)
            entity?.let {
                it.name = customer.name
                it.internalCode = customer.internalCode
                it.notes = customer.notes
                it.paymentMethod = customer.paymentMethod
                it.nationalId = customer.nationalId
            }
            entity
        }

        return customerEntity?.let { Customer.fromEntity(it) }
    }

}