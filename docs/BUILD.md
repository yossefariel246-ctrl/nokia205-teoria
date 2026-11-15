# איך לבנות את ה-JAR לנוקיה 205

## אפשרות 1: NetBeans + Java ME
1. התקן NetBeans עם Java ME SDK
2. File → New Project → Java ME → Mobile Application
3. העתק את קבצי ה-src לתוך הפרויקט
4. הגדר CLDC 1.1 + MIDP 2.0
5. Build → Create Package → יווצר JAR + JAD

## אפשרות 2: Wireless Toolkit (WTK) / Oracle Java ME SDK
1. צור פרויקט חדש
2. העתק את הקבצים
3. Build

## קובץ JAD לדוגמה (אחרי בניה)
```
MIDlet-Name: Teoria Nokia 205
MIDlet-Version: 1.0.0
MIDlet-Vendor: Open Source
MIDlet-1: Teoria, , TheoryMidlet
MicroEdition-Profile: MIDP-2.0
MicroEdition-Configuration: CLDC-1.1
MIDlet-Jar-URL: teoria.jar
MIDlet-Jar-Size: XXXX
```

## התקנה על הנוקיה
- העבר את קובץ ה-JAR (ולפעמים גם ה-JAD) דרך Bluetooth או כרטיס microSD
- בטלפון: תפריט → יישומים / Java → התקן

## הערות למסך הקטן
- הימנע מטקסטים ארוכים מדי בשאלה אחת
- תמונות תמרורים דורשות עיבוד מיוחד (PNG קטן מאוד)
