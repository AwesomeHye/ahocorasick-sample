package dev.elsboo.ahocorasicksample.autocomplete

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Paths

class ElasticSearchAutoCompleteService(keywords: List<String>, searchKeywords: List<String>) : AutocompleteService(keywords, searchKeywords) {
    companion object {
        private const val logKey: String = "엘라스틱서치"
    }

    val httpClient = HttpClient.newHttpClient()
    val fileRootPath = Paths.get("src/main/resources/es_template")

    /**
     * POST autocomplete_elsboo/_bulk
     * {"index": {"_index": "autocomplete_elsboo", "_id": 1}}
     * {"keyword":"hers"}
     * {"index": {"_index": "autocomplete_elsboo", "_id": 2}}
     * {"keyword":"his"}
     *
     */
    override fun index() {
        // es bulk index
        val bulkIndexRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9201/autocomplete_elsboo/_bulk"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(bulkIndexBody()))
            .build()

        val httpResponse = httpClient.send(bulkIndexRequest, HttpResponse.BodyHandlers.ofString())
        log.debug(httpResponse.body())
    }

    private fun bulkIndexBody(): String {
        val bulkIndexTemplate = Files.readString(fileRootPath.resolve("bulk_index"))

        val stringBuilder = StringBuilder()
        for ((i, keyword) in keywords.withIndex()) {
            stringBuilder.append(String.format(bulkIndexTemplate, i + 1, keyword))
        }
        stringBuilder.append("\n")

        return stringBuilder.toString()
    }

    override fun perform() {
        // es search
        val searchUrl = "http://localhost:9201/autocomplete_elsboo/_search"
        val searchBodyTemplate = Files.readString(fileRootPath.resolve("search_template"))

        for (searchKeyword in searchKeywords) {
            log.debug("검색어: $searchKeyword")
            val searchRequestBody = HttpRequest.newBuilder()
                .uri(URI.create(searchUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.format(searchBodyTemplate, searchKeyword)))
                .build()

            val httpResponse = httpClient.send(searchRequestBody, HttpResponse.BodyHandlers.ofString())
            log.debug(httpResponse.body())
        }
    }

    fun indexWithDuration() {
        super.indexWithDuration(logKey)
    }

    fun performWithDuration() {
        super.performWithDuration(logKey)
    }
}
