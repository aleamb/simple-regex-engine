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
     * Compiles regular expression.
     * 
     * Compile operation builds a {@link Regex} object for to able text matching and other operations.
     * 
     * Syntax supported:
     * 
     * <table>
     *  <tr>
     *      <td>\t</td>
     *      <td>Tab character</td>
     *  </tr>
     *  <tr>
     *      <td>\s</td>
     *      <td>Whitespace</td>
     *  </tr>
     *  <tr>
     *      <td>[abc]</td>
     *      <td>One of characters inside [ ]</td>
     *  </tr>
     *  <tr>
     *      <td>^[abc]</td>
     *      <td>Any character except inside [ ]</td>
     *  </tr>
     *  <tr>
     *      <td>^[a-z]</td>
     *      <td>Any character except range [ ]</td>
     *  </tr>
     *  <tr>
     *      <td>[a-z]</td>
     *      <td>character range 'a' to 'z'</td>
     *  </tr>
     *  <tr>
     *      <td>*</td>
     *      <td>Matches between zero and unlimited times'</td>
     *  </tr>
     *  <tr>
     *      <td>+</td>
     *      <td>Matches between one and unlimited times</td>
     *  </tr>
     *  <tr>
     *      <td>?</td>
     *      <td>Matches between zero and one times</td>
     *  </tr>
     *  <tr>
     *      <td> \|</td>
     *      <td>OR Alternative</td>
     *  </tr>
     *  <tr>
     *      <td> \x</td>
     *      <td>Escape character 'x'</td>
     *  </tr>
     *  <tr>
     *      <td>.</td>
     *      <td>Any imprimible character (Unicode)</td>
     *  </tr>
     * </table>
     * 
     * Order of precedence (descending precedence order)
     * <ul>
            <li>1. (EXPR) Group.</li>
            <li>2. 'x' (character 'x')</li>
            <li>3. Quantifier (*, + , ?)</li>
            <li>4. OR operator,</li>
       </ul>
     * @param regexExpr regex expression
     * @return Compiled Regular Expression.
     * @throws Exception on compilation error.
     */
    public static Regex compile(String regexExpr) throws RegexException {

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(regexExpr);
        Parser syntaxAnalyzer = new Parser(lexicalAnalyzer);

        ASTNode syntaxNodeTree = syntaxAnalyzer.analyze();
        LOGGER.debug("Syntax tree:\n graph g {\n{}\n};\n", syntaxNodeTree.toString());

        Automaton nfa = AutomatonBuilder.generateFromAST(syntaxNodeTree);
        LOGGER.debug("Nondeterministic finite automaton:\n digraph ndfa {\n{}\n};\n", nfa.toString());

        Automaton dfa = AutomatonBuilder.generateDFAFromNFA(nfa);
        LOGGER.debug("Deterministic finite automaton:\n digraph dfa {\n{}\n};\n", dfa.toString());
        return new Regex(dfa);
    }

}
