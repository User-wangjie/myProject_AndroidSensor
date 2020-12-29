package demo.task10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartView extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    Button exit;
    private static final String TAG = MainActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView mTextView;
    private float x = 0f;
    private float y = 0f;
    private Game mGame;

    /**
     * 1加速度
     * 5亮度
     * 8距离
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_view);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.text_view);
        mGame = (Game) findViewById(R.id.game);
        exit=(Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                if (mGame != null) {

                    if (!mGame.isPlaying) {
                        mGame.mNumber = 1;
                        mGame.isPlaying = true;
                        mGame.failure = false;
                        mGame.mTime = 0;
                        mGame.fresh();
                        mGame.mSystemTime = System.currentTimeMillis();
                        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
                    }
                }
                break;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mGame != null && !mGame.failure) {
            x -= event.values[0] / 15;
            y += event.values[1] / 15;
            if (mGame.mPlayerX + x >= mGame.mWidth) {
                mGame.mPlayerX = mGame.mWidth;
            } else if (mGame.mPlayerX + x <= 0) {
                mGame.mPlayerX = 0;
            } else {
                mGame.mPlayerX += x;
            }
            if (mGame.mPlayerY + y >= mGame.mHeight) {
                mGame.mPlayerY = mGame.mHeight;
            } else if (mGame.mPlayerY + y <= 0) {
                mGame.mPlayerY = 0;
            } else {
                mGame.mPlayerY += y;
            }
            String s = String.valueOf(mGame.mTime / 1000);
            mTextView.setText(s);
        } else if (mGame != null && mGame.isPlaying) {
            Toast.makeText(StartView.this, "你失败了,共坚持了" + mGame.mTime / 1000 + "秒", Toast.LENGTH_SHORT).show();
            if (mGame != null) {
                mGame.isPlaying = false;
                mGame.fresh();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
