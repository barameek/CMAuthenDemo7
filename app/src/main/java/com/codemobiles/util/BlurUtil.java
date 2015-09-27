package com.codemobiles.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.npi.blureffect.Blur;
import com.npi.blureffect.ImageUtils;

import java.io.File;

/**
 * Created by maboho_retina on 3/7/15 AD.
 */
public class BlurUtil {


    private static int blureImageResID;
    private static View mHeaderView;
    private static ImageView mBlurredImage;
    private static ImageView mNormalImage;
    private static File blurredImage;
    private static int screenWidth;
    private static float alpha;
    private static final int TOP_HEIGHT = 700;


    private static void updateView(final int screenWidth, Activity activity) {
        try {
            Bitmap bmpBlurred = BitmapFactory.decodeFile(CMAssetBundle.getAppPackagePath(activity) + "BlurImage");
            bmpBlurred = Bitmap.createScaledBitmap(bmpBlurred, screenWidth, (int) (bmpBlurred.getHeight()
                    * ((float) screenWidth) / (float) bmpBlurred.getWidth()), false);

            mBlurredImage.setImageBitmap(bmpBlurred);
        }catch (Exception e){

        }
    }

    public static void setupBlurEffect(Activity activity, ListView listview, int _blureImageResID, View _headerView, ImageView _normalImageView, ImageView _blurredImageView) {

        mNormalImage = _normalImageView;
        mBlurredImage = _blurredImageView;
        mHeaderView = _headerView;
        blureImageResID = _blureImageResID;

        screenWidth = ImageUtils.getScreenWidth(activity);
        mBlurredImage.setAlpha(alpha);

        CMAssetBundle.getAppPackagePath(activity);

        blurredImage = new File(CMAssetBundle.getAppPackagePath(activity) + "BlurImage");
        if (!blurredImage.exists()) {
            createBlurImage(listview, activity);
        } else {
            updateView(screenWidth, activity);
        }


        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            /**
             * Listen to the list scroll. This is where magic happens ;)
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // Calculate the ratio between the scroll amount and the list
                // header weight to determinate the top picture alpha
                alpha = (float) -mHeaderView.getTop() / (float) TOP_HEIGHT;
                // Apply a ceil
                if (alpha > 1) {
                    alpha = 1;
                }


                mBlurredImage.setAlpha(alpha);


                // Parallax effect : we apply half the scroll amount to our
                // three views
                mBlurredImage.setTop(mHeaderView.getTop() / 2);
                mNormalImage.setTop(mHeaderView.getTop() / 2);


            }
        });
    }


    private static void createBlurImage(final ListView listview, final Activity activity) {
        // launch the progressbar in ActionBar
        activity.setProgressBarIndeterminateVisibility(true);

        new Thread(new Runnable() {

            @Override
            public void run() {

                // No image found => let's generate it!
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap image = BitmapFactory.decodeResource(listview.getResources(),blureImageResID, options);
                Bitmap newImg = Blur.fastblur(activity, image, 12);
                ImageUtils.storeImage(newImg, blurredImage);
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        updateView(screenWidth,activity );

                        // And finally stop the progressbar
                        activity.setProgressBarIndeterminateVisibility(false);
                    }
                });

            }
        }).start();
    }
}
