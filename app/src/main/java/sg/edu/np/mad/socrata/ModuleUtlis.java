package sg.edu.np.mad.socrata;

import androidx.annotation.ColorInt;

import java.util.HashMap;
import java.util.Map;

public class ModuleUtlis {

    // Parse the map from the firebase and convert it to hashmap
    static Map<String, Module> parseModuleMap (Map < String, Object > modules){
        Map<String, Module> moduleMap = new HashMap<>();

        if (modules == null) {
            return null;
        }

        for (Map.Entry<String, Object> moduleMapEntry : modules.entrySet()) {
            Map moduleMapValue = (Map) moduleMapEntry.getValue();

            String name = (String) moduleMapValue.get("moduleName");
            String goal = (String) moduleMapValue.get("targetGrade");
            int targetHoursPerWeek = ((Number) moduleMapValue.get("targetHoursPerWeek")).intValue();
            @ColorInt int colorInt = ((Number) moduleMapValue.get("color")).intValue();
            ;

            Module module = new Module(name, goal, targetHoursPerWeek, colorInt);

            moduleMap.put(moduleMapEntry.getKey(), module);
        }

        return moduleMap;
    }
}
