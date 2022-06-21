package sg.edu.np.mad.socrata;

import android.content.DialogInterface;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkRecyclerViewHolder> {
    ArrayList<Homework> homeworkArrayList;

    Map<String, Module> moduleMap;

    public class HomeworkRecyclerViewHolder extends RecyclerView.ViewHolder {

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

    public HomeworkAdapter(ArrayList<Homework> homeworkArrayList, Map<String, Module> moduleMap) {
        this.homeworkArrayList = homeworkArrayList;
        this.moduleMap = moduleMap;
    }
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
        Homework homework = homeworkArrayList.get(position);

        String moduleRef = homework.getModuleRef();

        Module module =  moduleMap.get(moduleRef);

        holder.moduleText.setBackgroundColor(module.getColor());
        holder.moduleText.setText(module.getModuleName());
        holder.textViewHomeworkName.setText(homework.getHomeworkName());

        LocalDateTime dueDateTime = homework.ConvertDueDateTime(homework.getDueDateTimeString());

        Duration duration = Duration.between(LocalDateTime.now(), dueDateTime);
        Long second = duration.getSeconds();

        holder.textViewTimeLeft.setText(String.format("%.2f h", second.floatValue() / 3600.0));

        // 7 Days
        if (second > 604800) {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), R.color.homework_green_color));
        // 1 Day
        } else if (second > 86400) {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), R.color.homework_yellow_color));
        } else {
            holder.constraintUrgentIndicator.setBackgroundTintList(ContextCompat.getColorStateList(holder.constraintUrgentIndicator.getContext(), R.color.homework_red_color));
        }

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference homeworkRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUser).child("homework");

        holder.buttonMarkDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homework.setStatus("Completed");

                Query homeworkQuery = homeworkRef.orderByChild("moduleRef").equalTo(moduleRef);

                homeworkQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                            Homework newHomework = homeworkSnapshot.getValue(Homework.class);

                            assert newHomework != null;
                            if (newHomework.getHomeworkName().equals(homework.getHomeworkName())) {
                                homeworkSnapshot.getRef().child("status").setValue("Done");
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(view.getContext(), homework.getHomeworkName() + " Mark as Done", Toast.LENGTH_SHORT).show();

                homeworkArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        holder.imageButtonDeleteHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage("Are you sure you want to delete " + homework.getHomeworkName() + " homework?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Query homeworkQuery = homeworkRef.orderByChild("moduleRef").equalTo(moduleRef);

                        homeworkQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                                    Homework newHomework = homeworkSnapshot.getValue(Homework.class);

                                    assert newHomework != null;
                                    if (newHomework.getHomeworkName().equals(homework.getHomeworkName())) {
                                        homeworkSnapshot.getRef().removeValue();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(view.getContext(), "Homework Removed Successfully", Toast.LENGTH_SHORT).show();

                        homeworkArrayList.remove(holder.getAdapterPosition());
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
        return homeworkArrayList.size();
    }
}
