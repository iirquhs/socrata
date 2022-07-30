package sg.edu.np.mad.socrata;

import java.util.ArrayList;

public class HomeworkUtils {
    static ArrayList<Homework>[] splitByIsCompletedHomeworkArrayList(ArrayList<Module> moduleArrayList) {
        ArrayList<Homework> notCompletedHomeworkArrayList = new ArrayList<>();
        ArrayList<Homework> completedHomeworkArrayList = new ArrayList<>();

        for (Module module : moduleArrayList) {
            for (Homework homework : module.getHomeworkArrayList()) {
                if (homework.getIsCompleted()) {
                    completedHomeworkArrayList.add(homework);
                }
                else {
                    notCompletedHomeworkArrayList.add(homework);
                }
            }
        }

        return new ArrayList[] {notCompletedHomeworkArrayList, completedHomeworkArrayList};
    }

    static ArrayList<Homework>[] splitByIsCompletedHomeworkArrayList(Module module) {
        ArrayList<Homework> notCompletedHomeworkArrayList = new ArrayList<>();
        ArrayList<Homework> completedHomeworkArrayList = new ArrayList<>();

        for (Homework homework : module.getHomeworkArrayList()) {
            if (homework.getIsCompleted()) {
                completedHomeworkArrayList.add(homework);
                continue;
            }

            notCompletedHomeworkArrayList.add(homework);
        }

        return new ArrayList[]{notCompletedHomeworkArrayList, completedHomeworkArrayList};
    }

    static ArrayList<Homework> getAllHomework(ArrayList<Module> moduleArrayList) {
        ArrayList<Homework> homeworkArrayList = new ArrayList<>();
        for (Module module : moduleArrayList) {
            homeworkArrayList.addAll(module.getHomeworkArrayList());
        }
        return homeworkArrayList;
    }

    static void replaceHomework(ArrayList<Homework> homeworkArrayList, Homework homeworkToReplace, String homeworkName, String moduleName) {
        for (int i = 0; i < homeworkArrayList.size(); i++) {
            Homework homework = homeworkArrayList.get(i);
            if (homework.getModuleName().equals(moduleName) && homework.getHomeworkName().equals(homeworkName)) {
                homeworkArrayList.set(i, homeworkToReplace);
            }
        }
    }

    static void removeHomework(ArrayList<Homework> homeworkArrayList, String homeworkName, String moduleName) {
        for (Homework homework : homeworkArrayList) {
            if (homework.getHomeworkName().equals(homeworkName) && homework.getModuleName().equals(moduleName)) {
                homeworkArrayList.remove(homework);
                break;
            }
        }
    }

    static int findHomework(ArrayList<Homework> homeworkArrayList, String homeworkName) {
        for (int i = 0; i < homeworkArrayList.size(); i++) {
            if (homeworkArrayList.get(i).getHomeworkName().equals(homeworkName)) {
                return i;
            }
        }
        return -1;
    }
}
