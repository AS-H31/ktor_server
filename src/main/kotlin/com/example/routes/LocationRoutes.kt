package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


fun Route.locationRouting() {
    route("/report") {
        post {
            println("Received a report")
            val data = call.receive<String>()
            File("log.txt").bufferedWriter().use { out -> out.write(data) }
            call.respondText("Data saved in ", status = HttpStatusCode.OK)
        }

        get {
            println("Received a request for reading a report")
            val file = File("log.txt")
            val fileContent = file.readText()
            call.respondText(fileContent, status = HttpStatusCode.OK)
        }
    }
}