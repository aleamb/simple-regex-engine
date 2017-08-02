package aleamb.regexengine.fa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Autómata finito (puede ser determinista o no determinista) que será
 * construído a partir de una expresión regular.
 * 
 * La instancia de esta clase mantiene una referencia al primer estado del
 * autómata. Los restantes estdos se obtienen mediante las transiciones a partir
 * del primero.
 * 
 * También se mantendrá una lista con las referencias a todas los tipos de
 * transiciones que tenga el autómata, ya que de esta forma se almacena el
 * propio alfabeto que soporta. Esta lista también se usa para optimizar el
 * algoritmo de transformación de un atómata no determinista a uno determinista.
 * 
 * 
 */
public class Automaton {

    // número de estados del autómata
    private int stateCount;

    // estado inicial. Los restantes estados son enlazados mediante sus
    // transiciones.
    private State initialState;

    // ultimo estado a�adido
    private State lastState;

    // lista de los tipos de transición que usa el autómata. Esto puede servir
    // como alfabeto.
    private Set<Transition> alphabet;

    // es conveniente guardar una lista lineal de los estados.
    private List<State> states;

    public Automaton() {
        alphabet = new HashSet<Transition>();
        states = new ArrayList<State>();
        stateCount = 0;
    }

    public Automaton(State firstState) {
        this();
        initialState = firstState;
        build();
    }

    public int getStateCount() {
        return stateCount;
    }

    public void setStateCount(int stateCount) {
        this.stateCount = stateCount;
    }

    public State getInitialState() {
        return initialState;
    }

    public void setIntialState(State initialState) {
        this.initialState = initialState;
    }

    public boolean addToAlphabet(Transition pTransition) {
        return alphabet.add(pTransition);
    }

    public Iterable<Transition> getAlphabet() {
        return Collections.unmodifiableSet(alphabet);
    }

    public State getLastState() {
        return lastState;
    }

    public Collection<State> getStateList() {
        return Collections.unmodifiableList(states);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        boolean[] visited = new boolean[stateCount];
        navigateAndPrint(builder, initialState, visited);

        return builder.toString();
    }

    private void navigateAndPrint(StringBuilder builder, State pState, boolean[] visited) {

        if (pState == null) {
            return;
        }
        visited[pState.getId()] = true;
        builder.append(pState);
        Iterable<Transition> transitions = pState.getTransitions();
        if (transitions != null) {
            for (Transition t : pState.getTransitions()) {
                State nextState = t.getNextState();
                if (!visited[nextState.getId()]) {
                    navigateAndPrint(builder, nextState, visited);
                }
            }
        }
    }

    private void build() {

        /*
         * Recorrido en amplitud para contabilizar y otorgarles id's a los
         * estados
         */
        Queue<State> stateQueue = new LinkedList<State>();

        stateQueue.add(initialState);
        initialState.setId(stateCount++);

        while (!stateQueue.isEmpty()) {

            State state = stateQueue.poll();
            states.add(state);

            if (state.getTransitions() != null) {
                for (Transition t : state.getTransitions()) {
                    // añadir transición para formar el alfabeto del autómata
                    alphabet.add(t);
                    State childState = t.getNextState();
                    if (childState.getId() == -1) {
                        childState.setId(stateCount++);
                        stateQueue.add(childState);
                    }
                }
            }
        }
    }
}
