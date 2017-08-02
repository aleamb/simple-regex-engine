package aleamb.regexengine.fa;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import aleamb.regexengine.RegexException;
import aleamb.regexengine.parser.ASTNode;
import aleamb.regexengine.parser.Token;

import java.util.Queue;

/**
 * Constructor de autómata finito no determinista (AFND) a partir del árbol
 * sintáctico de la expresión regular.
 * 
 * Cada nodo sintáctico será analizado siguiendo su rama y se construirá un
 * subconjunto de estados con uno inicial y final {@link StatePair}.
 * 
 * Cada AFND, representado por sus estados inicial y final mediante
 * {@link StatePair}, será añadido a una cola para finalmente proceder a la
 * unión de todos los subconjuntos de estados en un AFND final.
 * 
 * Cada tipo de nodo sintáctico tiene su propio método de la clase para generar
 * el par de estados incial y final que representa el significado semántico del
 * nodo.
 * 
 * 
 * Una vez construído el grafo del AFND mediante la conexión de pares de nodos,
 * se creará una instancia de {@link Automaton} y se conectará el estado inicial
 * al primer estado de los pares creados.
 * 
 * Esta clase también incluye el algoritmo subconjunto para crear una autómata
 * finito determinista, que es el que realmente ejecutará el motor de regex.
 * Este método será llamado en la compilación de la Regex.
 * 
 * 
 */
public final class AutomatonBuilder {

    /**
     * Genera un AFND a partir de la expresión regular.
     * 
     * @param node
     *            Nodo raíz del árbol sintáctico.
     * @return Un autómata finito no determinista.
     */
    public static Automaton generateFromAST(ASTNode node) {

        if (node == null || node.isEmpty()) {
            throw new IllegalArgumentException("El árbol sintáctico es nulo o está vacío");
        }

        return new AutomatonBuilder().generateNFA(node);
    }

    /**
     * Generación de autómata no determinista a determinista usando algoritmo
     * subconjunto.
     * 
     * @param pNDAutomaton
     *            Autómata no determinista.
     * @return Nuevo autómata dterminista.
     */
    public static Automaton generateDFAFromNFA(Automaton pNDAutomaton) {

        return new AutomatonBuilder().generateDFA(pNDAutomaton);

    }

    private Automaton generateNFA(ASTNode node) {

        ASTNode rootRegexNode = node.getChildren().iterator().next();

        // generar el grafo del autómata. Se retorna el primer par de estados
        StatePair statePair = faBuildRegexp(rootRegexNode);

        statePair.getFirstState().setInitial(true);
        statePair.getSecondState().setEnd(true);

        // construir el autómata a partir de los estados obtenidos
        Automaton nfa = new Automaton(statePair.getFirstState());

        return nfa;
    }

    /**
     * Generación de autómata no determinista a determinista usando algoritmo de
     * generación de subconjuntos.
     * 
     * @param pNDAutomaton
     *            Autómata no determinista.
     * @return Nuevo autómata dterminista.
     */
    public Automaton generateDFA(Automaton pNDAutomaton) {

        List<SubSetState> estadosAFD = new LinkedList<SubSetState>();

        // calcular cierre-epsilon del estado inicial
        SubSetState S1 = epsilonClosure(pNDAutomaton.getInitialState());

        S1.setInitial(true);

        estadosAFD.add(S1);

        // obetener esatdo no marcado
        SubSetState subSetState = selectState(estadosAFD);

        // mientras haya estados no procesados o marcados
        while (subSetState != null) {

            // por cada símbolo 'a' perteneciente al lenguaje
            for (Transition t : pNDAutomaton.getAlphabet()) {

                if (!(t instanceof TransitionEmpty)) {
                    // calcular a que estados se va con esta transición desde el
                    // subonjutno de estados
                    SubSetState moveState = epsilonMove(pNDAutomaton, subSetState, t);
                    if (!moveState.isEmpty()) {
                        SubSetState moCloseState = epsilonClosure(moveState);

                        // comprueba si no existe ya este estado agrupado.
                        SubSetState euqlState = findState(estadosAFD, moCloseState);
                        if (euqlState == null) {
                            estadosAFD.add(moCloseState);
                        } else {
                            moCloseState = euqlState;
                        }
                        Transition afdTransition;
                        try {
                            afdTransition = (Transition) t.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException("Fail copying transition");
                        }
                        subSetState.getState().connect(afdTransition, moCloseState.getState());

                    }
                }
            }
            subSetState.setMark(true);
            // siguiente estado sin marcar
            subSetState = selectState(estadosAFD);

        }

        // construir autómata con los nuevos estados interconectados
        Automaton dfa = new Automaton(S1.getState());
        return dfa;
    }

