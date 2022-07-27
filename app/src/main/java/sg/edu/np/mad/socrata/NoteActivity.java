package sg.edu.np.mad.socrata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.np.mad.socrata.databinding.ActivityMainBinding;
import sg.edu.np.mad.socrata.databinding.ActivityNoteBinding;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ArrayList<Note> noteArrayList = new ArrayList<>();
        Note note1 = new Note("Title1", "Content1");
        Note note2 = new Note("Title2", "Content2");
        noteArrayList.add(note1);
        noteArrayList.add(note2);
        noteArrayList.add(note1);
        noteArrayList.add(note2);
        noteArrayList.add(note1);
        noteArrayList.add(note2);


        RecyclerView rcv = findViewById(R.id.noteRecyclerView);
        rcv.setHasFixedSize(true);

        rcv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        NoteAdapter adapter = new NoteAdapter(noteArrayList);
        rcv.setAdapter(adapter);
    }
}