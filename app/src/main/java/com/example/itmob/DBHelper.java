package com.example.itmob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";
    private Context dbContext;

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
        dbContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE VERTRAG(vertragID INT PRIMARY KEY, StartlaufZeit TEXT, EndLaufZeit Text, Preis TEXT, Vorname TEXT, Nachname TEXT, Geburtsdatum TEXT, Email TEXT, KuendigungVorgemerkt INT)");
        db.execSQL("CREATE TABLE UEBUNG(uebungID INT PRIMARY KEY, Name TEXT, Muskelgruppe Text, Wiederholungen TEXT, Saetze TEXT)");
        db.execSQL("CREATE TABLE USER(userID INT PRIMARY KEY, Email TEXT, Passwort TEXT, VertragID INT, FOREIGN KEY(VertragID) REFERENCES VERTRAG(vertragid), FOREIGN KEY(Email) REFERENCES VERTRAG(Email))");
        db.execSQL("CREATE TABLE USER_UEBUNG(user_uebungID INT PRIMARY KEY, USERID INT, UEBUNGID INT, FOREIGN KEY(USERID) REFERENCES USER(userID), FOREIGN KEY(UEBUNGID) REFERENCES UEBUNG(uebungID))");
        db.execSQL("CREATE TABLE ACTIVEUSER(userID INT PRIMARY KEY)");



        // table for courses
        db.execSQL("CREATE TABLE courses_table(courseid INTEGER PRIMARY KEY AUTOINCREMENT , courseName TEXT, courseTrainer TEXT, courseDate TEXT, courseStartTime TEXT , courseTimeDuration TEXT)");
        // creating table for participants of a particular course
        db.execSQL("CREATE TABLE PARTICIPANTS(courseId INTEGER  , vertragEmail TEXT , pID INTEGER PRIMARY KEY)");

        db.execSQL("INSERT INTO VERTRAG (vertragID, startlaufzeit, endlaufzeit, preis, vorname, nachname, geburtsdatum, email ) VALUES (1, '2022-06-23', '2024-06-23', '39.99','Khalid','Butt','06-05-1997', 'kb' );");
        db.execSQL("INSERT INTO VERTRAG (vertragID, startlaufzeit, endlaufzeit, preis, vorname, nachname, geburtsdatum, email ) VALUES (2, '2021-03-06', '2024-03-06', '24.00','Markus','Ruehl','23-08-1978', 'mr' );");
        db.execSQL("INSERT INTO VERTRAG (vertragID, startlaufzeit, endlaufzeit, preis, vorname, nachname, geburtsdatum, email ) VALUES (3, '2020-02-05', '2024-02-05', '19.99','Ronnie','Coleman','02-10-1988', 'rc' );");

        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (1);");
        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (2);");
        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (3);");
        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (4);");
        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (5);");
        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (6);");
        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (7);");
        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (8);");
        db.execSQL("INSERT INTO ACTIVEUSER (userID) VALUES (9);");

        db.execSQL("INSERT INTO courses_table (courseid, courseName, courseTrainer, courseDate, courseStartTime, courseTimeDuration ) VALUES (999, 'SLIM FIT', 'ISRAR ALI', '30-06-2022','8:15','45 minutes' );");
        db.execSQL("INSERT INTO courses_table (courseid, courseName, courseTrainer, courseDate, courseStartTime, courseTimeDuration ) VALUES (510, 'BODY Fit', 'AHMAD', '30-06-2022','9:15','50 minutes' );");
        db.execSQL("INSERT INTO courses_table (courseid, courseName, courseTrainer, courseDate, courseStartTime, courseTimeDuration ) VALUES (515, 'Slim Fit', 'Adnan', '28-06-2022','9:15','50 minutes' );");
        db.execSQL("INSERT INTO courses_table (courseid, courseName, courseTrainer, courseDate, courseStartTime, courseTimeDuration ) VALUES (495, 'Slim Fit', 'Zeeshan', '01-07-2022','10:15','50 minutes' );");
        db.execSQL("INSERT INTO courses_table (courseid, courseName, courseTrainer, courseDate, courseStartTime, courseTimeDuration ) VALUES (395, 'Body Fit', 'Ajmal', '02-07-2022','8:15','40 minutes' );");







    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists USER");
        db.execSQL("drop Table if exists UEBUNG");
        db.execSQL("drop Table if exists USER_UEBUNG");
        db.execSQL("drop Table if exists VERTRAG");

    }

    public void deleteUser(String email){


        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        myDB.execSQL("DELETE FROM USER WHERE Email LIKE '"+email+"'");


    }


    public int getActiveUser(){
        int count = 0;
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = myDB.rawQuery("Select Count(*) from ACTIVEUSER", new String[]{});

        if (cursor.moveToFirst()) {

            count = cursor.getInt(0);
            return count;
        }

        return count;

    }


    public Boolean insertDataUser(String email, String password, int vertragsID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("Email", email);
        contentValues.put("Passwort", password);
        contentValues.put("VertragID", vertragsID);

        long result = MyDB.insert("USER", null, contentValues);
        if (result == -1) return false;
        else
            return true;
    }

    public boolean updateKuendigungsstatus(String vertragsID){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        myDB.execSQL("UPDATE VERTRAG SET KuendigungVorgemerkt=1 WHERE vertragID='"+vertragsID+"'");

        return false;
    }

    public boolean getKuendigungsStatus(String vertragsID){

        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = myDB.rawQuery("Select * from VERTRAG where vertragID= ?", new String[]{vertragsID});

        if (cursor.moveToFirst()){

            int status = cursor.getInt(8);

            if(status==0){
                return false;
            }
            else {
                return true;
            }

        }
        return false;

    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from USER where Email = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkvertrag(String email, String vertragsID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from VERTRAG where VertragID = ? and Email = ?", new String[]{vertragsID, email});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean vertragsIDFromUser(String vertragsID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from VERTRAG where vertragID = ?", new String[]{vertragsID});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from USER where Email = ? and Passwort = ?", new String[]{email, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public void dropTable(String table) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String query = "DROP TABLE " + table;
        MyDB.execSQL(query);
    }

    public ArrayList<String> getUserData(String searchEmail){

        ArrayList<String> userData = new ArrayList<>();

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from VERTRAG where Email = ?", new String[]{searchEmail});

        if (cursor.moveToFirst()){

            String startLaufzeit = cursor.getString(1);
            String endLaufzeit = cursor.getString(2);
            String preis = cursor.getString(3);
            String vorname = cursor.getString(4);
            String nachname = cursor.getString(5);
            String geburtsdatum = cursor.getString(6);
            String email = cursor.getString(7);
            String vertragsnummer = cursor.getString(0);

            userData.add(startLaufzeit);
            userData.add(endLaufzeit);
            userData.add(preis);
            userData.add(vorname);
            userData.add(nachname);
            userData.add(geburtsdatum);
            userData.add(email);
            userData.add(vertragsnummer);

        }
        cursor.close();
        return userData;
    }


    public List<ModelCourses> getAllCourses(){

        List<ModelCourses> coursesList = new ArrayList<>();

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from courses_table",null);


        if (cursor.moveToFirst()){

            do {

                String courseId = cursor.getString(0);
                String courseName = cursor.getString(1);
                String courseTrainer = cursor.getString(2);
                String courseDate = cursor.getString(3);
                String courseStartTime = cursor.getString(4);
                String courseTimeDuration = cursor.getString(5);


                coursesList.add(new ModelCourses(courseId,courseName , courseTrainer , courseDate , courseStartTime ,courseTimeDuration));




            }while (cursor.moveToNext());


        }
        cursor.close();

        return coursesList ;

    }


    public List<ModelCourses> getCoursesByDate(String date){

        List<ModelCourses> coursesList = new ArrayList<>();

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from courses_table WHERE courseDate=?",new String[]{date});

        if (cursor.moveToFirst()){

            do {

                String courseId = cursor.getString(0);
                String courseName = cursor.getString(1);
                String courseTrainer = cursor.getString(2);
                String courseDate = cursor.getString(3);
                String courseStartTime = cursor.getString(4);
                String courseTimeDuration = cursor.getString(5);


                coursesList.add(new ModelCourses(courseId,courseName , courseTrainer , courseDate , courseStartTime ,courseTimeDuration));




            }while (cursor.moveToNext());


        }
        cursor.close();

        return coursesList ;

    }

    public boolean insertParticipant(int courseId , String email){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("courseId", courseId);
        contentValues.put("vertragEmail", email);


        long result = MyDB.insert("PARTICIPANTS", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else{
            Toast.makeText(dbContext, "Congrats You've participated", Toast.LENGTH_SHORT).show();;
            return  true ;
        }



    }

    // method to check If user has already participated


    public boolean checkIfParticipated(String courseId , String email){

        SQLiteDatabase MyDB = this.getWritableDatabase();
//        String query = "SELECT * FROM PARTICIPANTS WHERE courseId =?";
//        MyDB.execSQL(query,new Integer[]{courseId});

        Cursor cursor = MyDB.rawQuery("Select * from PARTICIPANTS where courseId = ? AND vertragEmail = ?", new String[]{courseId,email});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;

    }


    // Unrolling user from the course


    public boolean unRollUserFromCourse(String courseId){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("DELETE FROM PARTICIPANTS WHERE courseId = ?",new String[]{courseId});

        if (cursor.getCount() > 0)
            return true;
        else
            return false;

    }



    // Get All Participants of a Course

    public List<String> gettingAllParticipants(String courseId){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT vertragEmail FROM PARTICIPANTS WHERE courseId =?",new String[]{courseId});


        List<String> list = new ArrayList<>();


        if (cursor.moveToFirst()){

            do {

                String email = cursor.getString(0);
                list.add(email);


            }while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }


    // To Check Participants' names


    public String getParticipantsName(String searchEmail){

        ArrayList<String> namesList = new ArrayList<>();

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from VERTRAG where Email = ?", new String[]{searchEmail});

        if (cursor.moveToFirst()){


            String vorname = cursor.getString(4);
            String nachname = cursor.getString(5);


            namesList.add(vorname+" "+nachname);



        }
        cursor.close();
        return namesList.get(0);

    }


}