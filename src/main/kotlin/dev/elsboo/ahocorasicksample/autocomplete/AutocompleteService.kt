package dev.elsboo.ahocorasicksample.autocomplete

import org.slf4j.LoggerFactory

abstract class AutocompleteService(val keywords: List<String>, val searchKeywords: List<String>) {
    val log = LoggerFactory.getLogger(AutocompleteService::class.java)

    abstract fun index()

    fun performWithDuration() {
        val startTime = System.currentTimeMillis()
        perform()
        val endTime = System.currentTimeMillis()
        log.info("Duration: ${endTime - startTime} ms")
    }

    abstract fun perform()
}
