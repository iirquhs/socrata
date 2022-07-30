package sg.edu.np.mad.socrata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        Intent intentFromNote = getIntent();
        String content = intentFromNote.getStringExtra("note_content");
        String homeworkName = intentFromNote.getStringExtra("homework_name");

        TextView noteDetailsText = findViewById(R.id.noteDetailsText);
        noteDetailsText.setText(content);

        Button deleteBtn = findViewById(R.id.deleteNoteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = noteDetailsText.getText().toString();


                LocalStorage localStorage = new LocalStorage(NoteDetailsActivity.this);
                User user = localStorage.getUser();
                ArrayList<Module> moduleArrayList = user.getModuleArrayList();
                ArrayList<Homework> homeworkArrayList = HomeworkUtils.getAllHomework(moduleArrayList);
                Homework homework = homeworkArrayList.get(HomeworkUtils.findHomework(homeworkArrayList, homeworkName));

                ArrayList<Note> noteArrayList = homework.getNoteArrayList();
                int pos = 0;
                for (int i = 0; i < noteArrayList.size(); i++) {
                    if (noteArrayList.get(i).getContent().equals(content)) {
                        pos = i;
                    }
                }
                noteArrayList.remove(pos);
                homework.setNoteArrayList(noteArrayList);

                localStorage = new LocalStorage(NoteDetailsActivity.this);
                FirebaseUtils firebaseUtils = new FirebaseUtils();

                localStorage.setNoteArrayList(noteArrayList, homeworkArrayList, homeworkName, homework.getModuleName());
                firebaseUtils.updateNoteArrayList(noteArrayList, homeworkArrayList, moduleArrayList, homeworkName, homework.getModuleName());

                Intent intent = new Intent(NoteDetailsActivity.this, NoteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}