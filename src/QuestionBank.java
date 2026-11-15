import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.Random;

/**
 * טוען את מאגר השאלות מקובץ המשאב /questions.txt
 */
public class QuestionBank {

    private Question[] questions;

    public QuestionBank() {
        Vector list = new Vector();
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream("/questions.txt");
            if (is == null) {
                questions = new Question[0];
                return;
            }
            InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            StringBuffer lineBuf = new StringBuffer();
            String qText = null;
            Vector answers = new Vector();
            int correct = -1;
            String cat = "";
            int c;
            while ((c = reader.read()) != -1) {
                if (c == '\n' || c == '\r') {
                    if (lineBuf.length() == 0) continue;
                    String line = lineBuf.toString();
                    lineBuf.setLength(0);
                    if (line.startsWith("Q|")) {
                        qText = line.substring(2);
                        answers = new Vector();
                        correct = -1;
                        cat = "";
                    } else if (line.startsWith("A|")) {
                        answers.addElement(line.substring(2));
                    } else if (line.startsWith("C|")) {
                        try {
                            correct = Integer.parseInt(line.substring(2).trim());
                        } catch (Exception e) {
                            correct = -1;
                        }
                    } else if (line.startsWith("CAT|")) {
                        cat = line.substring(4);
                    } else if (line.equals("---")) {
                        if (qText != null && answers.size() == 4 && correct >= 0 && correct <= 3) {
                            String[] ans = new String[4];
                            for (int i = 0; i < 4; i++) {
                                ans[i] = (String) answers.elementAt(i);
                            }
                            list.addElement(new Question(qText, ans, correct, cat));
                        }
                        qText = null;
                        answers = new Vector();
                        correct = -1;
                        cat = "";
                    }
                } else {
                    lineBuf.append((char) c);
                }
            }
            reader.close();
        } catch (Exception e) {
            // empty on error
        } finally {
            if (is != null) {
                try { is.close(); } catch (Exception e) {}
            }
        }
        questions = new Question[list.size()];
        for (int i = 0; i < list.size(); i++) {
            questions[i] = (Question) list.elementAt(i);
        }
    }

    public int size() {
        return questions != null ? questions.length : 0;
    }

    public Question get(int index) {
        return questions[index];
    }

    /** מחזיר count אינדקסים אקראיים שונים */
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
