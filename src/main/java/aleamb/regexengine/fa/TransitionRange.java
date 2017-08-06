package aleamb.regexengine.fa;

/**
 * Transition for [c1-c2]
 * 
 */
public class TransitionRange extends TransitionBase implements Cloneable {

    private char initChar;
    private char endChar;

    public TransitionRange(char initChar, char endChar) {
        super();
        this.initChar = initChar;
        this.endChar = endChar;
        if (initChar == endChar) {
            setRepresentation("'" + String.valueOf(initChar) + "'");
        } else {
            setRepresentation(String.valueOf(initChar) + "-" + String.valueOf(endChar));
        }
    }

    public char getInitChar() {
        return initChar;
    }

    public char getEndChar() {
        return endChar;
    }

    @Override
    public boolean match(char character) {

        return (character >= initChar && character <= endChar);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        TransitionRange t = new TransitionRange(initChar, endChar);

        return t;
    }

}
