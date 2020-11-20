package com.xsc.db.table.customer

import org.jetbrains.exposed.dao.id.UUIDTable

object Customers : UUIDTable() {
    val oldId = integer("old_id").index(isUnique = true).autoIncrement()
    val name = varchar("name", 128)
    val internalCode = varchar("internal_code", 50).nullable()
    val paymentMethod = varchar("payment_method", 50).nullable()
    val nationalId = varchar("national_id", 50).nullable()
    val notes = text("notes").nullable()
}

object Emails : UUIDTable() {
    val email = varchar("email", 100)
    val customer = reference("customer", Customers)
}

object Phones: UUIDTable() {
    val alias = varchar("alias", 50)
    val internationalCode = integer("international_code")
    val phone = integer("phone")
    val customer = reference("customer", Customers)
}

object Addresses: UUIDTable() {
    val alias = varchar("alias", 50)
    val address = text("address")
    val latitude = long("latitude").nullable()
    val longitude = long("longitude").nullable()
    val customer = reference("customer", Customers)
}

object SubCustomers : UUIDTable() {
    val name = varchar("name", 128)
    val paymentMethod = varchar("payment_method", 50).nullable()
    val nationalId = varchar("national_id", 50).nullable()
    val notes = text("notes").nullable()
    val customer = reference("customer", Customers)
}

object SubEmails : UUIDTable() {
    val email = varchar("email", 100)
    val subCustomer = reference("sub_customer", SubCustomers)
}

object SubPhones: UUIDTable() {
    val alias = varchar("alias", 50)
    val internationalCode = integer("international_code")
    val phone = integer("phone")
    val subCustomer = reference("sub_customer", SubCustomers)
}

object SubAddresses: UUIDTable() {
    val alias = varchar("alias", 50)
    val address = text("address")
    val latitude = long("latitude").nullable()
    val longitude = long("longitude").nullable()
    val subCustomer = reference("sub_customer", SubCustomers)
}