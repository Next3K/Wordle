import java.util.*;

public class Solver {

    // when signature is equal to "stopSignature" we know we found the solution
    private final static int stopSignature = 0b11111_11111_11111_11111_11111;

    public static void solve() {
        List<Wrapper> allWords = Function.getAllWords();
        Collections.shuffle(allWords);
        HashSet<Integer> memo = new HashSet<>(50_000);
        ArrayList<Wrapper> solution = new ArrayList<>(5);

        long startTime = System.currentTimeMillis(); // start time measurement
        Map<Character, Integer> frequencyMap = Function.getFrequencyMap(allWords);
        allWords.sort(Comparator.comparingInt(a -> Function.calculateScore(a.getWord(), frequencyMap)));
        boolean foundSolution = solveRecursive(allWords, memo,
                solution, 0b0, new HashSet<>(26), 0);
        long endTime = System.currentTimeMillis(); // stop time measurement

        float totalTime = (endTime - startTime) / 1000f;

        System.out.println("Searching for solution took: " + totalTime + " [sec]");
        if (foundSolution) {
            System.out.println("solution found: " + solution);
        } else {
            System.out.println("solution not found");
        }
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
        // memoization check - check if we have been in equivalent state before, if so, go back
        if (memo.contains(currentSignature)) return false;
        else memo.add(currentSignature);

        int solutionSize = currentSolution.size();

        if (solutionSize == 4) {
            for (int i = startIndex; i < currentList.size(); i++) {
                Wrapper wrapper = currentList.get(i);
                int checkSignature = currentSignature | wrapper.getSignature();
                if ((checkSignature ^ stopSignature) == 0) { // Check if we are done
                    currentSolution.add(wrapper);
                    return true;
                } else {
                    memo.add(checkSignature);
                }
            }
            return false;
        }

        // return false when too few words left in the list
        if ((currentList.size() - startIndex) < (5 - solutionSize)) return false;

        for (int i = startIndex; i < currentList.size(); i++) {
            Wrapper wrapper = currentList.get(i);
            String word = wrapper.getWord();

            // add this wrapper to the list of wrappers that may become a solution
            currentSolution.add(wrapper);

            // create new set of used letters and add 5 new letters
            Set<Character> updatedUsedCharacters = new HashSet<>(usedCharacters);
            for (int j = 0; j < word.length(); j++) {
                updatedUsedCharacters.add(word.charAt(j));
            }

            if (solveRecursive(currentList, memo, currentSolution,
                    currentSignature | wrapper.getSignature(), updatedUsedCharacters,i + 1)) {
                return true;
            }

            // did not find solution, revert list to the original state (cut of the tail of the list)
            // it will cut off from 1 up to 5 elements from the list
            currentSolution.subList(solutionSize, currentSolution.size()).clear();
        }
        return false;
    }
}
