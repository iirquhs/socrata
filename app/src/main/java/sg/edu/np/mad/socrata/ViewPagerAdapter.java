package sg.edu.np.mad.socrata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    //Arrays
    public int[] sliderImage = {
            R.drawable.ic_undraw_process_re_gws7,
            R.drawable.ic_undraw_time_management_re_tk5w,
            R.drawable.ic_undraw_color_palette_re_dwy7,
            R.drawable.ic_undraw_online_calendar_re_wk3t
    };
    public String[] sliderTitle = {
            "Track Progress",
            "Set Study Goals",
            "Customizations",
            "Never be late"
    };
    public String[] sliderDescription = {
            "Create modules and homework to organise your study plan",
            "Keep track of study time using the timer to reach your study goals",
            "Set your modules to your preferred colour, name and goals",
            "Set due dates to homework so that you would never be late for submission"
    };
    Context mContext;

    public ViewPagerAdapter(Context context) {

        this.mContext = context;

    }

    @Override
    public int getCount() {

        return sliderTitle.length;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.activity_slider_layout, container, false);

        ImageView image = layoutScreen.findViewById(R.id.sliderImage);
        TextView title = layoutScreen.findViewById(R.id.sliderTitle);
        TextView description = layoutScreen.findViewById(R.id.sliderDescription);

        image.setImageResource(sliderImage[position]);
        title.setText(sliderTitle[position]);
        description.setText(sliderDescription[position]);

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }
}
