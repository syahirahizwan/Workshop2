package my.edu.utem.ftmk.workshop2;

public class Candidates {

    String Course, Faculty, FullName, Manifesto, Url, Image;
    private boolean isChecked = false;

    public Candidates ()
    {

    }

    public Candidates(String Course, String Faculty, String FullName, String Manifesto, String Url, String Image)
    {
        this.Course = Course;
        this.Faculty = Faculty;
        this.FullName = FullName;
        this.Manifesto = Manifesto;
        this.Url = Url;
        this.Image = Image;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getManifesto() {
        return Manifesto;
    }

    public void setManifesto(String manifesto) {
        Manifesto = manifesto;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
}
