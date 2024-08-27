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
