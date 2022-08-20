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
     * Read all words from the file
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
     * Check if text contains letter that is in the set
     * @param str - text
     * @param usedCharacters set of characters
     * @return true if text contains character that is in the set
     */
    public static boolean containsForbiddenCharacter(String str, Set<Character> usedCharacters) {
        for (int i = 0; i < str.length(); i++) {
            if (usedCharacters.contains(str.charAt(i))) return true;
        }
        return false;
    }

    /**
     * Calculate how common alphabet letters are in a list of words
     * @param tokens - list of words to analyze
     * @return map with info of how common every letter is
     */
    public static Map<Character, Integer> getFrequencyMap(List<Wrapper> tokens) {
        Map<Character, Integer> letterFreq = new HashMap<>(26);
        char c = 'a';
        for (int i = 0; i < 26; i++) {
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
     * Calculates bitwise signature of word
     * @param str - word to calculate bitwise signature of
     * @return integer representing bitwise signature
     */
    public static int calculateSignature(String str) {
        int tmp = 0;
        for (int i = 0; i < str.length(); i++) {
            tmp |= (1 << Function.lettersPositions.get(str.charAt(i)));
        }
        return tmp;
    }

    /**
     * Calculate how common letters that make a word are
     * @param str - the word
     * @param letterFrequencies - how common all letters are
     * @return score
     */
    public static int calculateScore(String str, Map<Character, Integer> letterFrequencies) {
        int score = 0;
        for (int i = 0; i < str.length(); i++) {
            score |= (1 << letterFrequencies.get(str.charAt(i)));
        }
        return score;
    }

}
