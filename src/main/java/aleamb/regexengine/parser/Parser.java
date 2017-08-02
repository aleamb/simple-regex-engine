package aleamb.regexengine.parser;

/**
 * Analizador sintáctico descendente-predictivo-recursivo para una Regex.
 * Codificado a partir de la gramática BNF ampliada.
 * 
 * La entrada será el conjunto de tokens obtenidos mediante el analizador
 * léxico. La salida será un árbol sintáctico de la expresión regular.
 * 
 * Cada método corresponde a una producción de la gramática. Su función es leer
 * los tokens que le correspondan y lanzar error en el monmento en que no llegue
 * el token esperado.
 * 
 * Cada producción construye su correspondiente rama en el árbol sintáctico. Las
 * propias llamadas recursivas del analizador unen unas ramas con otras de forma
 * adecuada.
 * 
 * 
 * Gramática:
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
 * <char> ::= Caracteres imprimibles excepto '(' | ')' | '*' | '+' | '?' | '[' |
 * '\' | '|'
 * 
 * <escape_char> ::= '(' | ')' | '*' | '+' | '?' | '['| '\' | '|' <escape>
 * ::='\' <escape_char> | 's' | 't' | '-' | ']' | | '^' | '.'
 * 
 * <quantifier> ::= '*'| '+' | '?'
 * 
 * 
 * La gramática dada no es LL(1), se necesita un pequeño análisis extra para
 * resolver la ambigüedad de la primera producción.
 * 
 */

public class Parser {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token token;

    public Parser(LexicalAnalyzer pLexicalAnalyzer) {
        lexicalAnalyzer = pLexicalAnalyzer;
    }

    public ASTNode analyze() {

        // un símbolo adelantado
        readNextToken();

        if (token == null) {
            throw new SyntaxException("Regex vacía.");
        }
        // nodo raíz del árbol sintáctico.
        ASTNode root = createNode(Token.ROOT);

        // entramos por la producción principal. A partir de ahí la recursión de
        // producciones consumirá los tokens.
        ASTNode n = prRegexp();

        // análisis sintáctico terminado. Añadir al nodo raiz el nodo padre del
        // árbol sintáctico.
        addChildNode(root, n);

        // no debería haber más tokens si el parseo ha sido correcto.
        if (token != null) {
            parseError("Expresión no balanceada.");
        }

        return root;

    }

    private ASTNode prRegexp() {
        ASTNode node = createNode(Token.REGEXP);

        // al menos un quantified_expression. Es necesario verificar el primero
        // y si no hay, entonces es un error sintáctico

        if (match(Token.CHAR, Token.ESCAPE, Token.L_BRACKET, Token.L_PARENTHESIS)) {
            ASTNode n = prQuantifiedExpression();
            addChildNode(node, n);
        } else {
            parseError(Token.CHAR, Token.ESCAPE, Token.L_BRACKET, Token.L_PARENTHESIS);
        }

        // seguido de cero o más.
        while (match(Token.CHAR, Token.ESCAPE, Token.L_BRACKET, Token.L_PARENTHESIS)) {
            ASTNode n = prQuantifiedExpression();
            addChildNode(node, n);
        }
        // ahora un <regex>
        ASTNode regexNode = prRegex();

        // que puede ser palabra vacia
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

        // o palabra vacia

        return node;
    }

    private ASTNode prQuantifiedExpression() {

        ASTNode node = createNode(Token.QUANTIFIED_EXPR);

        // debe venir una expression
        ASTNode n = prExpression();
        addChildNode(node, n);

        // opcionalmente puede venir un cuantificador
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

        // evita un rango vacio
        if (lexicalAnalyzer.getTokenValue().equals("]")) {
            parseError(Token.CHAR, Token.ESCAPE);
        }
        /*
         * Se observa que en el bucle while se puede consumir cualquier carácter
         * excepto ']'. El léxico enviará tokens como L_PARENTHESIS o L_BRACKET
         * si huiera caracteres '(' y '[', pero en este caso se ignoran y se
         * tratan como CHAR.
         */
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

    // fin de los métodos asociados a las producciones.

    private void readNextToken() {

        if (lexicalAnalyzer.readToken()) {
            token = lexicalAnalyzer.getToken();
        } else {
            token = null;
        }
    }

    private boolean match(Token... pToken) {

        for (Token t : pToken) {
            if (token == t) { // uso == por null-safe
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