    // comienzo métodos de construccion del semántica del AF

    /**
     * Construye los estados correspondientes al nodo sintáctico <regexp>
     * 
     * Debido a que cada expresión de la regex es independiente de la anterior,
     * cada rama de nodo será construída haciendo un anaĺisis en profundidad y,
     * una vez construídos todos los estados y tarsnciones, se almacenarán en ua
     * cola para ir enlazando uno de tras de otro.
     * 
     * @param pNode
     *            Nodo del árbol sintáctico.
     * @return Par de estados que representan la expresión regular como AFN
     */
    private StatePair faBuildRegexp(ASTNode pNode) {

        StatePair headStatePair = null;

        Queue<StatePair> statePairsQueue = new LinkedList<StatePair>();

        for (ASTNode childNode : pNode.getChildren()) {

            if (childNode.getType().equals(Token.QUANTIFIED_EXPR)) {

                StatePair newExpressionPair = faBuildQuantifiedExpression(childNode);
                // añadir subconjunto de estados del autómata a la cola para
                // unir todos posteriormente. Esto respeta el orden de
                // prioiridad
                // de primero las expresiones y luego el OR
                statePairsQueue.add(newExpressionPair);

            } else if (childNode.getType().equals(Token.REGEX)) {

                StatePair regexpPair = faBuildRegex(childNode, statePairsQueue);

                // conectamos todos los estados anteriores, que forman r1
                StatePair orStatePair = null;
                headStatePair = statePairsQueue.poll();
                orStatePair = headStatePair;
                while (!statePairsQueue.isEmpty()) {
                    StatePair newStatePair = statePairsQueue.poll();
                    orStatePair.getSecondState().connect(createEmptyTransition(), newStatePair.getFirstState());
                    orStatePair = newStatePair;
                }

                /*
                 * ahora r1 es un solo autómata que unimos al autómata del
                 * segundo operador del OR
                 */

                State q0 = new State();
                State q1 = new State();
                q0.connect(createEmptyTransition(), headStatePair.getFirstState());

                q0.connect(createEmptyTransition(), regexpPair.getFirstState());

                regexpPair.getSecondState().connect(createEmptyTransition(), q1);
                orStatePair.getSecondState().connect(createEmptyTransition(), q1);
                StatePair pipeStatePair = new StatePair(q0, q1);
                statePairsQueue.add(pipeStatePair);
            }
        }

        /*
         * Se unen todos los subconjuntos de estados obtenidos a partir de los
         * nodos sintácticos analizados.
         * 
         * Estos son los nodos tipo <qualified_expression> que aún hay que
         * procesar.
         */
        headStatePair = statePairsQueue.poll();
        StatePair tmpPair = headStatePair;

        while (!statePairsQueue.isEmpty()) {

            StatePair pair = statePairsQueue.poll();

            tmpPair.getSecondState().connect(createEmptyTransition(), pair.getFirstState());
            tmpPair = pair;
        }

        headStatePair.setSecondState(tmpPair.getSecondState());

        return headStatePair;
    }

    /**
     * Procesa el nodo de la produccion auxiliar <regex>
     * 
     * Si se ha llegado aquí, necesariamnete ha habido una sentencia OR en la
     * regexp.
     * 
     * @param regexNode
     * @param statePairsQueue
     * @return
     */
    private StatePair faBuildRegex(ASTNode regexNode, Queue<StatePair> statePairsQueue) {

        // si se ha llegdo aqui, hay que procesar un OR

        Iterator<ASTNode> childNodes = regexNode.getChildren().iterator();

        // saltar nodo pipe
        childNodes.next();
        ASTNode regexpNode = childNodes.next();

        // si hay un PIPE, debe haber un nodo hermano que sea <regexp>
        StatePair regexpPair = faBuildRegexp(regexpNode);

        return regexpPair;

    }

