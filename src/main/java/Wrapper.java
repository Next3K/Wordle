import lombok.Getter;
import lombok.ToString;

/**
 * Immutable class wrapping 5-letter word
 */
@ToString(onlyExplicitlyIncluded = true)
public class Wrapper {

    @Getter
    @ToString.Include
    private final String word;

    @Getter
    private final int signature;

    public Wrapper(String word) {
        this.word = word;
        this.signature = Function.calculateSignature(this.word);
    }
}
