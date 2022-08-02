package sg.edu.np.mad.socrata;

public class Note {
    private String content;
    private String homeworkName;
    private String moduleName;

    public Note() {

    }

    public Note(String content) {
        this.setContent(content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHomeworkName() {return homeworkName;}

    public void setHomeworkName(String homeworkName) { this.homeworkName = homeworkName; }

    public String getModuleName() { return moduleName; }

    public void setModuleName(String moduleName) { this.moduleName = moduleName; }

}
