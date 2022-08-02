package sg.edu.np.mad.socrata;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FirebaseUtils {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String userUID;

    {
        assert user != null;
        userUID = user.getUid();
    }

    public void updateUser(User user) {
        db.collection("Users").document(userUID).set(user);
    }

    public void updateModuleArrayList(ArrayList<Module> moduleArrayList) {
        db.collection("Users").document(userUID).update("moduleArrayList", moduleArrayList);
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(this, "Failed to update information", Toast.LENGTH_SHORT);
//                    }
//                });
    }

    public void updateHomeworkArrayList(ArrayList<Homework> homeworkArrayList, ArrayList<Module> moduleArrayList, String moduleName) {
        Module module = moduleArrayList.get(ModuleUtils.findModule(moduleArrayList, moduleName));
        module.setHomeworkArrayList(homeworkArrayList);

        updateModuleArrayList(moduleArrayList);

    }

//    public void updateNoteArrayList(Homework homework, User user) {
//        ArrayList<Module> moduleArrayList = user.getModuleArrayList();
//        ArrayList<Homework> homeworkArrayList = HomeworkUtils.getAllHomework(moduleArrayList);
//
//        homeworkArrayList.set(HomeworkUtils.findHomework(homeworkArrayList, homework.getHomeworkName()), homework);
//
//        updateHomeworkArrayList(homeworkArrayList, moduleArrayList, homework.getModuleName());
//    }

    public void updateNoteArrayList(ArrayList<Note> noteArrayList, ArrayList<Homework> homeworkArrayList,
                                    ArrayList<Module> moduleArrayList, String homeworkTitle, String moduleName) {
        Homework homework = homeworkArrayList.get(HomeworkUtils.findHomework(homeworkArrayList, homeworkTitle));
        homework.setNoteArrayList(noteArrayList);

        updateHomeworkArrayList(homeworkArrayList, moduleArrayList, moduleName);
    }
}
