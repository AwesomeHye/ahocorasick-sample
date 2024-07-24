package dev.elsboo.ahocorasicksample.autocomplete

import org.ahocorasick.trie.PayloadTrie

class AhocorasickAutoCompleteService(keywords: List<String>, searchKeywords: List<String>) : AutocompleteService(keywords, searchKeywords) {
    companion object {
        private const val logKey: String = "아호코라식"
    }

    private lateinit var trie: PayloadTrie<String>

    override fun index() {
        val trieBuilder = PayloadTrie.builder<String>()
        keywords.forEach { keyword ->
            log.debug("색인할 단어: $keyword")
            trieBuilder.addKeyword(keyword)
            edgeNgramTokenizing(keyword).forEach { token ->
                log.debug("-- 토큰: $token")
                trieBuilder.addKeyword(token, keyword)
            }
        }
        trie = trieBuilder.build()
    }


    private fun edgeNgramTokenizing(keyword: String): List<String> {
        val tokens = ArrayList<String>()
        for (i in keyword.indices) {
            tokens.add(keyword.substring(0, i + 1))
        }
        return tokens
    }

    override fun perform() {
        for (searchKeyword in searchKeywords) {
            val emits = trie.parseText(searchKeyword)
            log.debug("검색어: $searchKeyword")
            /*emits.forEach {
                log.info("노출된 키워드: [${it.keyword} / ${if (it.payload != null) it.payload else it.keyword}] at [${it.start}, ${it.end}]")
            }*/
        }

    }

    fun indexWithDuration() {
        super.indexWithDuration(logKey)
    }

    fun performWithDuration() {
        super.performWithDuration(logKey)
    }
}
