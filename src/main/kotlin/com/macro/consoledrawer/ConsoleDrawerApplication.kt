package com.macro.consoledrawer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class ConsoleDrawerApplication {
}

fun main(args: Array<String>) {
    runApplication<ConsoleDrawerApplication>(*args)
}
