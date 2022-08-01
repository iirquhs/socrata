package sg.edu.np.mad.socrata;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MotivationBackgroundReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        new GetRandomQuote().execute();
    }

    private class GetRandomQuote extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL("https://zenquotes.io/api/random");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        output.append(line);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            String randomQuote = output.toString();

            randomQuote = randomQuote.substring(1, randomQuote.length() - 1);
            Map<String, Object> quoteMap = new Gson().fromJson(
                    randomQuote, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );

            return "\"" + quoteMap.get("q") + "\" - " + quoteMap.get("a") + ".";
        }

        @Override
        protected void onPostExecute(String randomQuote) {
            super.onPostExecute(randomQuote);

            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            } else {
                pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "SocrataChannel")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Motivational Quote!")
                    .setContentText(randomQuote)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(2, builder.build());
        }
    }
}
