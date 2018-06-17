package nl.itsjaap.pmdfinal.models;

import java.util.ArrayList;
import java.util.Date;

public class BackupModel {

    private ArrayList<CourseModel> userData;
    private String userName;
    private Date timestamp;

    private BackupModel() {} // needed for the dataChangeListener

    public BackupModel(ArrayList<CourseModel> userData, String userName) {
        this.userData = userData;
        this.userName = userName;
        this.timestamp = new java.util.Date();
    }

    public ArrayList<CourseModel> getUserData() {
        return userData;
    }

    public String getUserName() {
        return userName;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
