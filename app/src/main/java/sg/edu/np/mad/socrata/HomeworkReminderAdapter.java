package sg.edu.np.mad.socrata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeworkReminderAdapter extends RecyclerView.Adapter<HomeworkReminderAdapter.HomeworkReminderViewHolder> {

    private final ArrayList<HomeworkReminder> homeworkReminderArrayList = new ArrayList<HomeworkReminder>();

    public HomeworkReminderAdapter() {
        homeworkReminderArrayList.add(new HomeworkReminder(1, "Day"));
    }

    @NonNull
    @Override
    public HomeworkReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_notification, parent, false);

        return new HomeworkReminderAdapter.HomeworkReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkReminderViewHolder holder, int position) {
        HomeworkReminder homeworkReminder = getHomeworkReminderArrayList().get(position);

        holder.textViewReminderTime.setText(homeworkReminder.makeText());

        // Delete notification
        holder.imageButtonDeleteReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHomeworkReminderArrayList().remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return getHomeworkReminderArrayList().size();
    }

    public ArrayList<HomeworkReminder> getHomeworkReminderArrayList() {
        return homeworkReminderArrayList;
    }

    public void addHomeworkReminder(HomeworkReminder homeworkReminder) {
        homeworkReminderArrayList.add(homeworkReminder);
        notifyItemInserted(getItemCount() - 1);
    }

    static class HomeworkReminderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewReminderTime;
        ImageButton imageButtonDeleteReminder;


        public HomeworkReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewReminderTime = itemView.findViewById(R.id.textViewReminderTime);
            imageButtonDeleteReminder = itemView.findViewById(R.id.imageButtonDeleteReminder);
        }
    }
}
