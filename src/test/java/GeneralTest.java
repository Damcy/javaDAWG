import java.util.ArrayList;

public class GeneralTest {
    public static void main(String[] args) {
        String testStr = "一二十户日C#";
        char[] charArray = testStr.toCharArray();
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        for (char ch: charArray) {
//            System.out.println(ch);
//            System.out.println((int)ch);
            tmp.add((int)ch);
        }
        for (int x: tmp) {
            System.out.println(x);
            System.out.println((char)x);
        }
    }
}
