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
