package sg.edu.np.mad.socrata;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModlueViewHolder> {
    ArrayList<Module> moduleArrayList;

    public class ModlueViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModuleName, textViewGoal, textViewTargetHours;
        Button buttonStudy;

        public ModlueViewHolder(View itemView) {
            super(itemView);

            textViewModuleName = itemView.findViewById(R.id.textViewModuleName);
            textViewGoal = itemView.findViewById(R.id.textViewGoal);
            textViewTargetHours = itemView.findViewById(R.id.textViewTargetHours);
            buttonStudy = itemView.findViewById(R.id.buttonStudy);

            buttonStudy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Redirect to the specific module
                }
            });
        }
    }

    @NonNull
    @Override
    public ModlueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ModlueViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
