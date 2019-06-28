import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DawgTest {
    public static void loadDict(HashMap<String, Integer> map, HashMap<Integer, String> map2) {
        try {
            String fileName = DawgTest.class.getResource("dict.txt").getPath();
            File file = new File(fileName);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line;
            int cnt = 1;
            while ((line = br.readLine()) != null) {
                if (map.containsKey(line)) {
                    continue;
                }
                map.put(line, cnt);
                map2.put(cnt, line);
                cnt += 1;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static void testSave() {
        try {
            Dawg dawg = new Dawg();
            String fileName = DawgTest.class.getResource("dict.txt").getPath();
            File file = new File(fileName);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                dawg.insert(line);
            }
            reader.close();
            dawg.finish();

            dawg.save("./test.bin");
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }


    public static void testLoad() {
        Dawg dawg = new Dawg();
        dawg.load("./test.bin");
//        Scanner sc =new Scanner(System.in);
//        String s = sc.next();

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        HashMap<Integer, String> map2 = new HashMap<Integer, String>();
        loadDict(map, map2);
        int cnt = 1;
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


    public static void dawgTest() {
        try {
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
            for (Map.Entry<String, Integer> iter : map.entrySet()) {
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
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }


    public static void generalMap() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        HashMap<Integer, String> map2 = new HashMap<Integer, String>();
        loadDict(map, map2);
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
    }


    public static void benchmark() {
        // init dawg
        Dawg dawg = new Dawg();
        dawg.load("./test.bin");
        // init hash map
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        HashMap<Integer, String> map2 = new HashMap<Integer, String>();
        loadDict(map, map2);

        long start;
        long end;

        System.out.println("start testing dawg ...");
        start = System.currentTimeMillis();
        for (int i = 0; i < map.size(); ++i) {
            String tmp = dawg.index2word(i);
        }
        end = System.currentTimeMillis();
        System.out.printf("dawg index2word, avg: %.6f\n", (end - start) * 1.0 / map.size());

        start = System.currentTimeMillis();
        for (String key: map.keySet()) {
            int index = dawg.word2index(key);
        }
        end = System.currentTimeMillis();
        System.out.printf("dawg word2index, avg: %.6f\n", (end - start) * 1.0 / map.size());

        start = System.currentTimeMillis();
        for (int i = 0; i < map.size(); ++i) {
            String tmp = map2.get(i);
        }
        end = System.currentTimeMillis();
        System.out.printf("hash map index2word, avg: %.6f\n", (end - start) * 1.0 / map.size());

        start = System.currentTimeMillis();
        for (String key: map.keySet()) {
            int index = map.get(key);
        }
        end = System.currentTimeMillis();
        System.out.printf("hash map word2index, avg: %.6f\n", (end - start) * 1.0 / map.size());
    }

    public static void main(String[] args) {
//        testSave();
//        testLoad();
//        generalMap();
//        dawgTest();
        benchmark();
    }
}
