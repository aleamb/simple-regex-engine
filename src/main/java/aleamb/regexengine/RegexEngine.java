package aleamb.regexengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aleamb.regexengine.fa.Automaton;
import aleamb.regexengine.fa.AutomatonBuilder;
import aleamb.regexengine.parser.ASTNode;
import aleamb.regexengine.parser.LexicalAnalyzer;
import aleamb.regexengine.parser.Parser;

/**
 * Simple regex engine for a tiny subset of PCRE.
 * 
 */
public class RegexEngine {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegexEngine.class);

    /**
     * Compila la expresón regular para poder ser ejecutada.
     * 
     * @param regexExpr
     *            String con la expresión regular.
     * @return Expresión regular compilada.
     * @throws Exception
     *             En caso de fallo al compilar la expresión.
     */
    public static Regex compile(String regexExpr) throws RegexException {

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(regexExpr);
        Parser syntaxAnalyzer = new Parser(lexicalAnalyzer);

        ASTNode syntaxNodeTree = syntaxAnalyzer.analyze();
        LOGGER.debug("Syntax tree:\n graph g {\n{}\n};\n", syntaxNodeTree.toString());

        Automaton nfa = AutomatonBuilder.generateFromAST(syntaxNodeTree);
        LOGGER.debug("Nondeterministic finite automaton:\n digraph afnd {\n{}\n};\n", nfa.toString());

        Automaton dfa = AutomatonBuilder.generateDFAFromNFA(nfa);
        LOGGER.debug("Deterministic finite automaton:\n digraph afnd {\n{}\n};\n", dfa.toString());
        return new Regex(dfa);
    }

}
