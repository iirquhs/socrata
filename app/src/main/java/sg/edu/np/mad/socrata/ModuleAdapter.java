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

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModlueViewHolder> {
    ArrayList<Module> moduleArrayList;

    public class ModlueViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModuleName, textViewGoal, textViewTargetHours;
        Button buttonStudy;
        CardView cardViewModule;
        ImageButton imageButtonEditModule, imageButtonDeleteModule;

        public ModlueViewHolder(View itemView) {
            super(itemView);

            textViewModuleName = itemView.findViewById(R.id.textViewModuleName);
            textViewGoal = itemView.findViewById(R.id.textViewGoal);
            textViewTargetHours = itemView.findViewById(R.id.textViewTargetHours);
            buttonStudy = itemView.findViewById(R.id.buttonStudy);
            cardViewModule = itemView.findViewById(R.id.cardViewModule);
            imageButtonEditModule = itemView.findViewById(R.id.imageButtonEditModule);
            imageButtonDeleteModule = itemView.findViewById(R.id.imageButtonDeleteModule);
        }
    }

    public ModuleAdapter(ArrayList<Module> moduleArrayList) {
        this.moduleArrayList = moduleArrayList;
    }

    @NonNull
    @Override
    public ModlueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_list_module, parent, false);
        ModlueViewHolder modlueViewHolder = new ModlueViewHolder(view);
        return modlueViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ModlueViewHolder holder, int position) {
        Module module = moduleArrayList.get(position);
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
                //Redirect to timer
            }
        });

        holder.cardViewModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirect to view module
            }
        });

        holder.imageButtonEditModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirect to edit module page
            }
        });

        holder.imageButtonDeleteModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage("Are you sure you want to delete " + moduleName + " module?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moduleArrayList.remove(holder.getAdapterPosition());
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
        return moduleArrayList.size();
    }
}
