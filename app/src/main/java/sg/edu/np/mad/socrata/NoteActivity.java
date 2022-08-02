package sg.edu.np.mad.socrata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {


    private ArrayList<Note> noteArrayList;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intentFromHomework = getIntent();
        String homeworkName = intentFromHomework.getStringExtra("homework_name");
        String moduleName = intentFromHomework.getStringExtra("module_name");

        LocalStorage localStorage = new LocalStorage(NoteActivity.this);
        User user = localStorage.getUser();
        ArrayList<Module> moduleArrayList = user.getModuleArrayList();
        Module module = moduleArrayList.get(ModuleUtils.findModule(moduleArrayList, moduleName));
        ArrayList<Homework> homeworkArrayList = module.getHomeworkArrayList();
        Homework homework = homeworkArrayList.get(HomeworkUtils.findHomework(homeworkArrayList, homeworkName));
        noteArrayList = new ArrayList<>();
        noteArrayList.addAll(homework.getNoteArrayList());
        for (Note note : noteArrayList) {
            note.setHomeworkName(homework.getHomeworkName());
            note.setModuleName(module.getModuleName());
        }

        if (noteArrayList.size() == 0) {
            Intent intent = new Intent(NoteActivity.this, NoteCreateActivity.class);
            intent.putExtra("homework_name", homeworkName);
            intent.putExtra("module_name", moduleName);
            startActivity(intent);
        }

        SearchView searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return false;
            }
        });
        RecyclerView rcv = findViewById(R.id.noteRecyclerView);
        rcv.setHasFixedSize(true);

        rcv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NoteAdapter(noteArrayList);
        rcv.setAdapter(adapter);


        FloatingActionButton createNoteBtn = findViewById(R.id.createNoteBtn);
        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, NoteCreateActivity.class);
                intent.putExtra("homework_name", homeworkName);
                intent.putExtra("module_name", moduleName);
                startActivity(intent);
            }
        });
    }

    private void filterList(String text) {
        ArrayList<Note> filteredList = new ArrayList<>();
        for (Note item : noteArrayList) {
            if (item.getContent().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No note found.", Toast.LENGTH_SHORT).show();
        }
        else {
            adapter.setFilteredList(filteredList);
        }
    }
}