import java.util.List; 
import java.util.Set;

/**
 * @opt inferrel
 * @opt collpackages java.util.*
 * @opt inferdep
 * @opt inferdepinpackage
 * @opt hide java.*
 * @hidden
 */
class UMLOptions {}


 class Automaton {


    private int stateCount;

    private State initialState;

    private State lastState;

    private Set<Transition> alphabet;

    private List<State> states;
}

class State {
    private List<Transition> transitions;
    private boolean initial = false;
    private boolean end = false;
    private int id = -1;
}

 interface Transition {
    boolean match(char character);
    void setNextState(State pState);
    State getNextState();
    Object clone() throws CloneNotSupportedException;
}

 abstract class TransitionBase implements Transition {

    private State nextState;
    private String representation;
    public State getNextState() {
		return nextState;
    }
    public void setNextState(State nextState) {
		this.nextState = nextState;
   }
}
   

 class TransitionEmpty extends TransitionBase   {}
 class TransitionExclude extends TransitionBase {}
 class TransitionExcludeRange extends TransitionBase {}
 class TransitionRange extends TransitionBase {}

   
