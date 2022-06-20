package sg.edu.np.mad.socrata;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeworkRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView moduleText;
    public HomeworkRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        moduleText = itemView.findViewById(R.id.homeworkModule);
    }

    public TextView getView() {
        return moduleText;
    }
}
