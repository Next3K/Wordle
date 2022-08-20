import java.util.*;

public class Solver {

    private final static int stopSignature = 0b11111_11111_11111_11111_11111;

    public static void solve() {
        List<Wrapper> allWords = Function.getAllWords();
        Collections.shuffle(allWords);
        List<Wrapper> solution = new ArrayList<>(5);
        HashSet<Integer> memo = new HashSet<>(50_000);

        long startTime = System.currentTimeMillis();

        boolean foundSolution = solveRecursive(allWords, memo,
                solution, 0, new HashSet<>(26));

        long endTime = System.currentTimeMillis();

        System.out.println("Searching for solution took: " + (endTime - startTime) / 1000f + " [sec]");
        System.out.println((foundSolution) ? "solution found" + solution : "solution not found");
    }

    private static boolean solveRecursive(List<Wrapper> words,
                                          Set<Integer> memo,
                                          List<Wrapper> currentSolution,
                                          int currentSignature,
                                          Set<Character> usedCharacters) {

        // memoization check
        if (memo.contains(currentSignature)) {
            return false;
        } else {
            memo.add(currentSignature);
        }

        // Check if we are done
        int originalSize = currentSolution.size();
        if (originalSize == 5) {
            return (currentSignature ^ stopSignature) == 0;
        }

        // remove illegal words containing used letters
        words.removeIf(e -> Function.containsForbiddenCharacter(e.getWord(), usedCharacters));
        if (words.size() < (5 - originalSize)) return false;

        // update letter frequency
        Map<Character, Integer> letterFreq = Function.getFrequencyMap(words);

        // sort all current words with score ascending
        Comparator<Wrapper> cmp = Comparator.comparingInt(a -> Function.calculateScore(a.getWord(), letterFreq));
        words.sort(cmp.reversed());

        for (int i = 0; i < words.size(); i++) {
            Wrapper word = words.get(i);
            String wordStr = word.getWord();
            Set<Character> newUsedCharacters = new HashSet<>(usedCharacters);

            // add this word to list of words that may be a solution
            currentSolution.add(word);
            // add 5 used characters to the set
            for (int j = 0; j < wordStr.length(); j++) {
                newUsedCharacters.add(wordStr.charAt(j));
            }
            // create a new list of words without unnecessary words
            int capacity = words.size() - i;
            List<Wrapper> newWords = new ArrayList<>(capacity);
            for (int k = 0; k < capacity; k++) {
                newWords.add(words.get(k));
            }
            if (solveRecursive(newWords, memo, currentSolution,
                    currentSignature | word.getSignature(), newUsedCharacters)) {
                return true;
            }
            // did not find solution, revert list to the original state (cut of the tail of the list)
            int currentSize = currentSolution.size();
            currentSolution.subList(originalSize, currentSize).clear();
        }
        return false;
    }
}
