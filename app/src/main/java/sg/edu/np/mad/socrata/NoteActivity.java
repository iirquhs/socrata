package sg.edu.np.mad.socrata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intentFromHomework = getIntent();
        String homeworkName = intentFromHomework.getStringExtra("homework_name");

        FirebaseUtils firebaseUtils;
        LocalStorage localStorage = new LocalStorage(NoteActivity.this);
        User user = localStorage.getUser();

        ArrayList<Module> moduleArrayList = user.getModuleArrayList();
        ArrayList<Homework> homeworkArrayList = HomeworkUtils.getAllHomework(moduleArrayList);
        ArrayList<Note> noteArrayList = new ArrayList<>();
        Homework homework = homeworkArrayList.get(HomeworkUtils.findHomework(homeworkArrayList, homeworkName));
        noteArrayList.addAll(homework.getNoteArrayList());
        for (Note note : noteArrayList) {
            note.setHomeworkName(homework.getHomeworkName());
        }

        if (noteArrayList.size() == 0) {
            Intent intent = new Intent(NoteActivity.this, NoteCreateActivity.class);
            intent.putExtra("homework_name", homeworkName);
            startActivity(intent);
        }

        RecyclerView rcv = findViewById(R.id.noteRecyclerView);
        rcv.setHasFixedSize(true);

        rcv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        NoteAdapter adapter = new NoteAdapter(noteArrayList);
        rcv.setAdapter(adapter);


        FloatingActionButton createNoteBtn = findViewById(R.id.createNoteBtn);
        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, NoteCreateActivity.class);
                intent.putExtra("homework_name", homeworkName);
                startActivity(intent);
            }
        });
    }
}