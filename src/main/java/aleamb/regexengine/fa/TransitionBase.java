package aleamb.regexengine.fa;

public abstract class TransitionBase implements Transition, Comparable<Transition> {

    private State nextState;
    private String representation;

    public State getNextState() {
        return nextState;
    }

    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    @Override
    public int hashCode() {
        return representation.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransitionBase other = (TransitionBase) obj;
        if (representation == null) {
            if (other.representation != null)
                return false;
        } else if (!representation.equals(other.representation))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return representation;
    }

    @Override
    public int compareTo(Transition o) {
        return getRepresentation().compareTo(((TransitionBase) o).getRepresentation());
    }

    @Override
    public boolean match(char character) {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("TransitionBase");
    }

}
