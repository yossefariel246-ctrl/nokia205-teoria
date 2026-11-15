import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * אפליקציית תרגול תיאוריה לנוקיה 205 (MIDP 2.0)
 * מסך 320x240, עברית
 */
public class TheoryMidlet extends MIDlet implements CommandListener {

    private Display display;
    private List mainMenu;
    private Form questionForm;
    private Command exitCmd;
    private Command backCmd;
    private Command nextCmd;
    private Command startCmd;

    private QuestionBank bank;
    private int currentIndex;
    private int correctCount;
    private int totalAnswered;
    private ChoiceGroup answersGroup;
    private StringItem questionText;
    private StringItem feedback;

    public TheoryMidlet() {
        bank = new QuestionBank();
    }

    public void startApp() {
        display = Display.getDisplay(this);
        exitCmd = new Command("יציאה", Command.EXIT, 1);
        backCmd = new Command("חזרה", Command.BACK, 2);
        nextCmd = new Command("הבא", Command.OK, 1);
        startCmd = new Command("התחל", Command.OK, 1);

        showMainMenu();
    }

    private void showMainMenu() {
        mainMenu = new List("תיאוריה - נוקיה 205", List.IMPLICIT);
        mainMenu.append("תרגול שאלות", null);
        mainMenu.append("סימולציית מבחן (בקרוב)", null);
        mainMenu.append("אודות", null);
        mainMenu.addCommand(exitCmd);
        mainMenu.setCommandListener(this);
        display.setCurrent(mainMenu);
    }

    private void startPractice() {
        currentIndex = 0;
        correctCount = 0;
        totalAnswered = 0;
        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= bank.size()) {
            showResults();
            return;
        }

        Question q = bank.get(currentIndex);

        questionForm = new Form("שאלה " + (currentIndex + 1) + "/" + bank.size());
        questionText = new StringItem(null, q.text);
        questionForm.append(questionText);

        answersGroup = new ChoiceGroup("בחר תשובה:", Choice.EXCLUSIVE);
        for (int i = 0; i < q.answers.length; i++) {
            answersGroup.append(q.answers[i], null);
        }
        questionForm.append(answersGroup);

        feedback = new StringItem(null, "");
        questionForm.append(feedback);

        questionForm.addCommand(nextCmd);
        questionForm.addCommand(backCmd);
        questionForm.setCommandListener(this);
        display.setCurrent(questionForm);
    }

    private void checkAnswer() {
        int selected = answersGroup.getSelectedIndex();
        if (selected < 0) {
            feedback.setText("בחר תשובה קודם");
            return;
        }

        Question q = bank.get(currentIndex);
        totalAnswered++;
        if (selected == q.correctIndex) {
            correctCount++;
            feedback.setText("נכון!");
        } else {
            feedback.setText("שגוי. התשובה הנכונה: " + q.answers[q.correctIndex]);
        }
    }

    private void showResults() {
        Form results = new Form("תוצאות");
        results.append(new StringItem(null, "ענית נכון על " + correctCount + " מתוך " + totalAnswered));
        results.addCommand(backCmd);
        results.setCommandListener(this);
        display.setCurrent(results);
    }

    private void showAbout() {
        Form about = new Form("אודות");
        about.append(new StringItem(null,
            "תרגול תיאוריה לנוקיה 205\n" +
            "מבוסס על מאגר משרד התחבורה\n" +
            "גרסה ראשונית - דוגמאות בלבד\n\n" +
            "השאלות המלאות ב-data.gov.il"));
        about.addCommand(backCmd);
        about.setCommandListener(this);
        display.setCurrent(about);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCmd) {
            destroyApp(true);
            notifyDestroyed();
            return;
        }

        if (c == backCmd) {
            showMainMenu();
            return;
        }

        if (d == mainMenu && c == List.SELECT_COMMAND) {
            int idx = mainMenu.getSelectedIndex();
            if (idx == 0) {
                startPractice();
            } else if (idx == 2) {
                showAbout();
            } else {
                Alert a = new Alert("בקרוב", "סימולציה מלאה תתווסף בהמשך", null, AlertType.INFO);
                a.setTimeout(2000);
                display.setCurrent(a, mainMenu);
            }
            return;
        }

        if (d == questionForm && c == nextCmd) {
            // בדיקה + מעבר לשאלה הבאה
            checkAnswer();
            currentIndex++;
            showQuestion();
        }
    }

    public void pauseApp() {}
    public void destroyApp(boolean unconditional) {}
}
