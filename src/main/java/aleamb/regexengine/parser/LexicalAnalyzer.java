package aleamb.regexengine.parser;

import java.util.Arrays;

/**
 * Lexer for regex engine.
 * 
 */
public class LexicalAnalyzer {

    // regular expression
    private String regex;
    // lexer position
    private int index;
    // last token build
    private Token token = null;
    // last character read.
    private char character;
    // valor del token.
    private String tokenValue;

    // ignored characters
    private char[] ignoredChars = { '\t', ' ' };

    // set of permitted chars for to scape
    private char[] escape_chars = { '(', ')', '*', '+', '-', '.', '?', '[', '\\', ']', '^', 's', 't', '|' };

    // value for scape chars.
    private char[] escape_chars_value = { '(', ')', '*', '+', '-', '.', '?', '[', '\\', ']', '^', ' ', '\t', '|' };

    public LexicalAnalyzer(String pRegex) {

        Arrays.sort(escape_chars);

        if (pRegex == null) {
            throw new IllegalArgumentException("Empty regex");
        }
        regex = pRegex;
        index = 0;
        character = 0;
    }

    public boolean readToken() throws LexerException {
        boolean charRead = readCharacter();
        if (charRead) {
            switch (character) {
            case '[':
                token = Token.L_BRACKET;
                tokenValue = "[";
                break;
            case '(':
                token = Token.L_PARENTHESIS;
                tokenValue = "(";
                break;
            case ')':
                token = Token.R_PARENTHESIS;
                tokenValue = ")";
                break;
            case '+':
            case '*':
            case '?':
                token = Token.QUANTIFIER;
                tokenValue = String.valueOf(character);
                break;
            case '|':
                token = Token.PIPE;
                tokenValue = "|";
                break;
            case '\\':
                verifyEscapeCharacters();
                break;
            default:
                if (isValidChar(character)) {
                    token = Token.CHAR;
                    tokenValue = String.valueOf(character);
                } else {
                    lexError("Charecter not valid.");
                }
            }
        }
        return charRead;
    }

    public int getIndex() {
        return index;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getTokenValue() {
        return tokenValue.toString();
    }

    private boolean isValidChar(char pCharacter) {
        return (pCharacter > 0x20 && pCharacter < Character.MAX_VALUE);
    }

    /*
     * Detected scape sequence. Check is character is in set of permitted
     * character for to scape.
     */
    private void verifyEscapeCharacters() {

        if (readCharacter()) {
            int charIndex = findEscapedChar(character);
            if (charIndex >= 0) {
                token = Token.ESCAPE;
                tokenValue = String.valueOf(escape_chars_value[charIndex]);
            } else {
                lexError("Character " + character + " can not be scaped");
            }
        } else {
            lexError(" " + Arrays.toString(escape_chars));
        }
    }

    private int findEscapedChar(char pCharacter) {
        return Arrays.binarySearch(escape_chars, pCharacter);
    }

    private boolean isIgnoredChar(char pCharacter) {
        return (Arrays.binarySearch(ignoredChars, pCharacter) >= 0);
    }

    /*
     * 
     */
    private boolean readCharacter() {

        boolean charRead = false;

        while (!charRead && index < regex.length()) {
            character = regex.charAt(index++);
            charRead = !isIgnoredChar(character);
        }
        return charRead;

    }

    private void lexError(String errorMessage) {
        throw new LexerException(index, errorMessage);

    }

}
