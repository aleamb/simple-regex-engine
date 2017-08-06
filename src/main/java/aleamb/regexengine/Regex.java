package aleamb.regexengine;

import aleamb.regexengine.fa.Automaton;
import aleamb.regexengine.fa.State;
import aleamb.regexengine.fa.Transition;

/**
 * Compiled regular expression.
 * 
 * In order to use a regular expression it must be compiled first. Later it will be applied
 * to a char buffer.
 * 
 * In case of correspondence, {@link Regex#match(char[], RegexMatchResult)} method returns true. 
 * If pass an instance of {@link RegexMatchResult} in second parameter, matching will start from 
 * indicated position.
 * 
 *  
 */
public class Regex {

    // Deterministic finite automaton that models regular expression.
    private Automaton finiteAutomaton;

    Regex(Automaton pFiniteAutomaton) {

        finiteAutomaton = pFiniteAutomaton;

    }

    /**
     * Executes compiled regex for an input char buffer.
     * 
     * if {@link RegexMatchResult} is specified, method will be store the start position of match and its length into it.
     * this param indicates to method where start matching too, thorough position attribute.
     * 
     * @param buffer input buffer.
     * @param regexMatcher {@link RegexMatchResult} that register subset of matching char data.
     * @return true if match found.
     */
    public boolean match(char[] buffer, RegexMatchResult regexMatcher) {

        boolean stop = false;

        boolean match = false;
        // start of occurrence
        int matchStartPosition = -1;

        // current position
        int position = 0;

        if (regexMatcher != null) {
            position = regexMatcher.getPosition();
        }

        // inital state of deterministic automaton
        State currentState = finiteAutomaton.getInitialState();

        // While chars in buffer. Automaton is greedy
        while (currentState.hasTransitions() && (position < buffer.length) && !stop) {

            char c = buffer[position];

            // find the transition match character
            Transition transition = matchTransition((char) c, currentState);

            if (transition == null) {

                if (currentState.isEnd()) {
                    match = true;
                    stop = true;

                    if (matchStartPosition == -1) {
                        matchStartPosition = position;
                    }

                } else {

                    // reset automaton to start
                    currentState = finiteAutomaton.getInitialState();
                    position++;
                    // clear start position of match register
                    matchStartPosition = -1;
                }

            } else {
                // transition found, go to next state
                currentState = transition.getNextState();
                // registramos la posicion de comienzo de la coincidencia

                // la regex se cuplira s el estado es de aceptacion
                match = currentState.isEnd();

                if (matchStartPosition == -1) {
                    matchStartPosition = position;
                }
                position++;

            }
        }

        if (match) {
            if (regexMatcher != null) {
                regexMatcher.setMatchStartPosition(matchStartPosition);

                regexMatcher.setMatchLength(position - matchStartPosition);
            }
        }

        return match;
    }

    private Transition matchTransition(char c, State pState) {

        Iterable<Transition> transitionList = pState.getTransitions();
        if (transitionList != null) {
            for (Transition t : pState.getTransitions()) {
                if (t.match(c)) {
                    return t;
                }
            }
        }

        return null;
    }

    /**
     * Serialize regex object to Graphiz/DOT.
     */

    @Override
    public String toString() {

        StringBuilder graphvizBuffer = new StringBuilder();
        graphvizBuffer.append("digraph g {\n").append(finiteAutomaton).append("}\n");
        return graphvizBuffer.toString();
    }

}
