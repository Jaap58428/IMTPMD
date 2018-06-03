package nl.itsjaap.pmdfinal.models;

public class CourseModel {

    private String name;
    private String credits;
    private String grade;
    private String period;
    private String year;
    private String isOpt;
    private String user;
    private String note;

    public CourseModel(String n, String c, String p, String y, String i) {
        this.name = n;
        this.credits = c;
        this.grade = null;
        this.period = p;
        this.year = y;
        this.isOpt = i;
        this.user = null;
        this.note = "";
    }

    public String getName() {
        return name;
    }

    public String getCredits() {
        return credits;
    }

    public String getGrade() {
        return grade;
    }

    public String getPeriod() {
        return period;
    }

    public String getYear() {
        return year;
    }

    public String getIsOpt() {
        return isOpt;
    }

    public String getUser() {
        return user;
    }

    public String getNote() {
        return note;
    }
}