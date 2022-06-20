package sg.edu.np.mad.socrata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkRecyclerViewHolder> {
    ArrayList<Homework> data;

    public class HomeworkRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView moduleText, textViewHomeworkName, textViewTimeLeft;

        ConstraintLayout constraintUrgentIndicator;

        public HomeworkRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleText = itemView.findViewById(R.id.homeworkModule);
            textViewHomeworkName = itemView.findViewById(R.id.textViewHomeworkName);
            textViewTimeLeft = itemView.findViewById(R.id.textViewTimeLeft);

            constraintUrgentIndicator = itemView.findViewById(R.id.constraintUrgentIndicator);
        }
    }

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
        Homework homework = data.get(position);
        holder.moduleText.setBackgroundColor(homework.getModule().getColor());
        holder.moduleText.setText(homework.getModule().getModuleName());
        holder.textViewHomeworkName.setText(homework.getHomeworkName());

        LocalDateTime dueDateTime = homework.getDueDateTime();

        Duration duration = Duration.between(LocalDateTime.now(), dueDateTime);
        Long second = duration.getSeconds();

        holder.textViewTimeLeft.setText(String.format("%.2f", second.floatValue() / 3600.0) + "h");

        // 7 Days
        if (second > 604800) {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), R.color.green_color));
        // 1 Day
        } else if (second > 86400) {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), com.github.dhaval2404.colorpicker.R.color.yellow_400));
        } else {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), R.color.red_color));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
