package com.example.jepapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.VideoView;

import com.example.jepapp.R;

public class SplashActivity extends Activity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreenlayout);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        },600);





//        videoView = (VideoView) findViewById(R.id.videoView);
//
//
//        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash4b);
//        videoView.setVideoURI(video);
//        videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SplashActivity.this.finish();
//                startActivity(new Intent(SplashActivity.this, Login.class));
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//            }
//        });
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            public void onCompletion(MediaPlayer mp) {
//                startNextActivity();
//            }
//        });
//
//        videoView.start();
//    }
//
//    private void startNextActivity() {
//        startActivity(new Intent(this, Login.class));
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        finish();
//    }
    }

}
