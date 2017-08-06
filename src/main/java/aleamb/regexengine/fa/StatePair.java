package aleamb.regexengine.fa;

/**
 * Store state pair. This class is useful for build graph.
 */
class StatePair {

    private State firstState;
    private State secondState;

    public StatePair(State firstState, State secondState) {
        this.firstState = firstState;
        this.secondState = secondState;
    }

    public State getFirstState() {
        return firstState;
    }

    public void setFirstState(State firState) {
        this.firstState = firState;
    }

    public State getSecondState() {
        return secondState;
    }

    public void setSecondState(State secondState) {
        this.secondState = secondState;
    }

}