package com.codemobiles.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;


/*
 * Copyright (C) 2009 codemobiles.com.
 * http://www.codemobiles.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use ctx file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * author: Chaiyasit Tayabovorn 
 * email: chaiyasit.t@gmail.com
 * 
 */
public class CMNotification {

    private static NotificationManager mNotificationManager;
    protected static Bitmap remote_picture;


    public static void notify(final Context ctx, final int iconResID, final String remote_image_url, final String title, final String desc, final Class<?> receiver) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    remote_picture = BitmapFactory.decodeStream((InputStream) new URL(remote_image_url).getContent());
                    // remote_picture = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

                        Notification noti = setNormalNotification(ctx, iconResID, remote_picture, title, desc, receiver);
                        // Notification noti = setBigTextStyleNotification(ctx);
                        noti.defaults |= Notification.DEFAULT_LIGHTS;
                        noti.defaults |= Notification.DEFAULT_VIBRATE;
                        noti.defaults |= Notification.DEFAULT_SOUND;
                        noti.defaults |= Notification.FLAG_AUTO_CANCEL;
                        noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

                        final int notificationID = new Random().nextInt();
                        noti.when = System.currentTimeMillis() + 1000 * 60;
                        mNotificationManager.notify(notificationID, noti);
                    }
                });

            }
        }).start();

    }


    public static Notification setNormalNotification(Context ctx, int iconResID, Bitmap remote_picture, String title, String desc, Class<?> receiver) {

        // Setup an explicit intent for an SuccessActivity to receive.
        Intent resultIntent = new Intent(ctx, receiver);

        // TaskStackBuilder ensures that the back button follows the recommended
        // convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        // Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(receiver);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(ctx).setSmallIcon(iconResID).setAutoCancel(true).setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent).setContentTitle(title).setContentText(desc).build();
    }

}
