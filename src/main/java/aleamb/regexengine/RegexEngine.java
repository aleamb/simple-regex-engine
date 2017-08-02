package es.chathispano.canalprogramacion.regexengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.chathispano.canalprogramacion.regexengine.fa.Automaton;
import es.chathispano.canalprogramacion.regexengine.fa.AutomatonBuilder;
import es.chathispano.canalprogramacion.regexengine.parser.ASTNode;
import es.chathispano.canalprogramacion.regexengine.parser.LexicalAnalyzer;
import es.chathispano.canalprogramacion.regexengine.parser.Parser;

/**
 * Motor de regex para el canal #programacion de IRC-Hispano.
 * 
 * 
 */
public class RegexEngine {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(RegexEngine.class);

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
		LOGGER.debug("Arbol sintáctico:\n graph g {\n{}\n};\n",
				syntaxNodeTree.toString());

		Automaton nfa = AutomatonBuilder.generateFromAST(syntaxNodeTree);
		LOGGER.debug("Autómata NO determinista:\n digraph afnd {\n{}\n};\n",
				nfa.toString());

		Automaton dfa = AutomatonBuilder.generateDFAFromNFA(nfa);
		LOGGER.debug("Autómata Determinista:\n digraph afnd {\n{}\n};\n",
				dfa.toString());
		return new Regex(dfa);
	}

}
