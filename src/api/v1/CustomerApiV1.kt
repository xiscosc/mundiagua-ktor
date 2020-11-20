package com.xsc.api.v1

import com.xsc.api.v1.response.ListResponse
import com.xsc.service.customer.CustomerService
import com.xsc.service.model.Customer
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.*

fun Routing.customerApiV1(customerService: CustomerService) {
    route("/api/v1/customer") {
        get {
            val response = coroutineScope {
                val count = async(Dispatchers.IO) { customerService.getCustomerCount() }
                val customers = async(Dispatchers.IO) { customerService.getAllCustomers() }
                ListResponse(count.await(), customers.await())
            }

            call.respond(response)
        }

        post {
            val customer = withContext(Dispatchers.IO) { call.receive<Customer>() }
            call.respond(customerService.saveCustomer(customer))
        }

        get("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            call.respond(customerService.getCustomer(id) ?: "")
        }

        patch("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val customer = withContext(Dispatchers.IO) { call.receive<Customer>() }
            call.respond(customerService.updateCustomer(id, customer) ?: "")
        }
    }
}