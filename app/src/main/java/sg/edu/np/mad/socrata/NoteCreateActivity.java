package sg.edu.np.mad.socrata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NoteCreateActivity extends AppCompatActivity {

    FirebaseUtils firebaseUtils;
    ArrayList<Homework> homeworkArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);

        Intent intent = getIntent();
        String homeworkName = intent.getStringExtra("homework_name");

        Button button = findViewById(R.id.btnCreateNote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtViewContent = findViewById(R.id.editTextContent);

                String content = txtViewContent.getText().toString();

                Note note = new Note(content);

                LocalStorage localStorage = new LocalStorage(NoteCreateActivity.this);
                User user = localStorage.getUser();
                ArrayList<Module> moduleArrayList = user.getModuleArrayList();
                homeworkArrayList = HomeworkUtils.getAllHomework(moduleArrayList);
                Homework homework = homeworkArrayList.get(HomeworkUtils.findHomework(homeworkArrayList, homeworkName));

                ArrayList<Note> noteArrayList = homework.getNoteArrayList();
                homework.addNote(note);

                localStorage = new LocalStorage(NoteCreateActivity.this);
                firebaseUtils = new FirebaseUtils();

                localStorage.setNoteArrayList(noteArrayList, homeworkArrayList, homeworkName, homework.getModuleName());
                firebaseUtils.updateNoteArrayList(noteArrayList, homeworkArrayList, moduleArrayList, homeworkName, homework.getModuleName());

                Intent intent = new Intent(NoteCreateActivity.this, NoteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("homework_name", homeworkName);
                startActivity(intent);
            }
        });
    }
}