package dev.elsboo.ahocorasicksample

import dev.elsboo.ahocorasicksample.autocomplete.AhocorasickAutoCompleteService
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
        val keywords = Files.readAllLines(Paths.get("src/main/resources/autocomplete_keyword.txt"))
        log.info("색인 키워드: ${keywords.size} 개")
        val searchKeywords = randomFromEdgeNgramKeywords(keywords, 1000)
        /* 아호코라식 */
        printMemoryUsage()
        val ahocorasickAutoCompleteService = AhocorasickAutoCompleteService(keywords, searchKeywords)
        ahocorasickAutoCompleteService.indexWithDuration()
        ahocorasickAutoCompleteService.performWithDuration()
        printMemoryUsage()

        Thread.sleep(3000L)
        println()

        /* 엘라스틱서치 */
        printMemoryUsage()
        val elasticSearchAutoCompleteService = ElasticSearchAutoCompleteService(keywords, searchKeywords)
        elasticSearchAutoCompleteService.indexWithDuration()
        elasticSearchAutoCompleteService.performWithDuration()
        printMemoryUsage()

    }

    /**
     * 검색어 edge_ngram 중 랜덤으로 검색어 선택
     */
    private fun randomFromEdgeNgramKeywords(keywords: List<String>, count: Int): List<String> {
        val edgeNgramKeywords = ArrayList<String>()
        for (keyword in keywords) {
            for (i in keyword.indices) {
                edgeNgramKeywords.add(keyword.substring(0, i + 1))
            }
        }
        return edgeNgramKeywords.shuffled().take(count)
    }

    private fun printMemoryUsage() {
        val runtime = Runtime.getRuntime()
        runtime.gc()
        val mb = 1024 * 1024
        log.info("Used Memory: ${(runtime.totalMemory() - runtime.freeMemory()) / mb} mb")
    }
}

fun main(args: Array<String>) {
    runApplication<AhocorasickSampleApplication>(*args)
}
