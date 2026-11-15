# בניית JAR לנוקיה 205

## יעד
CLDC 1.1 + MIDP 2.0

## משאבים שחייבים בתוך ה-JAR
| נתיב ב-JAR | מקור |
|------------|------|
| `/questions_a.txt` וכו' | `res/questions_*.txt` |
| `/img/XXXX.jpg` | חלץ מ-`res/img_phone.zip` או מתיקיית `res/img/` |

## שלבים
1. חלץ את `img_phone.zip` לתיקיית `res/img/` (אם עדיין לא)
2. צור פרויקט Java ME והעתק את `src/*.java`
3. הוסף למשאבי ה-JAR:
   - את קבצי `questions_*.txt` בשורש
   - את כל קבצי `res/img/*.jpg` תחת תיקיית `img/` ב-JAR
4. Build → Create Package

## תמונות
כ־314 תמונות מוקטנות (~160px, ~600KB סה״כ) – מתאימות למסך 320×240.
נטענות אחת-אחת רק כשמוצגת השאלה.

## JAD
```
MIDlet-Name: Teoria Nokia 205
MIDlet-Version: 1.1.0
MIDlet-Vendor: Open Source
MIDlet-1: Teoria, , TheoryMidlet
MicroEdition-Profile: MIDP-2.0
MicroEdition-Configuration: CLDC-1.1
```
