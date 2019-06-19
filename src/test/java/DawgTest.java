import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DawgTest {

    public static void testSave() throws IOException {
        Dawg dawg = new Dawg();
        String fileName = DawgTest.class.getResource("dict.txt").getPath();
        File file = new File(fileName);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        BufferedReader br = new BufferedReader(reader);
        String line;
        int cnt = 1;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        while ((line = br.readLine()) != null) {
            if (map.containsKey(line)) {
                continue;
            }
            dawg.insert(line);
            map.put(line, cnt);
            cnt += 1;
        }
        reader.close();
        dawg.finish();

        dawg.save("/Users/Damcy/github/dawg/src/main/resources/test.bin");
    }


    public static void testLoad() throws IOException, ClassNotFoundException {
        Dawg dawg = new Dawg();
        dawg.load("/Users/Damcy/github/dawg/src/main/resources/test.bin");

        String fileName = DawgTest.class.getResource("dict.txt").getPath();
        File file = new File(fileName);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        BufferedReader br = new BufferedReader(reader);
        String line;
        int cnt = 1;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        while ((line = br.readLine()) != null) {
            if (map.containsKey(line)) {
                continue;
            }
            map.put(line, cnt);
            cnt += 1;
        }
        reader.close();

        cnt = 1;
        for (Map.Entry<String, Integer> iter: map.entrySet()) {
            if (cnt % 100000 == 0) {
                System.out.println(cnt);
            }
            if (!iter.getKey().equals(dawg.index2word(iter.getValue()))) {
                System.out.printf("not equal, word: %s, id: %d, index2word: %s\n",
                        iter.getKey(), iter.getValue(), dawg.index2word(iter.getValue()));
                break;
            }
            if (!iter.getValue().equals(dawg.word2index(iter.getKey()))) {
                System.out.printf("not equal, word: %s, id: %d, word2index: %s\n",
                        iter.getKey(), iter.getValue(), dawg.word2index(iter.getKey()));
            }
            cnt += 1;
        }
    }


    public static void dawgTest() throws IOException {
        Dawg dawg = new Dawg();
        String fileName = DawgTest.class.getResource("dict.txt").getPath();
        File file = new File(fileName);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        BufferedReader br = new BufferedReader(reader);
        String line;
        int cnt = 1;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        while ((line = br.readLine()) != null) {
            if (map.containsKey(line)) {
                continue;
            }
            dawg.insert(line);
            map.put(line, cnt);
            cnt += 1;
        }
        reader.close();
        dawg.finish();

        cnt = 1;
        for (Map.Entry<String, Integer> iter: map.entrySet()) {
            if (cnt % 100000 == 0) {
                System.out.println(cnt);
            }
            if (!iter.getKey().equals(dawg.index2word(iter.getValue()))) {
                System.out.printf("not equal, word: %s, id: %d, index2word: %s\n",
                        iter.getKey(), iter.getValue(), dawg.index2word(iter.getValue()));
                break;
            }
            if (!iter.getValue().equals(dawg.word2index(iter.getKey()))) {
                System.out.printf("not equal, word: %s, id: %d, word2index: %s\n",
                        iter.getKey(), iter.getValue(), dawg.word2index(iter.getKey()));
            }
            cnt += 1;
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        testLoad();
    }
}
