package dev.elsboo.ahocorasicksample.autocomplete

import org.ahocorasick.trie.Trie

class AhocorasickAutoCompleteService(keywords: List<String>, searchKeywords: List<String>) : AutocompleteService(keywords, searchKeywords) {
    private lateinit var trie: Trie;

    override fun index() {
        val trieBuilder = Trie.builder()
        keywords.forEach { keyword ->
            trieBuilder.addKeyword(keyword)
        }
        trie = trieBuilder.build()
    }


    override fun perform() {
        for (searchKeyword in searchKeywords) {
            val emits = trie.parseText(searchKeyword)
            log.info("검색어: $searchKeyword")
            emits.forEach {
                log.info("노출된 키워드: [${it.keyword}] at index: ${it.start}")
            }
        }

    }
}
