package sg.edu.np.mad.socrata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkRecyclerViewHolder> {
    ArrayList<Module> data;
    public HomeworkAdapter(ArrayList<Module> data) {this.data = data;}
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public HomeworkRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View c = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_list_homework, null, false);
        return new HomeworkRecyclerViewHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkRecyclerViewHolder holder, int position) {
        Module m = data.get(position);
        holder.moduleText.setText(m.getModuleName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
