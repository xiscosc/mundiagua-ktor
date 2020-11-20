package com.xsc.config

import com.typesafe.config.ConfigFactory
import com.xsc.db.table.customer.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource


object DatabaseFactory {

    fun init() {
        val dataSource = hikari()
        //migrate(dataSource)
        Database.connect(dataSource)
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create (Customers, Emails, Phones, Addresses, SubCustomers, SubAddresses, SubPhones, SubEmails)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = ConfigFactory.load().getString("db.url")
        config.maximumPoolSize = 5
        config.username = ConfigFactory.load().getString("db.username")
        config.password = ConfigFactory.load().getString("db.password")
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    private fun migrate(datasource: DataSource) {
        val f = Liquibase("db/changelog.yml", ClassLoaderResourceAccessor(), JdbcConnection(datasource.connection))
        f.update("")
        f.close()
    }
}