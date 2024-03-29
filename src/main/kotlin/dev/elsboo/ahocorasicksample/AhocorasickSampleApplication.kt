package dev.elsboo.ahocorasicksample

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AhocorasickSampleApplication : CommandLineRunner {
    private val log = LoggerFactory.getLogger(AhocorasickSampleApplication::class.java)

    override fun run(vararg args: String?) {
        log.info("Hello, World!")

    }
}

fun main(args: Array<String>) {
    runApplication<AhocorasickSampleApplication>(*args)
}
