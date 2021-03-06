package aleamb.regexengine.parser;

import aleamb.regexengine.RegexException;

public class LexerException extends RegexException {

    private static final long serialVersionUID = -3200992432865220339L;

    private int position;

    public LexerException(int index, String errorMessage) {

        super("Error in position " + index + ". " + errorMessage);
        position = index;
    }

    public int getPosition() {
        return position;
    }

}
