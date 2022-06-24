package sg.edu.np.mad.socrata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Introduction extends AppCompatActivity {

    public ViewPager mSlideViewPager;
    public LinearLayout mDotLayout;

    public TextView[] mDots;

    public  ViewPagerAdapter sliderAdapter;

    Button toLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome_page);

        mSlideViewPager = findViewById(R.id.viewPager2);

        mDotLayout = findViewById(R.id.indicator_layout);

        toLoginButton = findViewById(R.id.buttonGetStarted);



        // when this activity is about to be launch we need to check if its opened before or not

        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(),LoginActivity.class );
            startActivity(mainActivity);
            finish();


        }

        sliderAdapter = new ViewPagerAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        toLoginButton.setOnClickListener(exit);

    }

    public void addDotsIndicator(int position){

        mDots = new TextView[4];
        mDotLayout.removeAllViews();
        for(int i = 0; i < mDots.length; i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.parseColor("#AAAAAA"));
            mDotLayout.addView(mDots[i]);

        }

        if(mDots.length > 0){
            mDots[position].setTextColor(Color.parseColor("#3E30C2"));
        }

    }
    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return ((SharedPreferences) pref).getBoolean("isIntroOpened",false);



    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened",true);
        editor.apply();

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    View.OnClickListener exit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Introduction.this, LoginActivity.class);
            startActivity(intent);
            savePrefsData();
            finish();
        }
    };
}