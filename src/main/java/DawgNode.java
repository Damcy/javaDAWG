import java.io.Serializable;
import java.util.ArrayList;
import java.util.prefs.NodeChangeEvent;

public class DawgNode implements Serializable {
    // node
    Integer id;

    // character id, all characters are converted into integer in DAWG
    Integer charId;

    // out coming nodes, sorted by unicode
    ArrayList<Integer> edges;

    // end of word
    boolean isFinal;

    // number of words reachable from this state
    int wordNumber;

    // next id
    static int nextId;

    static {
        nextId = 0;
    }

    public DawgNode() {
        edges = new ArrayList<Integer>();
        id = nextId;
        nextId += 1;
        isFinal = false;
        wordNumber = 0;
    }

    public long toString(ArrayList<DawgNode> nodeBuffer) {
        String nodeStr = charId.toString();
        if (isFinal) {
            nodeStr += "_1";
        } else {
            nodeStr += "_0";
        }
        for (Integer x: edges) {
            nodeStr += ("_" + nodeBuffer.get(x).charId.toString() + "_" + nodeBuffer.get(x).id.toString());
        }
        return MurmurHash.hash64(nodeStr);
    }

    public int reviseEdgePointer(int targetCharId, int newNextNode, ArrayList<DawgNode> nodeBuffer) {
        int targetPos = binarySearch(targetCharId, nodeBuffer);
        if (targetPos < 0) {
            return targetPos;
        }
        edges.set(targetPos, newNextNode);
        return 0;
    }

    public int binarySearch(int targetCharId, ArrayList<DawgNode> nodeBuffer) {
        int low = 0;
        int high = edges.size() - 1;
        if (targetCharId < nodeBuffer.get(edges.get(low)).charId
            || targetCharId > nodeBuffer.get(edges.get(high)).charId
            || low > high) {
            return -2;
        }
        int middle;
        while (low <= high) {
            middle = (low + high) / 2;
            if (nodeBuffer.get(edges.get(middle)).charId > targetCharId) {
                high = middle - 1;
            } else if (nodeBuffer.get(edges.get(middle)).charId < targetCharId) {
                low = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    public int findEdge(int targetCharId, ArrayList<DawgNode> nodeBuffer) {
        int edgeIndex = binarySearch(targetCharId, nodeBuffer);
        if (edgeIndex >= 0) {
            return edges.get(edgeIndex);
        } else {
            return edgeIndex;
        }
    }

    public int edgeCount() {
        return edges.size();
    }

    public int reachableNodeCount(ArrayList<DawgNode> nodeBuffer) {
        if (wordNumber > 0) {
            return wordNumber;
        }
        int tmp = isFinal ? 1 : 0;
        for (int edge: edges) {
            tmp += nodeBuffer.get(edge).reachableNodeCount(nodeBuffer);
        }
        wordNumber = tmp;
        return wordNumber;
    }
}

