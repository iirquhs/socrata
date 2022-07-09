package sg.edu.np.mad.socrata;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {
    ArrayList<Module> moduleArrayList;

    public ModuleAdapter(ArrayList<Module> moduleArrayList) {
        this.moduleArrayList = moduleArrayList;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_list_module, parent, false);

        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {

        Module module = (Module) moduleArrayList.toArray()[position];
        String moduleName = module.getModuleName();
        String targetHours = String.valueOf(module.getTargetHoursPerWeek());
        String targetGrade = module.getTargetGrade();

        @ColorInt int color = module.getColor();

        holder.textViewModuleName.setText(moduleName);
        holder.textViewTargetHours.setText("Target hours (per week): " + targetHours + "h");
        holder.textViewGoal.setText("Goal: " + targetGrade);
        holder.textViewModuleName.setBackgroundColor(color);

        LocalStorage localStorage = new LocalStorage((Activity) holder.buttonStudy.getContext());
        FirebaseUtils firebaseUtils = new FirebaseUtils();

        Gson gson = new Gson();
        String moduleString = gson.toJson(module);

        // Redirect to timer activity
        holder.buttonStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TimerActivity.class);
                intent.putExtra("module_name", moduleName);
                view.getContext().startActivity(intent);
            }
        });

        // Redirect to module info activity
        holder.cardViewModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModuleInfoActivity.class);
                intent.putExtra("module_name", moduleName);
                view.getContext().startActivity(intent);
            }
        });

        // Redirect to edit module
        holder.imageButtonEditModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModuleUpdateActivity.class);
                intent.putExtra("module", moduleString);
                view.getContext().startActivity(intent);
            }
        });

        // Delete module
        holder.imageButtonDeleteModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage("Are you sure you want to delete " + moduleName + " module?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Module deleted", Toast.LENGTH_SHORT).show();

                        ModuleUtils.removeModule(moduleArrayList, moduleArrayList.get(holder.getAdapterPosition()));

                        localStorage.setModuleArrayList(moduleArrayList);

                        firebaseUtils.updateModuleArrayList(moduleArrayList);

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

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModuleName, textViewGoal, textViewTargetHours;
        Button buttonStudy;
        CardView cardViewModule;
        ImageButton imageButtonEditModule, imageButtonDeleteModule;

        public ModuleViewHolder(View itemView) {
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
}
