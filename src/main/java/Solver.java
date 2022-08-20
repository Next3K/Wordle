import java.util.*;
import java.util.stream.Collectors;

public class Solver {

    private final static int stopSignature = 0b11111_11111_11111_11111_11111;

    public static void solve() {
        List<Wrapper> allWords = Function.getAllWords();
        Collections.shuffle(allWords);
        List<Wrapper> solution = new ArrayList<>(5);
        HashSet<Integer> memo = new HashSet<>(50_000);

        long startTime = System.currentTimeMillis();
        Map<Character, Integer> frequencyMap = Function.getFrequencyMap(allWords);
        Comparator<Wrapper> cmp = Comparator.comparingInt(a -> Function.calculateScore(a.getWord(), frequencyMap));
        allWords.sort(cmp);
        boolean foundSolution = solveRecursive(allWords, memo,
                solution, 0, new HashSet<>(26), 0);

        long endTime = System.currentTimeMillis();

        System.out.println("Searching for solution took: " + (endTime - startTime) / 1000f + " [sec]");
        System.out.println((foundSolution) ? "solution found" + solution : "solution not found");
    }

    private static boolean solveRecursive(List<Wrapper> words,
                                          Set<Integer> memo,
                                          List<Wrapper> currentSolution,
                                          int currentSignature,
                                          Set<Character> usedCharacters,
                                          int startIndex) {

        List<Wrapper> currentList = words;

        if (usedCharacters.size() != 0) {
            currentList =
                    words.stream().filter(e -> !Function.containsForbiddenCharacter(e.getWord(), usedCharacters)).toList();
            startIndex = 0;
        }

        
        // memoization check
        if (memo.contains(currentSignature)) {
            return false;
        } else {
            memo.add(currentSignature);
        }

        int solutionSize = currentSolution.size();

        // Check if we are done
        if (solutionSize == 4) {
            for (int i = startIndex; i < currentList.size(); i++) {
                Wrapper wrapper = currentList.get(i);
                int checkSignature = currentSignature | wrapper.getSignature();
                if ((checkSignature ^ stopSignature) == 0) {
                    currentSolution.add(wrapper);
                    return true;
                } else {
                    memo.add(checkSignature);
                }
            }
            return false;
        }

        // we need (5 - solutionSize) currentList but there is only n currentList left in the list
        if ((currentList.size() - startIndex) < (5 - solutionSize)) return false;

        for (int i = startIndex; i < currentList.size(); i++) {
            Wrapper word = currentList.get(i);
            String currentWordStr = word.getWord();

            if (Function.containsForbiddenCharacter(currentWordStr, usedCharacters)) continue;

            // add this word to list of currentList that may be a solution
            currentSolution.add(word);

            // create new set of used letters and add 5 new letters
            Set<Character> newUsedCharacters = new HashSet<>(usedCharacters);
            for (int j = 0; j < currentWordStr.length(); j++) {
                newUsedCharacters.add(currentWordStr.charAt(j));
            }

            if (solveRecursive(currentList, memo, currentSolution,
                    currentSignature | word.getSignature(), newUsedCharacters,i + 1)) {
                return true;
            }

            // did not find solution, revert list to the original state (cut of the tail of the list)
            currentSolution.subList(solutionSize, currentSolution.size()).clear();
        }
        return false;
    }
}
