# The Clojure paradox

Several years ago, I came across The [Python Paradox](https://paulgraham.com/pypar.html) by Paul Graham. In it, Graham argues that **Lisp and Python programmers are often those who genuinely care about programming** and, as a result, tend to do it better than, for instance, Java programmers. This perspective fundamentally changed how I viewed Python as a language.

At first, I found Python’s lack of semicolons and reliance on indentation to be strange and uncomfortable. I even saw Python as a tool for building only basic applications. However, **with the rise of serverless computing, machine learning, and data science, the immense power of Python has become increasingly apparent**. The language is getting faster, and its ecosystem is rapidly growing. Libraries like FastAPI and Pandas are truly remarkable, allowing us to solve problems succinctly. 

`As programmers, our job is to solve problems, and since we read more code than we write, having fewer lines of code reduces the surface area for bugs to hide and helps us avoid cognitive overload.
`

When I started working with AWS's Boto3, **I realized that tasks that previously took me 30 lines of Java could now be done in just 3 lines of Python**. It was mind-blowing. Don’t get me wrong, Java is still one of my favorite programming languages, and with its new release cadence, it’s only getting better. But the amount of ceremony required to accomplish basic tasks in Java is something sometimes we’d all prefer to avoid.

Recently, I've been experimenting with Go. Although it prides itself on simplicity, IMHO *I find it too verbose*. I know that excellent tools have been built with Go, and there are certain ideas and applications that should be developed with it. **Its compilation speed and efficient memory usage make Go a strong contender**, it might even be the best combination of developer experience and performance, which is becoming increasingly important in modern, cloud-native applications.


![Clojure 1](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/bzeomju06z8bdhfy57m1.png)



However, after 10 years in the industry and having deployed applications in several languages, I remain a fan of Clojure. Despite its niche status, Clojure incorporates ideas from other languages, such as Go’s goroutines. It’s a Lisp dialect, inherently immutable, and designed with concurrency in mind. What stands out most to me is how Clojure allows you to **focus on solving problems without the burden of unnecessary ceremony**. The majority of the code is about the problem itself; it’s data-oriented, and I often find that it helps me enter a *Flow state* (Happiness) where programming becomes truly enjoyable.

With Go, I currently have mixed feelings. While it brings many good ideas to the table in terms of concurrency and simplicity, I find that the codebases tend to be larger and more ceremonious. Clojure, on the other hand, tends to produce code that is less brittle and primarily composed of pure functions.

## Timeless ideas

I’ve always been a fan of timeless ideas because they are often the most important and foundational, yet they are also the most overlooked and I feel that Clojure fully embraces of all them.

- Programs composed mostly of pure functions are more robust and easier to test.
- Immutability reduces complexity.
- Smaller programs have fewer bugs.
- Software development is fundamentally about composition.
- We should minimize 
- Incidental complexity can make a system harder to understand, maintain, and extend.

## Code examples

In this example, I read a book from an online text file and perform basic processing to illustrate how the same problem can be approached in different programming languages and paradigms.

### Go
**My opinion**: The absence of `sets` and `higher-order functions` makes the problem more difficult to solve. Basic tasks, such as filtering, often need to be done in an imperative manner.

```go
package main

import (
	"fmt"
	"io"
	"net/http"
	"regexp"
	"sort"
	"strings"
)

var commonWords = map[string]struct{}{
	"a": {}, "able": {}, "about": {}, "across": {}, "after": {}, "all": {}, "almost": {}, "also": {}, "am": {}, "among": {},
	"an": {}, "and": {}, "any": {}, "are": {}, "as": {}, "at": {}, "be": {}, "because": {}, "been": {}, "but": {}, "by": {},
	"can": {}, "cannot": {}, "could": {}, "dear": {}, "did": {}, "do": {}, "does": {}, "either": {}, "else": {}, "ever": {},
	"every": {}, "for": {}, "from": {}, "get": {}, "got": {}, "had": {}, "has": {}, "have": {}, "he": {}, "her": {}, "hers": {},
	"him": {}, "his": {}, "how": {}, "however": {}, "i": {}, "if": {}, "in": {}, "into": {}, "is": {}, "it": {}, "its": {},
	"just": {}, "least": {}, "let": {}, "like": {}, "likely": {}, "may": {}, "me": {}, "might": {}, "most": {}, "must": {},
	"my": {}, "neither": {}, "no": {}, "nor": {}, "not": {}, "of": {}, "off": {}, "often": {}, "on": {}, "only": {}, "or": {},
	"other": {}, "our": {}, "own": {}, "rather": {}, "said": {}, "says": {}, "she": {}, "should": {}, "since": {}, "so": {},
	"some": {}, "than": {}, "that": {}, "the": {}, "their": {}, "them": {}, "then": {}, "there": {}, "these": {}, "they": {},
	"this": {}, "those": {}, "through": {}, "to": {}, "too": {}, "more": {}, "upon": {}, "us": {}, "wants": {}, "was": {},
	"we": {}, "were": {}, "what": {}, "when": {}, "where": {}, "which": {}, "while": {}, "who": {}, "whom": {}, "why": {},
	"will": {}, "with": {}, "would": {}, "yet": {}, "you": {}, "your": {}, "shall": {}, "before": {}, "now": {}, "one": {},
	"even": {},
}

func getBook() string {
	resp, err := http.Get("https://www.gutenberg.org/cache/epub/84/pg84.txt")
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		panic(err)
	}
	return string(body)
}

func getWords(book string) []string {
	re := regexp.MustCompile(`[\w’]+`)
	return re.FindAllString(book, -1)
}

func filterWords(words []string) []string {
	var result []string
	for _, word := range words {
		w := strings.ToLower(word)
		_, ok := commonWords[w]
		if !ok {
			result = append(result, w)
		}
	}
	return result
}

func getFrequentWords(words []string, n int) map[string]int {
	var filteredWords []string
	for _, word := range words {
		_, ok := commonWords[word]
		if !ok {
			filteredWords = append(filteredWords, strings.ToLower(word))
		}
	}
	var unorderedWords = make(map[string]int)
	for _, word := range words {
		unorderedWords[word]++
	}
	type wordFrequency struct {
		word  string
		count int
	}
	var wordFrequencies []wordFrequency
	for word, count := range unorderedWords {
		wordFrequencies = append(wordFrequencies, wordFrequency{word, count})
	}
	sort.Slice(wordFrequencies, func(i, j int) bool {
		return wordFrequencies[i].count > wordFrequencies[j].count
	})

	topN := make(map[string]int)
	for i := 0; i < len(wordFrequencies) && i < n; i++ {
		topN[wordFrequencies[i].word] = wordFrequencies[i].count
	}

	return topN
}

func main() {
	book := getBook()
	words := getWords(book)
	filteredWords := filterWords(words)
	fmt.Println("Total words:", len(words))
	fmt.Println("Frequent words:", getFrequentWords(filteredWords, 10))

}

```
### Java

**My opinion**: It does the job. Since Java 8, the language has been getting better. Even though it has some verbosity, you'll find that we now have collectors and functions to perform tasks without issues. The tedious part is having to put everything into classes just to solve a problem.


```java
package jorgetovar.book;

import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Book {

    private static final Set<String> commonWords = Set.of(
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
    );

    public static String getBook() {
        RestTemplate restTemplate = new RestTemplate();
        String bookUrl = "https://www.gutenberg.org/cache/epub/84/pg84.txt";
        return restTemplate.getForObject(bookUrl, String.class);
    }

    public static List<String> getWords(String book) {
        List<String> words = new ArrayList<>();
        Pattern wordPattern = Pattern.compile("[\\w’]+");
        Matcher matcher = wordPattern.matcher(book);
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }

    public static List<Map.Entry<String, Long>> getFrequentWords(List<String> words, int takeN) {
        return words.stream()
                .map(String::toLowerCase)
                .filter(word -> !commonWords.contains(word))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(takeN)
                .map(e -> Map.entry(e.getKey(), e.getValue()))
                .toList();
    }

    public static Map<Integer, List<String>> getLongestWords(List<String> words, int takeN) {
        return words.stream()
                .map(String::toLowerCase)
                .distinct()
                .sorted(getLongestWord())
                .limit(takeN)
                .collect(Collectors.groupingBy(String::length));
    }

    public static boolean isPalindrome(String word) {
        return word.contentEquals(new StringBuilder(word).reverse());
    }

    public static List<String> getLongestPalindromes(List<String> words, int takeN) {
        return words.stream()
                .map(String::toLowerCase)
                .filter(word -> !commonWords.contains(word))
                .distinct()
                .filter(Book::isPalindrome)
                .sorted(getLongestWord())
                .limit(takeN)
                .toList();
    }

    private static Comparator<String> getLongestWord() {
        return Comparator.comparingInt(String::length).reversed();
    }

    public static void main(String[] args) {
        String book = getBook();
        List<String> words = getWords(book);
        System.out.println("Total Words: " + words.size());
        System.out.println("Most Frequent Words:");
        getFrequentWords(words, 10).forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("\nLongest Words Grouped by Length:");
        getLongestWords(words, 10).forEach((length, group) -> System.out.println("Length " + length + ": " + group));

        System.out.println("\nLongest Palindromes:");
        getLongestPalindromes(words, 3).forEach(System.out::println);
    }


}

```
### Kotlin

**My opinion**: For me, this could be the most fun and robust enterprise language. It has good support for functions and immutability.


```kotlin
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

```
### Python
**My opinion**: I really like using this language. Sometimes it can get messy because it's too permissive and allows you to mutate variables, etc. But in general, you'll find that list comprehensions are really good for solving these kinds of problems. I don’t like the result when using classes, but for this example, it was just enough.


```python
import requests
import re

from collections import Counter, defaultdict


def get_book():
    book = requests.get("https://www.gutenberg.org/cache/epub/84/pg84.txt")
    return book.text


def get_words(book):
    return re.findall(r"[a-zA-Z0-9’]+", book)


common_words = {
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
}


def get_frequent_words(words, take_n):
    frequent_words = [word.lower() for word in words if word.lower() not in common_words]
    word_frequencies = Counter(frequent_words)
    return word_frequencies.most_common(take_n)


def get_longest_words(words, take_n):
    unique_words = set(word.lower() for word in words)
    longest_groups = defaultdict(list)
    sorted_works = sorted(unique_words, key=len, reverse=True)[:take_n]
    for word in sorted_works:
        longest_groups[len(word)].append(word)
    return dict(longest_groups)


def is_palindrome(word):
    return word == word[::-1]


def get_longest_palindromes(words, take_n):
    unique_words = set(word.lower() for word in words if word.lower() not in common_words)
    palindromes = [word for word in unique_words if is_palindrome(word)]
    palindromes.sort(key=len, reverse=True)
    return palindromes[:take_n]


def main():
    book = get_book()
    words = get_words(book)
    print("Total words:", len(words))
    print(get_frequent_words(words, 10))
    print(get_longest_words(words, 10))
    print(get_longest_palindromes(words, 3))


if __name__ == "__main__":
    main()

```
### Clojure

**My opinion**: The problem with Clojure is its niche nature. It’s usually difficult to understand the basics of the language and its philosophy. The amount of parentheses is unattractive to a lot of people, but in general, I find it the most beautiful implementation.

```clojure

(ns clojure-book.core
  [:require [clojure.string :as str]]
  (:gen-class))

(def book (slurp "https://www.gutenberg.org/cache/epub/84/pg84.txt"))

(def words (re-seq #"[\w|’]+" book))

(def common-words
  #{"a" "able" "about" "across" "after" "all" "almost" "also" "am" "among" "an"
    "and" "any" "are" "as" "at" "be" "because" "been" "but" "by" "can" "cannot"
    "could" "dear" "did" "do" "does" "either" "else" "ever" "every" "for" "from"
    "get" "got" "had" "has" "have" "he" "her" "hers" "him" "his" "how" "however"
    "i" "if" "in" "into" "is" "it" "its" "just" "least" "let" "like" "likely"
    "may" "me" "might" "most" "must" "my" "neither" "no" "nor" "not" "of" "off"
    "often" "on" "only" "or" "other" "our" "own" "rather" "said" "says" "she"
    "should" "since" "so" "some" "than" "that" "the" "their" "them" "then"
    "there" "these" "they" "this" "those" "through" "to" "too" "more" "upon"
    "us" "wants" "was" "we" "were" "what" "when" "where" "which" "while" "who"
    "whom" "why" "will" "with" "would" "yet" "you" "your" "shall" "before" "now" "one"
    "even"
    })

(defn palindrome? [word]
  (= (seq word) (reverse (seq word)))
  )

(defn frequent-words [take-n]
  (->> words
       (map str/lower-case)
       (remove common-words)
       (frequencies)
       (sort-by val)
       (take-last take-n))
  )

(defn longest-words [take-n]
  (->> words
       (map str/lower-case)
       (distinct)
       (sort-by count)
       (take-last take-n)
       (group-by count)
       )
  )

(defn longest-palindromes [take-n]
  (->> words
       (map str/lower-case)
       (distinct)
       (filter palindrome?)
       (sort-by count)
       (take-last take-n)
       )
  )

(defn -main
  [& args]
  (println (str "Total words:" (count words)))
  (println (take 10 words))
  (println (frequent-words 10))
  (println (longest-words 10))
  (println (longest-palindromes 3))

  )

```

## Conclusion

Software is constantly evolving, and client expectations for the programs they use and build are growing. However, our focus should remain on solving problems, eliminating incidental complexity, and taking pride in our craft. There is no 'best' programming language—only tools that help us address specific problems. Even when working with legacy systems, we have the opportunity to make a positive impact through good naming conventions, best practices, improving the architecture, and generally putting the project in a better state

**There has never been a better time to be an engineer and create value in society through software.**

- [LinkedIn](https://www.linkedin.com/in/jorgetovar-sa)
- [Twitter](https://twitter.com/jorgetovar621)
- [GitHub](https://github.com/jorgetovar)

If you enjoyed the articles, visit my blog [jorgetovar.dev](jorgetovar.dev)