package sg.edu.np.mad.socrata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> noteArrayList = new ArrayList<>();

    public NoteAdapter(ArrayList<Note> noteArrayList) {
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rcv_note_item, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        TextView title = holder.cardView.findViewById(R.id.noteTitle);
        TextView content = holder.cardView.findViewById(R.id.noteContent);

        title.setText(noteArrayList.get(position).getTitle());
        content.setText(noteArrayList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.noteCardView);
        }
    }
}
