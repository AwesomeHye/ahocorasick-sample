package dev.elsboo.ahocorasicksample

import dev.elsboo.ahocorasicksample.autocomplete.ElasticSearchAutoCompleteService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootApplication
class AhocorasickSampleApplication : CommandLineRunner {
    private val log = LoggerFactory.getLogger(AhocorasickSampleApplication::class.java)

    override fun run(vararg args: String?) {
        val keywords = Files.readAllLines(Paths.get("src/main/resources/autocomplete_keyword"))
        keywords.forEach(::println)
        val searchKeywords = Files.readAllLines(Paths.get("src/main/resources/search_keyword"))
        // 검색어는 랜덤으로?

        /*AhocorasickAutoCompleteService(keywords, searchKeywords).performWithDuration()
        Thread.sleep(5000L)
        ElasticSearchAutoCompleteService(keywords, searchKeywords).performWithDuration()*/

        ElasticSearchAutoCompleteService(keywords, searchKeywords).perform()
    }
}

fun main(args: Array<String>) {
    runApplication<AhocorasickSampleApplication>(*args)
}
