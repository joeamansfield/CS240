package spell;

public class Node implements INode{
    private INode[] children = new INode[26];
    private int value = 0;

    public int getValue() {
        return value;
    }

    public void incrementValue() {
        value++;
    }

    public INode[] getChildren() {
        return children;
    }
}
