import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.Random;

/**
 * טוען שאלות מ-questions_a/b/c/core.txt
 * פורמט: imgId|שאלה|ת1|ת2|ת3|ת4|correct|קטגוריה
 */
public class QuestionBank {

    private Question[] questions;

    public QuestionBank() {
        Vector list = new Vector();
        loadFile("/questions_core.txt", list);
        loadFile("/questions_a.txt", list);
        loadFile("/questions_b.txt", list);
        loadFile("/questions_c.txt", list);
        loadFile("/questions.txt", list);
        questions = new Question[list.size()];
        for (int i = 0; i < list.size(); i++) {
            questions[i] = (Question) list.elementAt(i);
        }
    }

    private void loadFile(String path, Vector list) {
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream(path);
            if (is == null) return;
            InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            StringBuffer lineBuf = new StringBuffer();
            int c;
            while ((c = reader.read()) != -1) {
                if (c == '\n' || c == '\r') {
                    if (lineBuf.length() > 0) {
                        addLine(lineBuf.toString(), list);
                        lineBuf.setLength(0);
                    }
                } else {
                    lineBuf.append((char) c);
                }
            }
            if (lineBuf.length() > 0) addLine(lineBuf.toString(), list);
            reader.close();
        } catch (Exception e) {
        } finally {
            if (is != null) try { is.close(); } catch (Exception e) {}
        }
    }

    private void addLine(String line, Vector list) {
        Vector parts = split(line, '|');
        if (parts.size() < 7) return;
        try {
            String imgId = (String) parts.elementAt(0);
            String qtext = (String) parts.elementAt(1);
            String[] ans = new String[4];
            ans[0] = (String) parts.elementAt(2);
            ans[1] = (String) parts.elementAt(3);
            ans[2] = (String) parts.elementAt(4);
            ans[3] = (String) parts.elementAt(5);
            int correct = Integer.parseInt(((String) parts.elementAt(6)).trim());
            String cat = parts.size() > 7 ? (String) parts.elementAt(7) : "";
            if (qtext.length() > 0 && correct >= 0 && correct <= 3) {
                list.addElement(new Question(qtext, ans, correct, cat, imgId));
            }
        } catch (Exception e) {}
    }

    private Vector split(String s, char sep) {
        Vector v = new Vector();
        StringBuffer cur = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == sep) {
                v.addElement(cur.toString());
                cur.setLength(0);
            } else cur.append(ch);
        }
        v.addElement(cur.toString());
        return v;
    }

    public int size() {
        return questions != null ? questions.length : 0;
    }

    public Question get(int index) {
        return questions[index];
    }

    public int[] randomIndices(int count) {
        int n = size();
        if (n == 0) return new int[0];
        if (count > n) count = n;
        int[] idx = new int[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Random rnd = new Random(System.currentTimeMillis());
        for (int i = 0; i < count; i++) {
            int j = i + (Math.abs(rnd.nextInt()) % (n - i));
            int tmp = idx[i];
            idx[i] = idx[j];
            idx[j] = tmp;
        }
        int[] result = new int[count];
        System.arraycopy(idx, 0, result, 0, count);
        return result;
    }
}
