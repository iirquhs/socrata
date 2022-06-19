package sg.edu.np.mad.socrata;

import java.util.ArrayList;
import java.util.Map;

public class ModuleUtils {

    public static ArrayList<String> getModuleNames(Map<String,Object> users) {
        //iterate through each user, ignoring their UID
        ArrayList<String> nameList = new ArrayList<>();

        if (users == null) {
            return nameList;
        }

        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            String name = (String) singleUser.get("moduleName");
            nameList.add(name);

        }
        return nameList;
    }

    public static boolean doesModuleExists(ArrayList<String> nameList, String name) {
        for (int i = 0; i < nameList.size();i++) {
            if (name.equals(nameList.get(i))) {
                return true;
            }
        }
        return false;
    }

}
