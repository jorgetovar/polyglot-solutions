package jorgetovar.book

import org.springframework.web.client.RestTemplate


val commonWords = setOf(
    "a", "able", "about", "across", "after", "all", "almost", "also", "am", "among", "an",
    "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot",
    "could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from",
    "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", "however",
    "i", "if", "in", "into", "is", "it", "its", "just", "least", "let", "like", "likely",
    "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off",
    "often", "on", "only", "or", "other", "our", "own", "rather", "said", "says", "she",
    "should", "since", "so", "some", "than", "that", "the", "their", "them", "then",
    "there", "these", "they", "this", "those", "through", "to", "too", "more", "upon",
    "us", "wants", "was", "we", "were", "what", "when", "where", "which", "while", "who",
    "whom", "why", "will", "with", "would", "yet", "you", "your", "shall", "before", "now", "one",
    "even"
)

fun getBook(): String {
    val restTemplate = RestTemplate()
    val bookUrl = "https://www.gutenberg.org/cache/epub/84/pg84.txt"
    return restTemplate.getForObject(bookUrl, String::class.java) ?: ""
}

fun getWords(book: String): List<String> {
    return "[\\w’]+".toRegex().findAll(book).map { it.value }.toList()
}

fun getFrequentWords(words: List<String>, takeN: Int): List<Pair<String, Int>> {
    val filteredWords = words
        .map { it.lowercase() }
        .filter { it !in commonWords }

    return filteredWords
        .groupingBy { it }
        .eachCount()
        .toList()
        .sortedByDescending { it.second }
        .take(takeN)

}

fun getLongestWords(words: List<String>, takeN: Int): Map<Int, List<String>> {
    val uniqueWords = words
        .map { it.lowercase() }
        .distinct()
    return uniqueWords
        .sortedByDescending { it.length }
        .take(takeN)
        .groupBy { it.length }
}

fun isPalindrome(word: String): Boolean {
    return word == word.reversed()
}

fun getLongestPalindromes(words: List<String>, takeN: Int): List<String> {
    val uniqueWords = words
        .map { it.lowercase() }
        .filter { it !in commonWords }
        .distinct()
    val palindromes = uniqueWords
        .filter { isPalindrome(it) }
    return palindromes
        .sortedByDescending { it.length }.take(takeN)
}


fun main() {
    val book = getBook()
    val words = getWords(book)
    println("Total Words: ${words.size}")
    println("Most Frequent Words:")
    println(getFrequentWords(words, 10))
    println("Longest Words Grouped by Length:")
    println(getLongestWords(words, 5))
    println("Longest Palindromes:")
    println(getLongestPalindromes(words, 3))
}
