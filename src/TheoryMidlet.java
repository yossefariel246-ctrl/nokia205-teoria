import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * תרגול + מבחן תיאוריה לנוקיה 205
 * ניווט מקלדת בלבד (בלי מגע)
 * מבחן: 30 שאלות, 40 דקות, עובר ב-26 נכונות
 */
public class TheoryMidlet extends MIDlet implements CommandListener {

    private static final int EXAM_QUESTIONS = 30;
    private static final int EXAM_MINUTES = 40;
    private static final int PASS_SCORE = 26;

    private Display display;
    private List mainMenu;
    private Form questionForm;
    private Form resultForm;

    private Command exitCmd;
    private Command backCmd;
    private Command okCmd;
    private Command nextCmd;

    private QuestionBank bank;
    private int[] order;
    private int currentPos;
    private int correctCount;
    private int answeredCount;
    private boolean examMode;
    private boolean waitingFeedback;

    private ChoiceGroup answersGroup;
    private StringItem questionText;
    private StringItem feedbackItem;
    private StringItem timerItem;

    private Timer timer;
    private int secondsLeft;
    private boolean timeUp;

    public TheoryMidlet() {
        bank = new QuestionBank();
    }

    public void startApp() {
        display = Display.getDisplay(this);
        exitCmd = new Command("יציאה", Command.EXIT, 1);
        backCmd = new Command("חזרה", Command.BACK, 2);
        okCmd = new Command("אישור", Command.OK, 1);
        nextCmd = new Command("המשך", Command.OK, 1);
        showMainMenu();
    }

    private void showMainMenu() {
        stopTimer();
        mainMenu = new List("תיאוריה - נוקיה 205", List.IMPLICIT);
        mainMenu.append("תרגול (כל השאלות)", null);
        mainMenu.append("מבחן (30 שאלות, 40 דק')", null);
        mainMenu.append("אודות", null);
        mainMenu.addCommand(exitCmd);
        mainMenu.setCommandListener(this);
        display.setCurrent(mainMenu);
    }

    private void startPractice() {
        examMode = false;
        timeUp = false;
        int n = bank.size();
        if (n == 0) {
            showAlert("אין שאלות", "קובץ השאלות לא נטען");
            return;
        }
        order = new int[n];
        for (int i = 0; i < n; i++) order[i] = i;
        currentPos = 0;
        correctCount = 0;
        answeredCount = 0;
        waitingFeedback = false;
        showQuestion();
    }

