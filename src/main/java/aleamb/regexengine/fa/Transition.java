package aleamb.regexengine.fa;

/**
 * 
 * Common interface for state transitions.
 * 
 * There are four transition types:
 * 
 * <ul>
 * <li>Empty transition {@link TransitionEmpty} Used in nondeterministic finite automaton.</li>
 * <li>Transition for exclude a set of characters. {@link TransitionExclude}</li>
 * <li>Transition for exclude a character range{@link TransitionExcludeRange}</li>
 * <li>Transition for character range {@link TransitionRange}</li>
 * 
 * </ul>
 */
public interface Transition {

    /**
     * Check if transition is fulfilled for a character.
     * 
     * @param character
     * @return true if transition fulfills.
     */
    boolean match(char character);

    /**
     * Next automaton's state for this transition.
     * 
     * @param pState {@link State}
     */
    void setNextState(State pState);

    /**
     * Get destination state of this transition.
     * 
     * @return Destination state. {@link State}
     */
    State getNextState();

    Object clone() throws CloneNotSupportedException;

}
