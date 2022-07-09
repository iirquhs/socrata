package sg.edu.np.mad.socrata;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkRecyclerViewHolder> {

    ArrayList<Homework> homeworkViewArrayList;
    ArrayList<Module> moduleArrayList;

    FirebaseUtils firebaseUtils;
    LocalStorage localStorage;

    public HomeworkAdapter(ArrayList<Homework> homeworkArrayList, ArrayList<Module> moduleArrayList) {
        this.homeworkViewArrayList = homeworkArrayList;
        this.moduleArrayList = moduleArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public HomeworkRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View c = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_list_homework, parent, false);

        firebaseUtils = new FirebaseUtils();
        localStorage = new LocalStorage((Activity) parent.getContext());

        return new HomeworkRecyclerViewHolder(c);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkRecyclerViewHolder holder, int position) {
        Homework homework = homeworkViewArrayList.get(position);

        Module module = moduleArrayList.get(ModuleUtils.findModule(moduleArrayList, homework.getModuleName()));

        holder.moduleText.setBackgroundColor(module.getColor());
        holder.moduleText.setText(module.getModuleName());
        holder.textViewHomeworkName.setText(homework.getHomeworkName());

        LocalDateTime dueDateTime = homework.ConvertDueDateTime(homework.getDueDateTimeString());

        long second = homework.CalculateSecondsLeftBeforeDueDate(dueDateTime);

        holder.textViewTimeLeft.setText(String.format("%.2f h", (float) second / 3600.0));

        // 7 Days
        if (second > 604800) {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), R.color.homework_green_color));
            // 1 Day
        } else if (second > 86400) {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), R.color.homework_yellow_color));
        } else {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), R.color.homework_red_color));
        }

        // Mark homework as completed
        holder.buttonMarkDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homework.setIsCompleted(true);

                Toast.makeText(view.getContext(), homework.getHomeworkName() + " Mark as Done", Toast.LENGTH_SHORT).show();

                ArrayList<Module> moduleArrayList = localStorage.getModuleArrayList();
                ArrayList<Homework> homeworkArrayList = moduleArrayList
                        .get(ModuleUtils.findModule(moduleArrayList, module.getModuleName()))
                        .getHomeworkArrayList();

                HomeworkUtils.replaceHomework(homeworkArrayList, homework, homework.getHomeworkName(), homework.getModuleName());

                firebaseUtils.updateHomeworkArrayList(homeworkArrayList, moduleArrayList, module.getModuleName());
                localStorage.setHomeworkArrayList(homeworkArrayList, module.getModuleName());

                homeworkViewArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

            }
        });

        // Delete homework
        holder.imageButtonDeleteHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage("Are you sure you want to delete " + homework.getHomeworkName() + " homework?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        homeworkViewArrayList.remove(holder.getAdapterPosition());

                        ArrayList<Module> moduleArrayList = localStorage.getModuleArrayList();
                        ArrayList<Homework> homeworkArrayList = moduleArrayList
                                .get(ModuleUtils.findModule(moduleArrayList, module.getModuleName()))
                                .getHomeworkArrayList();

                        HomeworkUtils.removeHomework(homeworkArrayList, homework.getHomeworkName(), homework.getModuleName());

                        firebaseUtils.updateHomeworkArrayList(homeworkArrayList, moduleArrayList, module.getModuleName());
                        localStorage.setHomeworkArrayList(homeworkArrayList, module.getModuleName());

                        Toast.makeText(view.getContext(), "Homework Removed Successfully", Toast.LENGTH_SHORT).show();

                        notifyItemRemoved(holder.getAdapterPosition());

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return homeworkViewArrayList.size();
    }

    public static class HomeworkRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView moduleText, textViewHomeworkName, textViewTimeLeft;

        ImageButton imageButtonDeleteHomework;

        Button buttonMarkDone;

        ConstraintLayout constraintUrgentIndicator;

        public HomeworkRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleText = itemView.findViewById(R.id.homeworkModule);
            textViewHomeworkName = itemView.findViewById(R.id.textViewHomeworkName);
            textViewTimeLeft = itemView.findViewById(R.id.textViewTimeLeft);

            imageButtonDeleteHomework = itemView.findViewById(R.id.imageButtonDeleteHomework);

            buttonMarkDone = itemView.findViewById(R.id.buttonMarkDone);

            constraintUrgentIndicator = itemView.findViewById(R.id.constraintUrgentIndicator);
        }
    }
}
