package aleamb.regexengine.fa;

/**
 * Empty transition. Used in nondeterministic finite automaton.
 */
public class TransitionEmpty extends TransitionBase implements Cloneable {

    public TransitionEmpty() {
        setRepresentation("empty");
    }

    @Override
    public boolean match(char character) {
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new TransitionEmpty();
    }

}