    private StatePair faBuildQuantifiedExpression(ASTNode quantifiedExpression) {

        StatePair headStatePair = null;
        StatePair expressionPair = null;

        for (ASTNode child : quantifiedExpression.getChildren()) {

            if (child.getType() == Token.EXPRESSION) {

                StatePair newExpressionPair = faBuildExpression(child);

                if (expressionPair != null) {
                    Transition t = createEmptyTransition();
                    expressionPair.getSecondState().connect(t, newExpressionPair.getFirstState());

                }
                expressionPair = newExpressionPair;

            } else {
                // el nodo es de tipo cuantificador
                if (child.getValue().equals("+")) {

                    Transition t = createEmptyTransition();

                    expressionPair.getSecondState().connect(t, expressionPair.getFirstState());

                } else if (child.getValue().equals("*")) {

                    // conectar primero con ultimo con una transicion vacia
                    Transition t = createEmptyTransition();
                    expressionPair.getSecondState().connect(t, expressionPair.getFirstState());

                    // crear dos nuevos estados para '*'
                    State q0 = new State();
                    State q1 = new State();

                    q0.connect(createEmptyTransition(), expressionPair.getFirstState());

                    expressionPair.getSecondState().connect(createEmptyTransition(), q1);
                    q0.connect(createEmptyTransition(), q1);
                    expressionPair.setFirstState(q0);
                    expressionPair.setSecondState(q1);

                } else { // tipo '?'
                    expressionPair.getFirstState().connect(createEmptyTransition(), expressionPair.getSecondState());

                }

            }
            if (headStatePair == null) {
                headStatePair = expressionPair;
            }
        }

        return headStatePair;

    }

    private StatePair faBuildExpression(ASTNode node) {

        StatePair lastStates = null;

        ASTNode child = node.getChildren().iterator().next();

        if (child.getType() == Token.SELECTOR) {

            lastStates = faBuildSelector(child);
        } else if (child.getType() == Token.GROUP) {
            lastStates = faBuildGroup(child);
        }
        return lastStates;
    }

    private StatePair faBuildGroup(ASTNode node) {
        ASTNode regexNode = node.getChildren().iterator().next();
        return faBuildRegexp(regexNode);
    }

    private StatePair faBuildSelector(ASTNode node) {
        ASTNode child = node.getChildren().iterator().next();
        StatePair statePair = null;

        if (child.getType() == Token.RANGE) {
            statePair = faBuildRange(child);
        } else {
            statePair = faBuildChar(node);
        }
        return statePair;
    }

    private StatePair faBuildChar(ASTNode pNode) {

        char character = pNode.getValue().charAt(0);
        char initChar;
        char endChar;

        if (character == '.') {
            initChar = ' ';
            endChar = Character.MAX_VALUE;
        } else {
            initChar = endChar = character;
        }

        Transition t = new TransitionRange(initChar, endChar);
        State init = new State();
        State end = new State();

        init.connect(t, end);

        return new StatePair(init, end);

    }

    private StatePair faBuildRange(ASTNode node) {

        ASTNode child = node.getChildren().iterator().next();
        return faBuildCharRange(child);

    }

    /*
     * Para la construcción de los rangos se realiza un pequeño análisis
     * semántico de los valores del rango y la posición del gui�n.
     */
    private StatePair faBuildCharRange(ASTNode charRange) {

        ASTNode[] childNodes = new ASTNode[charRange.getChildrenCount()];
        int i = 0;
        for (ASTNode astNode : charRange.getChildren()) {
            childNodes[i++] = astNode;
        }
        i = 0;

        State firstState = new State();
        State secondState = new State();

        StringBuilder excludeChars = null;

        ASTNode firstChildNode = childNodes[0];
        int nChilds = childNodes.length;

        boolean excludeRange = (firstChildNode.getType() != Token.ESCAPE && firstChildNode.getValue().equals("^"));

        if (excludeRange) {
            excludeChars = new StringBuilder();
            i = 1;
        }

        while (i < childNodes.length) {

            // comprobar si viene un rango
            if (i + 2 < nChilds && childNodes[i + 1].getValue().equals("-")
                    && childNodes[i + 2].getType() != Token.ESCAPE) {

                char initChar = childNodes[i].getValue().charAt(0);
                char endChar = childNodes[i + 2].getValue().charAt(0);

                Transition t = null;

                if (excludeRange) {
                    t = new TransitionExcludeRange(initChar, endChar);
                } else {
                    if (endChar < initChar) {
                        throw new RegexException("Rango no válido en " + childNodes[i + 2].getPositionInRegex());
                    }
                    t = new TransitionRange(initChar, endChar);
                }
                firstState.connect(t, secondState);
                i += 3;

            } else {
                char rangeChar = childNodes[i].getValue().charAt(0);

                if (excludeRange) {
                    excludeChars.append(rangeChar);
                } else {
                    TransitionRange transitionRange = new TransitionRange(rangeChar, rangeChar);
                    firstState.connect(transitionRange, secondState);
                }
                i++;
            }
        }
        if (excludeRange && excludeChars.length() > 0) {
            TransitionExclude transitionExclude = new TransitionExclude(excludeChars.toString().toCharArray());
            firstState.connect(transitionExclude, secondState);
        }

        return new StatePair(firstState, secondState);
    }

