package aleamb.regexengine.parser;

/**
 * Parser recursive-descent-parser implemented from BNF:
 * 
 * Grammar:
 * 
 * <regexp> ::= <quantified_expression> { <quantified_expression> } <regex>
 * 
 * <regex> ::= '|' <regexp> | palabra_vacia
 * 
 * <quantified_expression> ::= <expression> [ <quantifier> ]
 * 
 * <expression> ::= <selector> | '(' <regexp> ')'
 * 
 * <selector> ::= <symbol> | <range>
 * 
 * <range> ::= '[' <char_range> ']'
 * 
 * <char_range> ::= <symbol> | <escape_char> { <char_range> }
 * 
 * <symbol> ::= <char> | <escape>
 * 
 * <char> ::= Printable charcaters except '(' | ')' | '*' | '+' | '?' | '[' |
 * '\' | '|'
 * 
 * <escape_char> ::= '(' | ')' | '*' | '+' | '?' | '['| '\' | '|' <escape>
 * ::='\' <escape_char> | 's' | 't' | '-' | ']' | | '^' | '.'
 * 
 * <quantifier> ::= '*'| '+' | '?'
 * 
 * 
 * Gramar is not LL(1), first production is ambiguous. Is neccesary semantic
 * analysis in that case.
 * 
 */

public class Parser {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token token;

    public Parser(LexicalAnalyzer pLexicalAnalyzer) {
        lexicalAnalyzer = pLexicalAnalyzer;
    }

    public ASTNode analyze() {

        // start symbol (forwarding symbol)
        readNextToken();

        if (token == null) {
            throw new SyntaxException("Regex vacía.");
        }
        // root node of syntax tree
        ASTNode root = createNode(Token.ROOT);

        // main production Rest of analysis is recursive.
        ASTNode n = prRegexp();

        // parsing finished. Add root of abstract syntax tree to root node.
        addChildNode(root, n);

        // it should not more tokens.
        if (token != null) {
            parseError("Not balanced expression");
        }

        return root;

    }

    private ASTNode prRegexp() {
        ASTNode node = createNode(Token.REGEXP);

        // at least a quantified expression.
        if (match(Token.CHAR, Token.ESCAPE, Token.L_BRACKET, Token.L_PARENTHESIS)) {
            ASTNode n = prQuantifiedExpression();
            addChildNode(node, n);
        } else {
            parseError(Token.CHAR, Token.ESCAPE, Token.L_BRACKET, Token.L_PARENTHESIS);
        }

        // next zero or more
        while (match(Token.CHAR, Token.ESCAPE, Token.L_BRACKET, Token.L_PARENTHESIS)) {
            ASTNode n = prQuantifiedExpression();
            addChildNode(node, n);
        }
        // now a <regex>
        ASTNode regexNode = prRegex();

        // it may be empty word
        if (regexNode != null) {
            addChildNode(node, regexNode);
        }

        return node;
    }

    private ASTNode prRegex() {

        ASTNode node = createNode(Token.REGEX);

        if (match(Token.PIPE)) {
            consume(Token.PIPE);
            ASTNode n = createNode(Token.PIPE);
            addChildNode(node, n);
            n = prRegexp();
            addChildNode(node, n);
        } else {
            node = null;
        }

        return node;
    }

    private ASTNode prQuantifiedExpression() {

        ASTNode node = createNode(Token.QUANTIFIED_EXPR);

        // must receive a expression
        ASTNode n = prExpression();
        addChildNode(node, n);

        // optionally read a quantifier
        if (match(Token.QUANTIFIER)) {
            addChildNode(node, Token.QUANTIFIER);
            consume(Token.QUANTIFIER);
        }
        return node;
    }

    private ASTNode prExpression() {

        ASTNode node = createNode(Token.EXPRESSION);
        if (match(Token.CHAR, Token.ESCAPE, Token.L_BRACKET)) {

            ASTNode n = prSelector();
            addChildNode(node, n);

        } else if (match(Token.L_PARENTHESIS)) {

            ASTNode nodeGroup = createNode(Token.GROUP);

            consume(Token.L_PARENTHESIS);
            ASTNode regex = prRegexp();
            consume(Token.R_PARENTHESIS);

            addChildNode(nodeGroup, regex);
            addChildNode(node, nodeGroup);
        }
        return node;

    }

    private ASTNode prSelector() {

        ASTNode node = createNode(Token.SELECTOR);

        if (match(Token.CHAR, Token.ESCAPE)) {
            ASTNode n = prSymbol();
            addChildNode(node, n);
        } else if (token == Token.L_BRACKET) {
            ASTNode n = prRange();
            addChildNode(node, n);

        }
        return node;
    }

    private ASTNode prRange() {

        ASTNode node = createNode(Token.RANGE);

        consume(Token.L_BRACKET);

        if (match(Token.ESCAPE, Token.CHAR)) {

            ASTNode charRange = prCharRange();
            addChildNode(node, charRange);

        } else {
            parseError(Token.CHAR, Token.ESCAPE);
        }
        consume("]");

        return node;
    }

    private ASTNode prCharRange() {
        ASTNode node = createNode(Token.CHAR_RANGE);

        // prevent empty state
        if (lexicalAnalyzer.getTokenValue().equals("]")) {
            parseError(Token.CHAR, Token.ESCAPE);
        }

        while (!lexicalAnalyzer.getTokenValue().equals("]")) {
            addChildNode(node, Token.CHAR);
            consume(token);
        }

        return node;
    }

    private ASTNode prSymbol() {

        ASTNode node = null;

        if (match(Token.CHAR)) {
            node = createNode(token);
            consume(Token.CHAR);
        } else if (match(Token.ESCAPE)) {
            node = createNode(token);
            consume(Token.ESCAPE);
        }

        return node;
    }

    // end productions

    private void readNextToken() {

        if (lexicalAnalyzer.readToken()) {
            token = lexicalAnalyzer.getToken();
        } else {
            token = null;
        }
    }

    private boolean match(Token... pToken) {

        for (Token t : pToken) {
            if (token == t) { // compare refs
                return true;
            }
        }
        return false;
    }

    private Token consume(Token pToken) {
        if (!token.equals(pToken)) {
            parseError(pToken);
        }
        readNextToken();
        return token;
    }

    private Token consume(String c) {
        String tokenValue = lexicalAnalyzer.getTokenValue();
        if (!c.equals(tokenValue)) {
            parseError(new String[] { c });
        }
        readNextToken();
        return token;
    }

    private ASTNode createNode(Token pToken) {

        ASTNode n = new ASTNode(pToken, lexicalAnalyzer.getIndex());
        n.setValue(lexicalAnalyzer.getTokenValue());
        return n;
    }

    private ASTNode addChildNode(ASTNode pParent, Token pToken) {
        ASTNode childNode = createNode(pToken);
        pParent.addChild(childNode);
        return childNode;
    }

    private void addChildNode(ASTNode root, ASTNode n) {
        root.addChild(n);
    }

    private void parseError(Token... tokens) {
        throw new SyntaxException(lexicalAnalyzer.getIndex(), tokens);

    }

    private void parseError(String[] c) {
        throw new SyntaxException(lexicalAnalyzer.getIndex(), c);

    }

    private void parseError(String msg) {
        throw new SyntaxException(lexicalAnalyzer.getIndex(), msg);

    }

}
