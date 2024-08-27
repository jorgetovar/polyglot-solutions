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
