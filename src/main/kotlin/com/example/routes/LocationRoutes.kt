package com.example.routes

import com.example.models.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.io.FileWriter


fun Route.locationRouting() {

    route("/report") {
        post("/savedata") {
            println("Received a report")
            // val data = call.receive<Request>() // in case we receive one piece of data instead of a list
            val data = call.receive<List<Request>>()

            val mapper = jacksonObjectMapper()

            val file = File("log.json")

            // in this case we are sending all data at once
            val fileWriter = FileWriter(file, false)

            val jsonData = mapper.writeValueAsString(data)

            fileWriter.write("${jsonData}\n")
            fileWriter.close()

            call.respondText("Data saved successfully", status = HttpStatusCode.OK)
        }

        get("/getdata") {
            println("Received a request for reading a report")
            val mapper = jacksonObjectMapper()
            val file = javaClass.getResource("/log.json").openStream()
            val jsonString = mapper.readValue<List<Request>>(file)

            if(jsonString.isNotEmpty()) {
                call.respond(jsonString)
            } else {
                call.respondText("No data found", status = HttpStatusCode.NotFound)
            }
        }

    }
}