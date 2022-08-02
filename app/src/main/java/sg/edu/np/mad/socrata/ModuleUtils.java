package sg.edu.np.mad.socrata;

import androidx.annotation.ColorInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModuleUtils {

    static ArrayList<String> getModuleNames(ArrayList<Module> moduleArrayList) {
        ArrayList<String> moduleNames = new ArrayList<>();
        for (Module module : moduleArrayList) {
            moduleNames.add(module.getModuleName());
        }
        return moduleNames;
    }

    static int findModule(ArrayList<Module> moduleArrayList, String moduleName) {
        for (int i = 0; i < moduleArrayList.size(); i++) {
            if (moduleArrayList.get(i).getModuleName().equals(moduleName)) {
                return i;
            }
        }
        return -1;
    }

    static void replaceModule(ArrayList<Module> moduleArrayList, Module module, String moduleName) {
        for (int i = 0; i < moduleArrayList.size(); i++) {
            if (moduleArrayList.get(i).getModuleName().equals(moduleName)) {
                moduleArrayList.set(i, module);
            }
        }
    }

    static void removeModule(ArrayList<Module> moduleArrayList, Module module) {
        moduleArrayList.remove(module);
    }

}
