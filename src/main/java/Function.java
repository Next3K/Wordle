import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Utility class with useful functions
 */
public class Function {

    private static final Map<Character, Integer> lettersPositions = new HashMap<>(26);

    // fill map with mappings of alphabet letters positions
    static {
        char a = 'a';
        for (int i = 0; i < 26; i++) {
            lettersPositions.put((char) (a + i), i);
        }
    }

    /**
     * Read all words from the txt file
     * @return list of 5-letter words with unique characters
     */
    public static List<Wrapper> getAllWords() {
        List<Wrapper> list = new LinkedList<>();
        Set<Integer> usedSignatures = new HashSet<>(5000);
        File file = new File("src/main/resources/filtered-words.txt");
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("input file not found");
            System.exit(1);
        }

        while (input.hasNext()) {
            String text = input.next().toLowerCase();
            Wrapper wrapper = new Wrapper(text);
            int signature = wrapper.getSignature();
            if (!usedSignatures.contains(signature)) {
                usedSignatures.add(signature);
                list.add(wrapper);
            }
        }

        System.out.println("Number of unique words: " + list.size());
        return list;
    }

    /**
     * Check if text contains any letter that is in the set
     * @param str - source text
     * @param usedCharacters - set of characters
     * @return true if text contains at least one character that is in the set
     */
    public static boolean containsForbiddenCharacter(String str, Set<Character> usedCharacters) {
        for (int i = 0; i < str.length(); i++) {
            if (usedCharacters.contains(str.charAt(i))) return true;
        }
        return false;
    }

    /**
     * Traverse the list of wrappers that contain words and count occurrences of each character
     * @param tokens - list of wrappers with words to traverse
     * @return map containing info about the number of occurrences of each letter
     */
    public static Map<Character, Integer> getFrequencyMap(List<Wrapper> tokens) {
        Map<Character, Integer> letterFreq = new HashMap<>(26);
        char c = 'a';
        for (int i = 0; i < 26; i++) { // fill the map with alphabet
            letterFreq.put((char) (c + i), 0);
        }
        for (var word : tokens) {
            String str = word.getWord();
            for (int i = 0; i < str.length(); i++) {
                if (letterFreq.containsKey(str.charAt(i))) {
                    char key = str.charAt(i);
                    letterFreq.put(key, letterFreq.get(key) + 1);
                }
            }
        }
        return letterFreq;
    }

    /**
     * Calculates bitwise signature of 5-letter word
     * Example some word:
     *  - - - b - j - o - r - - - k
     *  Will be mapped to:
     *  0 0 0 1 0 1 0 1 0 1 0 0 0 1
     * @param str - word to calculate bitwise signature of
     * @return integer representing bitwise signature
     */
    public static int calculateSignature(String str) {
        int tmp = 0;
        for (int i = 0; i < str.length(); i++) {
            tmp |= (1 << Function.lettersPositions.get(str.charAt(i))); // bit flip
        }
        return tmp;
    }

    /**
     * Calculates the score for each word based on the frequency of its letters
     * @param str - the word
     * @param letterFrequencies - how common all letters are in general
     * @return total score as the sum of squared occurrences of all letters
     */
    public static int calculateScore(String str, Map<Character, Integer> letterFrequencies) {
        int score = 0;
        for (int i = 0; i < str.length(); i++) {
            Integer letterScore = letterFrequencies.get(str.charAt(i));
            score += (letterScore * letterScore);
        }
        return score;
    }

}
