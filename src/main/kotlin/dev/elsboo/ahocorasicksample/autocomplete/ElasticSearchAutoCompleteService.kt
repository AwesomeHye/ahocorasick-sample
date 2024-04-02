package dev.elsboo.ahocorasicksample.autocomplete

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Paths

class ElasticSearchAutoCompleteService(keywords: List<String>, searchKeywords: List<String>) : AutocompleteService(keywords, searchKeywords) {
    val httpClient = HttpClient.newHttpClient()

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
        log.info(httpResponse.body())
    }

    private fun bulkIndexBody(): String {
        val bulkIndexTemplate = Files.readString(Paths.get("src/main/resources/bulk_index.json"))

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
        val searchBodyTemplate = Files.readString(Paths.get("src/main/resources/es_template/search_template"))

        for (searchKeyword in searchKeywords) {
            val searchRequestBody = HttpRequest.newBuilder()
                .uri(URI.create(searchUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.format(searchBodyTemplate, searchKeyword)))
                .build()

            val httpResponse = httpClient.send(searchRequestBody, HttpResponse.BodyHandlers.ofString())
            log.info(httpResponse.body())
        }
    }
}
