package com.xsc.db

import com.xsc.db.table.customer.*
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class CustomerEntity(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<CustomerEntity>(Customers)
    var oldId by Customers.oldId
    var name     by Customers.name
    var internalCode by Customers.internalCode
    var paymentMethod by Customers.paymentMethod
    var nationalId by Customers.nationalId
    var notes by Customers.notes

    val subCustomers by SubCustomerEntity referrersOn SubCustomers.customer
    val emails by EmailEntity referrersOn Emails.customer
    val phones by PhoneEntity referrersOn Phones.customer
    val addresses by AddressEntity referrersOn Addresses.customer
}

class AddressEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AddressEntity>(Addresses)
    var alias     by Addresses.alias
    var address     by Addresses.address
    var latitude     by Addresses.latitude
    var longitude     by Addresses.longitude
    var customer by CustomerEntity referencedOn Addresses.customer
}

class SubAddressEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SubAddressEntity>(SubAddresses)
    var alias     by SubAddresses.alias
    var address     by SubAddresses.address
    var latitude     by SubAddresses.latitude
    var longitude     by SubAddresses.longitude
    var subCustomer by SubCustomerEntity referencedOn SubAddresses.subCustomer
}

class PhoneEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PhoneEntity>(Phones)
    var alias     by Phones.alias
    var phone     by Phones.phone
    var internationalCode     by Phones.internationalCode
    var customer by CustomerEntity referencedOn Phones.customer
}

class SubPhoneEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SubPhoneEntity>(SubPhones)
    var alias     by SubPhones.alias
    var phone     by SubPhones.phone
    var internationalCode     by SubPhones.internationalCode
    var subCustomer by SubCustomerEntity referencedOn SubPhones.subCustomer
}

class EmailEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EmailEntity>(Emails)
    var email     by Emails.email
    var customer by CustomerEntity referencedOn Emails.customer
}

class SubEmailEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SubEmailEntity>(SubEmails)
    var email     by SubEmails.email
    var subCustomer by SubCustomerEntity referencedOn SubEmails.subCustomer
}

class SubCustomerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SubCustomerEntity>(SubCustomers)
    var name     by SubCustomers.name
    var paymentMethod by SubCustomers.paymentMethod
    var nationalId by SubCustomers.nationalId
    var notes by SubCustomers.notes
    var customer by CustomerEntity referencedOn SubCustomers.customer

    val emails by SubEmailEntity referrersOn SubEmails.subCustomer
    val phones by SubPhoneEntity referrersOn SubPhones.subCustomer
    val addresses by SubAddressEntity referrersOn SubAddresses.subCustomer
}