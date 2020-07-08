package com.example.instagrame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ImageView iconView;
private LinearLayout linearLayout;
private Button login;
private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        instWelcome();registration();login();

    }

    @Override
    protected void onStart() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.
                    this, MainFrame.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
        super.onStart();
    }

    public void registration()
    {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity
                        (new Intent(MainActivity.
                                this,Registration.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }
    public void login()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this
                ,Login.class).addFlags
                        (Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }
    public void instWelcome()
    {
        iconView=findViewById(R.id.image_icon);
        linearLayout=findViewById(R.id.linear);

        linearLayout.animate().alpha(0f).setDuration(1);
        TranslateAnimation animation=new
                TranslateAnimation
                (0,0,0,-1000);
        animation.setDuration(1500);
        animation.setFillAfter(true);
        animation.setAnimationListener(new MainActivity.Animation());
        iconView.setAnimation(animation);
    }
    private class Animation implements AnimationListener
    {

        @Override
        public void onAnimationStart(android.view.animation.Animation animation) {

        }

        @Override
        public void onAnimationEnd(android.view.animation.Animation animation) {
            iconView.clearAnimation();
            iconView.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1500);

        }

        @Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {

        }
    }
}
