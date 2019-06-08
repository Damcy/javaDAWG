import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Dawg {
    // previous word
    private String previousWord;

    // root
    private DawgNode root;

    // node buffer
    ArrayList<DawgNode> nodeBuffer;

    // unchecked nodes, for building process
    private ArrayList<Pair<Integer, Integer>> uncheckedNodes;

    // minimized nodes
    private HashMap<Long, Integer> minimizedNodes;

    public Dawg() {
        nodeBuffer = new ArrayList<DawgNode>();
        uncheckedNodes = new ArrayList<Pair<Integer, Integer>>();
        minimizedNodes = new HashMap<Long, Integer>();
        root = new DawgNode();
        root.isFinal = true;
        nodeBuffer.add(root);
        previousWord = "";
    }

    public int insert(String word) {
        if (previousWord.compareTo(word) >= 0) {
            return -1;
        }
        char[] charArray = word.toCharArray();
        int tmp = Math.min(word.length(), previousWord.length());
        int commonPrefix = 0;
        for (int i = 0; i < tmp; ++i) {
            if (charArray[i] != previousWord.charAt(i)) {
                break;
            }
            ++commonPrefix;
        }
        // Check the uncheckedNodes for redundant nodes, proceeding from last
        // one down to the common prefix size. Then truncate the list at that
        // point.
        minimize(commonPrefix);

        // add the suffix, starting from the correct node mid-way through the
        // graph
        int nodeId = 0;
        if (uncheckedNodes.size() > 0) {
            nodeId = uncheckedNodes.get(uncheckedNodes.size() - 1).getValue();
        }

        for (int i = commonPrefix; i < word.length(); ++i) {
            DawgNode newNode = new DawgNode();
//            System.out.printf("newNode id %d\n", newNode.id);
            newNode.charId = (int)charArray[i];
//            System.out.printf("insert process %c\n", charArray[i]);
            nodeBuffer.add(newNode);
            // insert edge into node
            nodeBuffer.get(nodeId).edges.add(newNode.id);
//            System.out.printf("link %d to %d\n", nodeId, newNode.id);
            uncheckedNodes.add(new Pair<Integer, Integer>(nodeId, newNode.id));
            nodeId = newNode.id;
        }
        // end of a word
        nodeBuffer.get(nodeId).isFinal = true;
        previousWord = word;
        return 0;
    }

    public int finish() {
        minimize(0);
        nodeBuffer.get(root.id).reachableNodeCount(nodeBuffer);
        System.out.printf("root node edges count: %d\n", root.edgeCount());
        System.out.printf("root node word number: %d\n", root.wordNumber);
        return 0;
    }

    private void minimize(int downTo) {
        for (int i = uncheckedNodes.size() - 1; i >= downTo; --i) {
            int nodeId = uncheckedNodes.get(i).getKey();
            int nextNodeId = uncheckedNodes.get(i).getValue();
            long hashKey = nodeBuffer.get(nextNodeId).toString(nodeBuffer);
//            System.out.printf("hash map size: %d\n", minimizedNodes.size());
//            System.out.printf("hashKey %d, containsKey %b\n", hashKey, minimizedNodes.containsKey(hashKey));
            if (minimizedNodes.containsKey(hashKey)) {
                nodeBuffer.get(nodeId).reviseEdgePointer(nodeBuffer.get(nextNodeId).charId, minimizedNodes.get(hashKey), nodeBuffer);
                nodeBuffer.remove(nextNodeId);
                DawgNode.nextId -= 1;
            } else {
                minimizedNodes.put(hashKey, nextNodeId);
            }
            uncheckedNodes.remove(i);
        }
    }

    public int word2index(String word) {
        char[] charArray = word.toCharArray();
        int wordId = 0;
        int tmpId = root.id;
        for (char ch: charArray) {
            int charId = (int)ch;
//            System.out.printf("process %c, now wordId %d, tmpId %d\n", ch, wordId, tmpId);
            int targetNodeId = nodeBuffer.get(tmpId).findEdge(charId, nodeBuffer);
            if (targetNodeId < 0) {
                return targetNodeId;
            }
            for (int x: nodeBuffer.get(tmpId).edges) {
                if (nodeBuffer.get(x).charId == charId) {
                    if (nodeBuffer.get(tmpId).isFinal) {
                        wordId += 1;
                    }
                    tmpId = x;
                    break;
                }
                wordId += nodeBuffer.get(x).wordNumber;
            }
        }
        if (nodeBuffer.get(tmpId).isFinal) {
            return wordId;
        }
        return -3;
    }

    public String index2word(int index) {
        String result = "";
        int tmpId = root.id;
        if (index < 0 || index > nodeBuffer.get(tmpId).wordNumber) {
            return result;
        }
        while (index > 0) {
            for (int x: nodeBuffer.get(tmpId).edges) {
                if (nodeBuffer.get(x).wordNumber < index) {
                    index -= nodeBuffer.get(x).wordNumber;
                } else {
                    result += (char)nodeBuffer.get(x).charId.intValue();
                    tmpId = x;
                    if (nodeBuffer.get(tmpId).isFinal) {
                        index -= 1;
                    }
                    break;
                }
            }
        }

        if (!nodeBuffer.get(tmpId).isFinal) {
            return "";
        }
        return result;
    }
}