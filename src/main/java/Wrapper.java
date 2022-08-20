import lombok.Getter;

/**
 * Immutable class wrapping 5-letter word
 */
public class Wrapper {

    @Getter
    private final String word;

    @Getter
    private final int signature;


    public Wrapper(String word) {
        this.word = word;
        this.signature = Function.calculateSignature(this.word);
    }

    @Override
    public String toString() {
        return "Wrapper{" +
                "str='" + word + '\'' +
                '}';
    }
}
