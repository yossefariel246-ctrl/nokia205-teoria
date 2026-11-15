/**
 * שאלה בודדת במבחן תיאוריה
 */
public class Question {
    public String text;
    public String[] answers;
    public int correctIndex; // 0-based
    public String category;  // חוקי התנועה / תמרורים / בטיחות / הכרת הרכב

    public Question(String text, String[] answers, int correctIndex, String category) {
        this.text = text;
        this.answers = answers;
        this.correctIndex = correctIndex;
        this.category = category;
    }
}
