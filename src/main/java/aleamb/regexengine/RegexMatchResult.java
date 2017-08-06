package aleamb.regexengine;

/**
 * 
 * Usado por {@link Regex#match(char[], RegexMatchResult)} para registrar la
 * posición de una coincidencia.
 * 
 * Used by {@link Regex#match(char[], RegexMatchResult)} for to register the
 * position of ocurrence.
 * 
 * Can be used too for to point out the position from where to read buffer.
 */

public class RegexMatchResult {

    private int position = 0;

    private int matchStartPosition = -1;
    private int matchLength = 0;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMatchStartPosition() {
        return matchStartPosition;
    }

    public void setMatchStartPosition(int matchStartPosition) {
        this.matchStartPosition = matchStartPosition;
    }

    public int getMatchLength() {
        return matchLength;
    }

    public void setMatchLength(int matchLength) {
        this.matchLength = matchLength;
    }

}
