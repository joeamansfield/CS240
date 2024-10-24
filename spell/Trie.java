package spell;

public class Trie implements ITrie{
    int wordCount = 0;
    int nodeCount = 1;
    INode root = new Node();

    public void add(String word) {
        addHelper(word, root);
    }
    private void addHelper(String word, INode node) {
        if (word.length() > 0) {
            int index = word.charAt(0) - 'a';
            if (node.getChildren()[index] == null) {
                node.getChildren()[index] = new Node();
                nodeCount++;
            }
            addHelper(word.substring(1), node.getChildren()[index]);
        }
        else {
            if (node.getValue() == 0) {
                wordCount++;
            }
            node.incrementValue();
        }
    }

    public INode find(String word) {
        return findHelper(root, word);
    }

    private INode findHelper (INode node, String word) {
        if (word.length() == 0) {
            if (node.getValue() > 0) {
                return node;
            }
            else {
                return null;
            }
        }
        int index = word.charAt(0) - 'a';
        if (node.getChildren()[index] != null) {
            return findHelper(node.getChildren()[index], word.substring(1));
        }
        else {
            return null;
        }
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder currentWord = new StringBuilder();
        StringBuilder result = new StringBuilder();
        toStringHelper(result, currentWord, root);
        return result.toString();
    }

    private void toStringHelper(StringBuilder result, StringBuilder currentWord, INode node) {
        if (node.getValue() > 0) {
            result.append(currentWord + "\n");
        }
        for (int i = 0; i < 26; i++) {
            char currChar = (char)('a' + i);
            currentWord.append(currChar);
            if (node.getChildren()[i] != null) {
                toStringHelper(result, currentWord, node.getChildren()[i]);
            }
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }

    @Override
    public int hashCode() {
        int x = 0;
        for (int i = 0; i < 26; i++) {
            x++;
            if (root.getChildren()[i] != null) {
                break;
            }
        }
        return wordCount * nodeCount * x;
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Trie obj = (Trie)o;
        if (this.getWordCount() != obj.getWordCount()) {
            return false;
        }
        if (this.getNodeCount() != obj.getNodeCount()) {
            return false;
        }
        return equalsHelper(root, obj.root);
    }

    private boolean equalsHelper(INode node1, INode node2) {
        if (node1.getValue() != node2.getValue()) {
            return false;
        }
        for (int i = 0; i < 26; i++) {
            boolean hasChild1 = (node1.getChildren()[i] != null);
            boolean hasChild2 = (node2.getChildren()[i] != null);
            if (hasChild1 != hasChild2) {
                return false;
            }
            boolean equal = true;
            if (hasChild1 && hasChild2) {
                equal = equalsHelper(node1.getChildren()[i], node2.getChildren()[i]);
            }
            if (!equal) {
                return false;
            }
        }
        return true;
    }
}
