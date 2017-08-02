package aleamb.regexengine.parser;

public enum Token {

    /* tokens */
    CHAR, L_PARENTHESIS, R_PARENTHESIS, L_BRACKET, PIPE, QUANTIFIER, ESCAPE,

    /* syntax elements */
    REGEX, SELECTOR, RANGE, QUANTIFIED_EXPR, GROUP, EXPRESSION, CHAR_RANGE, REGEXP,
    /* aux elements */
    ROOT,
}
