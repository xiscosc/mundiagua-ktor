package com.xsc

import com.auth0.jwk.UrlJwkProvider
import com.typesafe.config.ConfigFactory
import com.xsc.config.DatabaseFactory
import com.xsc.api.v1.customerApiV1
import com.xsc.service.customer.CustomerService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.text.DateFormat

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(StatusPages) {

    }


    install(Authentication) {
        jwt {
            verifier(UrlJwkProvider(ConfigFactory.load().getString("auth0.issuer")))
            validate { credential ->
                val payload = credential.payload
                if (payload.audience.contains(ConfigFactory.load().getString("auth0.audience"))) {
                    JWTPrincipal(payload)
                } else {
                    null
                }
            }
        }
    }

    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    DatabaseFactory.init()

    // Services
    val customerService = CustomerService()

    routing {
        authenticate { }
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }


        customerApiV1(customerService)
    }
}

