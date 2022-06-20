package sg.edu.np.mad.socrata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkRecyclerViewHolder> {
    ArrayList<Homework> data;
    public HomeworkAdapter(ArrayList<Homework> data) {this.data = data;}
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public HomeworkRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View c = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_list_homework, parent, false);
        return new HomeworkRecyclerViewHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkRecyclerViewHolder holder, int position) {
        Homework h = data.get(position);
        holder.moduleText.setText(h.getModule().getModuleName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
