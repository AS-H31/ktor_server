package com.example.routes

import com.example.models.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
            val jsonString = call.receiveText()
            val gson = Gson()
            val data = gson.fromJson(jsonString, ArrayList::class.java) as ArrayList<Request>

            val file = File("log.json")

            val fileWriter = FileWriter(file, false)
            val jsonData = gson.toJson(data)

            fileWriter.write("${jsonData}\n")
            fileWriter.close()

            call.respondText("Data saved successfully", status = HttpStatusCode.OK)
        }

        get("/getdata") {
            println("Received a request for reading a report")
            val mapper = jacksonObjectMapper()
            val file = File("log.json")
            val jsonString = mapper.readValue<List<Request>>(file)

            if(jsonString.isNotEmpty()) {
                call.respond(jsonString)
            } else {
                call.respondText("No data found", status = HttpStatusCode.NotFound)
            }
        }
    }
}