    // ///////////////////
    // métodos para el algoritmo subconjunto

    private SubSetState epsilonClosure(State state) {

        Set<State> closureSet = new HashSet<State>();
        epsilonClosure(state, closureSet, createEmptyTransition());

        SubSetState subSetState = new SubSetState(closureSet);
        subSetState.getState().setEnd(verifyFinalStates(subSetState));

        return subSetState;

    }

    private void epsilonClosure(State state, Set<State> closureSet, Transition sourceTransition) {

        if (state.getTransitions() != null) {
            for (Transition t : state.getTransitions()) {
                if (sourceTransition.equals(t)) {
                    epsilonClosure(t.getNextState(), closureSet, sourceTransition);
                }
            }
        }

        closureSet.add(state);

    }

    private SubSetState selectState(List<SubSetState> DFAstates) {

        for (SubSetState subSetState : DFAstates) {

            if (!subSetState.isMark()) {
                return subSetState;
            }
        }
        return null;
    }

    /**
     * Movimiento de S. Sienod S un estado agrupado.
     * 
     * Retorna el subconjunto de estados a los que llevan los estados de
     * pSubSetState con la transición indicada.
     * 
     * @param fa
     *            autómata
     * @param pSubSetState
     *            Estados a verificar.
     * @param t
     *            Transitión.
     * @return Estado {@link SubSetState} que agrupa ese conjunto de estados.
     */
    private SubSetState epsilonMove(Automaton fa, SubSetState pSubSetState, Transition t) {

        Set<State> moveStates = new HashSet<State>();
        for (State state : pSubSetState.getStates()) {

            searchStatesByTransition(state, t, moveStates);
        }

        SubSetState subSetState = new SubSetState(moveStates);

        return subSetState;

    }

    /*
     * Obtener todos los estados a los que se puede llegar desde state con
     * transision testTransition
     */
    private void searchStatesByTransition(State state, Transition testTransition, Set<State> pEpsilonMoveStates) {

        if (state == null) {
            return;
        }

        if (state.getTransitions() != null) {
            for (Transition t : state.getTransitions()) {
                if (t.equals(testTransition)) {
                    pEpsilonMoveStates.add(t.getNextState());
                } else if (t instanceof TransitionEmpty) {
                    searchStatesByTransition(t.getNextState(), testTransition, pEpsilonMoveStates);
                }

            }
        }
    }

    /*
     * Cierre epsilon de un estado agrupado.
     */
    private SubSetState epsilonClosure(SubSetState subSetState) {

        Set<State> states = new HashSet<State>();

        for (State state : subSetState.getStates()) {
            states.addAll(epsilonClosure(state).getStates());
        }

        SubSetState closureSubSet = new SubSetState(states);

        // comprueba si alguno de los estados es final para marcar
        // el estado superconjunto como final.

        closureSubSet.getState().setEnd(verifyFinalStates(closureSubSet));

        return closureSubSet;
    }

    private boolean verifyFinalStates(SubSetState subSetState) {
        for (State state : subSetState.getStates()) {
            if (state.isEnd()) {
                return true;
            }
        }
        return false;
    }

    private SubSetState findState(List<SubSetState> DFAstates, SubSetState closureState) {
        for (SubSetState subSetState : DFAstates) {
            if (subSetState.equals(closureState)) {
                return subSetState;
            }
        }
        return null;
    }

    private Transition createEmptyTransition() {
        return new TransitionEmpty();
    }

}
