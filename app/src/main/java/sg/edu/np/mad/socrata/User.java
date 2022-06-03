package sg.edu.np.mad.socrata;

import java.util.ArrayList;

public class User {

    private String username;
    private String email;

    private ArrayList<Module> moduleArrayList = new ArrayList<>();

    public User() {

    }

    public User(String username, String email) {
        this.setUsername(username);
        this.setEmail(email);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Module> getModuleArrayList() {
        return moduleArrayList;
    }

    public void setModuleArrayList(ArrayList<Module> moduleArrayList) {
        this.moduleArrayList = moduleArrayList;
    }
}
