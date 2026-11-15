# בניית JAR לנוקיה 205

## דרישות
- Java ME SDK / Wireless Toolkit / NetBeans + Java ME
- יעד: **CLDC 1.1 + MIDP 2.0**

## שלבים (NetBeans לדוגמה)
1. New Project → Java ME → Mobile Application  
2. העתק את `src/*.java`  
3. העתק את `res/questions.txt` כך שייארז **בשורש ה-JAR** (נתיב בתוך ה-JAR: `/questions.txt`)  
4. Build → Create Package  

## חשוב מאוד
`QuestionBank` טוען את השאלות כך:
```java
getClass().getResourceAsStream("/questions.txt")
```
אם הקובץ לא בשורש ה-JAR – האפליקציה תעלה בלי שאלות.

## JAD לדוגמה
```
MIDlet-Name: Teoria Nokia 205
MIDlet-Version: 1.0.0
MIDlet-Vendor: Open Source
MIDlet-1: Teoria, , TheoryMidlet
MicroEdition-Profile: MIDP-2.0
MicroEdition-Configuration: CLDC-1.1
MIDlet-Jar-URL: teoria.jar
MIDlet-Jar-Size: (גודל אחרי בנייה)
```

## מגבלות נוקיה 205
- מסך 320×240  
- זיכרון מוגבל – אם יש OutOfMemory, אפשר לצמצם את `questions.txt`  
- אין מגע – רק חיצים ומקשי תוכנה (OK / חזרה)

## התקנה
Bluetooth או כרטיס microSD → תפריט יישומים / Java → התקן.
