/**
 * שאלה בודדת במבחן תיאוריה
 */
public class Question {
    public String text;
    public String[] answers;
    public int correctIndex;
    public String category;
    /** מזהה תמונה (שם קובץ בלי .jpg) או מחרוזת ריקה */
    public String imageId;

    public Question(String text, String[] answers, int correctIndex, String category, String imageId) {
        this.text = text;
        this.answers = answers;
        this.correctIndex = correctIndex;
        this.category = category != null ? category : "";
        this.imageId = imageId != null ? imageId : "";
    }
}
