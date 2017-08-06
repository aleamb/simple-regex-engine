package aleamb.regexengine.fa;

import java.util.Arrays;

/**
 * Transition for ^[c1c2c3]...
 * 
 */
public class TransitionExclude extends TransitionBase implements Cloneable {

    private char[] excluded;

    public TransitionExclude(char[] excluded) {
        super();
        this.excluded = excluded;
        Arrays.sort(excluded);
        String excludedStr = Arrays.toString(excluded);
        setRepresentation("^" + excludedStr.substring(1).substring(0, excludedStr.length() - 2));
    }

    public char[] getExcluded() {
        return excluded;
    }

    @Override
    public boolean match(char character) {

        if (excluded != null) {

            return !(Arrays.binarySearch(excluded, character) >= 0);
        } else {
            return false;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        TransitionExclude t = new TransitionExclude(excluded);
        return t;
    }
}
