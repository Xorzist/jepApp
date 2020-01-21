package com.example.jepapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;
import com.example.jepapp.Activities.Admin.AdminPageforViewPager;
import com.example.jepapp.Activities.Login;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

@Metadata(
        mv = {1, 1, 15},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J\u0012\u0010\t\u001a\u00020\u00062\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0003R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000¨\u0006\f"},
        d2 = {"Lcom/example/jepapp/MyFirebaseMessagingService;", "Lcom/google/firebase/messaging/FirebaseMessagingService;", "()V", "ADMIN_CHANNEL_ID", "", "onMessageReceived", "", "p0", "Lcom/google/firebase/messaging/RemoteMessage;", "setupChannels", "notificationManager", "Landroid/app/NotificationManager;", "app_debug"}
)
public final class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String ADMIN_CHANNEL_ID = "admin_channel";

    public void onMessageReceived(RemoteMessage p0) {
        super.onMessageReceived(p0);
        Intent intent = new Intent(this, Login.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);
        if (VERSION.SDK_INT >= 26) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.pdelete);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) ;
        Map<String, String> notifdata = p0.getData();
        Builder notificationBuilder = new Builder(getApplicationContext(), ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.pdelete)
                .setLargeIcon(largeIcon)
                .setContentTitle(notifdata.get("title"))
                .setContentText(notifdata.get("message"))
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        if(VERSION.SDK_INT >= 21) {
            notificationBuilder.setColor(Color.BLACK);
        }
        notificationManager.notify(notificationID,notificationBuilder.build());

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private final void setupChannels(NotificationManager notificationManager) {
        String adminChannelName = "New notification";
        String adminChannelDescription = "Device to device notification";
        NotificationChannel adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID,adminChannelName,NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        notificationManager.createNotificationChannel(adminChannel);
        Log.e("setupChannels: ","Success" );


        }

    }

