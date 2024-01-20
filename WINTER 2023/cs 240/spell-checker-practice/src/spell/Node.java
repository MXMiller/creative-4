package spell;

public class Node implements INode{

    int value;

    Node[] children;

    public Node() {
        value = 0;
        children = new Node[26];
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void incrementValue() {
        value++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
