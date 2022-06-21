package sg.edu.np.mad.socrata;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModlueViewHolder> {
    Map<String, Module> moduleMap;

    public class ModlueViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModuleName, textViewGoal, textViewTargetHours;
        Button buttonStudy;
        CardView cardViewModule;
        ImageButton imageButtonEditModule, imageButtonDeleteModule;

        public ModlueViewHolder(View itemView) {
            super(itemView);

            textViewModuleName = itemView.findViewById(R.id.homework_module);
            textViewGoal = itemView.findViewById(R.id.textViewHomeworkName);
            textViewTargetHours = itemView.findViewById(R.id.textViewTargetHours);
            buttonStudy = itemView.findViewById(R.id.buttonStudy);
            cardViewModule = itemView.findViewById(R.id.cardViewModule);
            imageButtonEditModule = itemView.findViewById(R.id.imageButtonEditModule);
            imageButtonDeleteModule = itemView.findViewById(R.id.imageButtonDeleteModule);
        }
    }

    public ModuleAdapter(Map<String, Module> moduleMap) {
        this.moduleMap = moduleMap;
    }

    @NonNull
    @Override
    public ModlueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_list_module, parent, false);

        return new ModlueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModlueViewHolder holder, int position) {

        Module module = (Module) moduleMap.values().toArray()[position];
        String moduleRef = (String) moduleMap.keySet().toArray()[position];
        String moduleName = module.getModuleName();
        String targetHours = String.valueOf(module.getTargetHoursPerWeek());
        String targetGrade = module.getTargetGrade();

        @ColorInt int color = module.getColor();

        holder.textViewModuleName.setText(moduleName);
        holder.textViewTargetHours.setText("Target hours (per week): " + targetHours + "h");
        holder.textViewGoal.setText("Goal: " + targetGrade);
        holder.textViewModuleName.setBackgroundColor(color);

        holder.buttonStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent intent = new Intent(view.getContext(), TimerActivity.class);
                intent.putExtra("module_name", moduleName);
                view.getContext().startActivity(intent);
            }
        });

        holder.cardViewModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModuleInfo.class);
                Gson gson = new Gson();
                String moduleString = gson.toJson(module);
                intent.putExtra("module", moduleString);
                view.getContext().startActivity(intent);
            }
        });

        holder.imageButtonEditModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModuleUpdate.class);
                Gson gson = new Gson();
                String moduleString = gson.toJson(module);
                intent.putExtra("module", moduleString);
                view.getContext().startActivity(intent);
            }
        });

        holder.imageButtonDeleteModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage("Are you sure you want to delete " + moduleName + " module?");

                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentuser);

                DatabaseReference homeworkRef = userReference.child("homework");

                Query homeworkDeleteQuery = homeworkRef.orderByChild("moduleRef").equalTo(moduleRef);
                homeworkDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                            homeworkSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference moduleRef = userReference.child("modules");

                Query moduleDeleteQuery = moduleRef.orderByChild("moduleName").equalTo(moduleName);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moduleDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("tag", "onCancelled", databaseError.toException());
                            }
                        });

                        moduleMap.values().remove((Module) moduleMap.values().toArray()[holder.getAdapterPosition()]);
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleMap.size();
    }
}