    private void startExam() {
        examMode = true;
        timeUp = false;
        if (bank.size() < EXAM_QUESTIONS) {
            showAlert("שגיאה", "אין מספיק שאלות במאגר");
            return;
        }
        order = bank.randomIndices(EXAM_QUESTIONS);
        currentPos = 0;
        correctCount = 0;
        answeredCount = 0;
        waitingFeedback = false;
        secondsLeft = EXAM_MINUTES * 60;
        startTimer();
        showQuestion();
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (timeUp) return;
                secondsLeft--;
                if (secondsLeft <= 0) {
                    secondsLeft = 0;
                    timeUp = true;
                    stopTimer();
                    display.callSerially(new Runnable() {
                        public void run() {
                            finishExamByTime();
                        }
                    });
                } else {
                    display.callSerially(new Runnable() {
                        public void run() {
                            updateTimerDisplay();
                        }
                    });
                }
            }
        }, 1000, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void updateTimerDisplay() {
        if (timerItem != null && examMode && !timeUp) {
            int m = secondsLeft / 60;
            int s = secondsLeft % 60;
            String t = (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
            timerItem.setText("זמן: " + t);
        }
    }

    private void showQuestion() {
        if (timeUp) {
            finishExamByTime();
            return;
        }
        if (currentPos >= order.length) {
            showResults();
            return;
        }

        Question q = bank.get(order[currentPos]);
        waitingFeedback = false;

        questionForm = new Form("שאלה " + (currentPos + 1) + "/" + order.length);
        if (examMode) {
            int m = secondsLeft / 60;
            int s = secondsLeft % 60;
            String t = (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
            timerItem = new StringItem(null, "זמן: " + t);
            questionForm.append(timerItem);
        }

        questionText = new StringItem(null, q.text);
        questionForm.append(questionText);

        answersGroup = new ChoiceGroup("בחר:", Choice.EXCLUSIVE);
        for (int i = 0; i < q.answers.length; i++) {
            answersGroup.append((i + 1) + ". " + q.answers[i], null);
        }
        questionForm.append(answersGroup);

        feedbackItem = new StringItem(null, "");
        questionForm.append(feedbackItem);

        questionForm.addCommand(okCmd);
        questionForm.addCommand(backCmd);
        questionForm.setCommandListener(this);
        display.setCurrent(questionForm);
    }

    private void onConfirmAnswer() {
        if (waitingFeedback) {
            currentPos++;
            showQuestion();
            return;
        }

        int selected = answersGroup.getSelectedIndex();
        if (selected < 0) {
            feedbackItem.setText("בחר תשובה עם החיצים ואז אישור");
            return;
        }

        Question q = bank.get(order[currentPos]);
        answeredCount++;
        if (selected == q.correctIndex) {
            correctCount++;
            feedbackItem.setText("נכון!");
        } else {
            feedbackItem.setText("שגוי. נכון: " + (q.correctIndex + 1) + ". " + q.answers[q.correctIndex]);
        }
        waitingFeedback = true;

        questionForm.removeCommand(okCmd);
        questionForm.addCommand(nextCmd);
    }

    private void finishExamByTime() {
        stopTimer();
        showResults();
    }

    private void showResults() {
        stopTimer();
        resultForm = new Form("תוצאות");
        String msg;
        if (examMode) {
            boolean passed = correctCount >= PASS_SCORE;
            msg = "מבחן הסתיים\n"
                + "נכונות: " + correctCount + " מתוך " + EXAM_QUESTIONS + "\n"
                + (passed ? "עברת!" : "לא עברת (צריך " + PASS_SCORE + ")")
                + (timeUp ? "\n(הזמן נגמר)" : "");
        } else {
            msg = "תרגול הסתיים\nנכונות: " + correctCount + " מתוך " + answeredCount;
        }
        resultForm.append(new StringItem(null, msg));
        resultForm.addCommand(backCmd);
        resultForm.setCommandListener(this);
        display.setCurrent(resultForm);
    }

    private void showAbout() {
        Form about = new Form("אודות");
        about.append(new StringItem(null,
            "תיאוריה לנוקיה 205\n"
            + "מאגר משרד התחבורה\n"
            + "שאלות במאגר: " + bank.size() + "\n\n"
            + "מבחן: 30 שאלות, 40 דקות\n"
            + "ציון עובר: 26\n\n"
            + "ניווט: חיצים + מקשי תוכנה"));
        about.addCommand(backCmd);
        about.setCommandListener(this);
        display.setCurrent(about);
    }

    private void showAlert(String title, String text) {
        Alert a = new Alert(title, text, null, AlertType.INFO);
        a.setTimeout(3000);
        display.setCurrent(a, mainMenu);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCmd) {
            stopTimer();
            destroyApp(true);
            notifyDestroyed();
            return;
        }
        if (c == backCmd) {
            stopTimer();
            showMainMenu();
            return;
        }
        if (d == mainMenu && c == List.SELECT_COMMAND) {
            int idx = mainMenu.getSelectedIndex();
            if (idx == 0) startPractice();
            else if (idx == 1) startExam();
            else if (idx == 2) showAbout();
            return;
        }
        if (d == questionForm) {
            if (c == okCmd || c == nextCmd) {
                onConfirmAnswer();
            }
        }
    }

    public void pauseApp() {}
    public void destroyApp(boolean unconditional) {
        stopTimer();
    }
}
