package aleamb.regexengine.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Nodo para el árbol sintáctico.
 * 
 */
public class ASTNode {

    private String value = "";
    private List<ASTNode> children;
    private Token type;
    private int positionInRegex;

    public ASTNode(Token pType, int position) {

        type = pType;
        children = new ArrayList<ASTNode>();
        positionInRegex = position;
    }

    public ASTNode(Token pType, String pValue) {

        type = pType;
        value = pValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Iterable<ASTNode> getChildren() {

        return Collections.unmodifiableList(children);
    }

    public void addChild(ASTNode childNode) {
        children.add(childNode);
    }

    public Token getType() {
        return type;
    }

    public void setType(Token type) {
        this.type = type;
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public int getChildrenCount() {
        return children.size();
    }

    public int getPositionInRegex() {
        return positionInRegex;
    }

    /**
     * Representación del nodo en Graphviz/DOT
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append(hashCode() + " [label=\"" + readValue() + "\"];\n");

        for (ASTNode child : children) {
            buffer.append(String.valueOf(hashCode()) + " -- " + child.hashCode() + ";\n");
            buffer.append(child.toString());
        }

        return buffer.toString();

    }

    private String readValue() {
        return type.toString() + " ('" + value + "')";

    }

}
