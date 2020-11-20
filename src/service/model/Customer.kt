package com.xsc.service.model

import com.xsc.db.CustomerEntity
import java.util.*

data class Customer(
    val uuid: UUID? = null,
    val oldId: Int? = null,
    val name: String,
    val internalCode: String? = null,
    val paymentMethod: String? = null,
    val nationalId: String? = null,
    val notes: String? = null
) {
    companion object {
        fun fromEntity(entity: CustomerEntity): Customer {
            return Customer(
                entity.id.value,
                entity.oldId,
                entity.name,
                entity.internalCode,
                entity.paymentMethod,
                entity.nationalId,
                entity.notes
            )
        }
    }
}
