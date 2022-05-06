package anthony.brenon.go4lunch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import anthony.brenon.go4lunch.R;

/**
 * Created by Lycast on 28/04/2022.
 */
public class ReminderNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        List<String> workmates = intent.getStringArrayListExtra("workmates_list");
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the workmates who will eat with you : ");
        sb.append("\n");
        for (String workmateName : workmates) {
            sb.append(workmateName);
            sb.append("\n");
        }
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "NotifyChan")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(intent.getStringExtra("place_name"))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(intent.getStringExtra("place_address")
                                + "\n\n"
                                + sb ))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = context.getString(R.string.notification_channel_id);
            String channelTitle = context.getString(R.string.notification_channel_title);
            String channelDescription = context.getString(R.string.notification_channel_description);
            NotificationChannel channel = new NotificationChannel(channelId, channelTitle, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(200, builder.build());
    }
}
