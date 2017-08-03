package aleamb.regexengine;

import aleamb.regexengine.fa.Automaton;
import aleamb.regexengine.fa.State;
import aleamb.regexengine.fa.Transition;

/**
 *   * Represents a regular expression compiled.   *   * In order to use a
 * regular expression, it must be compiled first.   * Later it will be applied
 * to a char buffer.   *   * The application of the regex will be done on the
 * entrance of complete form, it is   * Say, the characters will be read and on
 * each one the expression will be applied   * regular. If there is no
 * correspondence, try again from the   * Suitable position depending on the
 * characters processed previously.   *   * In case of correspondence, it
 * returns true and, if there is a   * {@link RegexMatchResult} registered, send
 * the current position, position of   * Start and length of the match.   *   *
 *  
 */
public class Regex {

    // Autmata Finito Determinista que modela la expresin regular.
    private Automaton finiteAutomaton;

    Regex(Automaton pFiniteAutomaton) {

        finiteAutomaton = pFiniteAutomaton;

    }

    /**
     * Realiza la ejecucin de la regex sobre un buffer de caracteres.
     * 
     * Si se le pasa un {@link RegexMatchResult}, se registrar sobre este objeto
     * los datos de comienzo y longitud de la coincidencia. Adems, este mismo
     * objeto determinar la posicin desde donde se comienza a leer del buffer.
     * 
     * @param buffer
     * @param regexMatcher
     *            {@link RegexMatchResult} que registra los datos del suconjunto
     *            de caracteres que coincide con la regex.
     * @return true si encuentra una coincidencia.
     */
    public boolean match(char[] buffer, RegexMatchResult regexMatcher) {

        // flag que indica si continuar con la ejecuin de la regex en caso de
        // fallo o acierto
        boolean stop = false;

        boolean match = false;
        // indice que apunta al comienzo de la coincidencia.
        int matchStartPosition = -1;

        // posicion actual
        int position = 0;

        if (regexMatcher != null) {
            position = regexMatcher.getPosition();
        }

        // comienzo del automata
        State currentState = finiteAutomaton.getInitialState();

        // el autmata siempre quiere consumir caracteres
        while (currentState.hasTransitions() && (position < buffer.length) && !stop) {

            char c = buffer[position];

            // enfrentar el carácter con la regex
            Transition transition = matchTransition((char) c, currentState);

            if (transition == null) { // no hay coincidencia

                // si de todas formas estamos en un estado final, entonces hay
                // match
                if (currentState.isEnd()) {
                    match = true;
                    stop = true;

                    if (matchStartPosition == -1) {
                        matchStartPosition = position;
                    }

                } else {

                    // reiniciar automata
                    currentState = finiteAutomaton.getInitialState();
                    position++;
                    // no hay posicion de comienzo de region
                    matchStartPosition = -1;
                }

            } else {
                // coincide, avanzamos estado
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
