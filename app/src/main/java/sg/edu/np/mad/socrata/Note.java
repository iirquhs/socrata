package sg.edu.np.mad.socrata;

public class Note {
    private String title;
    private String content;

    public Note() {

    }

    public Note(String title, String content) {
        this.setTitle(title);
        this.setContent(content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
