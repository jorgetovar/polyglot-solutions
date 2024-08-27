import unicodedata

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
