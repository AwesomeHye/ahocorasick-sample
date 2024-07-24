package dev.elsboo.ahocorasicksample.autocomplete

import org.slf4j.LoggerFactory

abstract class AutocompleteService(val keywords: List<String>, val searchKeywords: List<String>) {
    val log = LoggerFactory.getLogger(AutocompleteService::class.java)

    abstract fun index()

    protected fun indexWithDuration(name: String) {
        log.info("[$name] Indexing started...")
        val startTime = System.currentTimeMillis()
        index()
        val endTime = System.currentTimeMillis()
        log.info("[$name] Index finished. Duration: ${endTime - startTime} ms")
    }

    protected fun performWithDuration(name: String) {
        log.info("[$name] Perform started...")
        val startTime = System.currentTimeMillis()
        perform()
        val endTime = System.currentTimeMillis()
        log.info("[$name] Perform finished. Duration: ${endTime - startTime} ms")
    }

    abstract fun perform()
}
