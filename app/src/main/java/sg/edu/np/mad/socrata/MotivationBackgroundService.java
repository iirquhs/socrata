package sg.edu.np.mad.socrata;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MotivationBackgroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer timer = new Timer();

        //Use this if you want to execute it once
        timer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {

                String randomQuote = null;
                try {
                    randomQuote = getRandomQuote();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "getString(R.string.channel_name)";
                    String description = "getString(R.string.channel_description)";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
                    channel.setDescription(description);
                    // Register the channel with the system
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Motivational Quote!")
                        .setContentText(randomQuote) //+ " Start studying now!")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                notificationManager.notify(1, builder.build());
                // 21600000ms = 6h
            }
            //21600000ms is 6h
        }, 0, 21600000);

        return super.onStartCommand(intent, flags, startId);
    }

    // Send a get request to the website to get a random quote
    private String getRandomQuote() throws Exception {

        StringBuilder output = new StringBuilder();
        URL url = new URL("https://zenquotes.io/api/random");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                output.append(line);
            }
        }

        String randomQuote = output.toString();

        randomQuote = randomQuote.substring(1, randomQuote.length() - 1);
        Map<String, Object> quoteMap = new Gson().fromJson(
                randomQuote, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );

        return "\"" + quoteMap.get("q") + "\" - " + quoteMap.get("a") + ".";

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
