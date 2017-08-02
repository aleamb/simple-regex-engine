package aleamb.regexengine.parser;

public enum Token {

	/* tokens */
	CHAR, L_PARENTHESIS, R_PARENTHESIS, L_BRACKET, PIPE, QUANTIFIER, ESCAPE,

	/* elementos sintácticos */
	REGEX, SELECTOR, RANGE, QUANTIFIED_EXPR, GROUP, EXPRESSION, CHAR_RANGE, REGEXP,
	/* elementos auxiliares */
	ROOT,
}
