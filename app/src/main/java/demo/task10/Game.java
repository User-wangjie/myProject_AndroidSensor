package demo.task10;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class Game extends ImageView {
    private static final String TAG = Game.class.getSimpleName();
    public static final int SPEED_DISTANCE = 8;
    public static final int DELAY_MILLIS = 30;
    public static final int DISTANCE = 20;
    private Paint mPaint;
    public int mWidth;
    public int mHeight;
    public int mNumber = 1;
    public int[] mPositionX;
    public int[] mPositionY;
    public int[] mDegree;
    public List<PointPosition> mPointPositions;
    public boolean isPlaying;
    public int mPlayerX;
    public int mPlayerY;
    public boolean failure = false;
    public long mTime;
    public long mSystemTime;

    public Game(Context context) {
        this(context, null);
    }

    public Game(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Game(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPositionX = new int[100];
        mPositionY = new int[100];
        mDegree = new int[100];

        for (int i = 0; i < mNumber; i++) {
            mPositionX[i] = new Random().nextInt(mWidth);
            mPositionY[i] = new Random().nextInt(mHeight);
            mDegree[i] = new Random().nextInt(360);
        }
        mPlayerX = new Random().nextInt(mWidth);
        mPlayerY = new Random().nextInt(mHeight);

        mPointPositions = new ArrayList<>();
        mPointPositions.add(new PointPosition(100, mPositionX, mPositionY, mDegree));

        mPaint = new Paint();
        fresh();
    }

    public void fresh() {
        if (isPlaying) {
            Fresh fresh = new Fresh(this);
            Message message = fresh.obtainMessage(123);
            fresh.sendMessageDelayed(message, DELAY_MILLIS);
            isFailure();
        }

    }

    private void isFailure() {
        for (int i = 0; i < mNumber; i++) {
            double distance = Math.sqrt(Math.pow(mPlayerX - mPointPositions.get(0).mPositionX[i], 2) + Math.pow(mPlayerY - mPointPositions.get(0).mPositionY[i], 2));
            if (distance < DISTANCE) {
                failure = true;
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (!failure) {
            mPaint.setColor(Color.WHITE);

            for (int i = 0; i < mNumber; i++) {
                int x = mPointPositions.get(0).getPositionX(i);
                int y = mPointPositions.get(0).getPositionY(i);
                canvas.drawCircle(x, y, 10, mPaint);
            }

            mPaint.setColor(Color.RED);
            canvas.drawCircle(mPlayerX, mPlayerY, 20, mPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() - 10;
        mHeight = getMeasuredHeight() - 100;
        init();
    }


    public class Fresh extends Handler {
        public final WeakReference<Game> mGameWeakReference;

        public Fresh(Game game) {
            mGameWeakReference = new WeakReference<>(game);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 123:
                    mTime += System.currentTimeMillis() - mSystemTime;
                    mSystemTime = System.currentTimeMillis();
                    if (mTime / 1000 > mNumber * 3) {
                        mNumber++;
                        mPointPositions.get(0).mPositionX[mNumber - 1] = new Random().nextInt(mWidth);
                        mPointPositions.get(0).mPositionY[mNumber - 1] = new Random().nextInt(mHeight);
                        mPointPositions.get(0).mDegree[mNumber - 1] = new Random().nextInt(360);
                        double distance = Math.sqrt(Math.pow(mPlayerX - mPointPositions.get(0).mPositionX[mNumber - 1], 2) + Math.pow(mPlayerY - mPointPositions.get(0).mPositionY[mNumber - 1], 2));
                        while (distance < DISTANCE) {
                            mPointPositions.get(0).mPositionX[mNumber - 1] = new Random().nextInt(mWidth);
                            mPointPositions.get(0).mPositionY[mNumber - 1] = new Random().nextInt(mHeight);
                            distance = Math.sqrt(Math.pow(mPlayerX - mPointPositions.get(0).mPositionX[mNumber - 1], 2) + Math.pow(mPlayerY - mPointPositions.get(0).mPositionY[mNumber - 1], 2));
                        }
                    }

                    for (int i = 0; i < mNumber; i++) {
                        int x = (int) (SPEED_DISTANCE * Math.cos(mPointPositions.get(0).getDegree(i)));
                        int y = (int) (SPEED_DISTANCE * Math.sin(mPointPositions.get(0).getDegree(i)));

                        if (mPointPositions.get(0).mPositionX[i] >= mWidth) {
                            mPointPositions.get(0).setPositionX(i, -10);
                            mPointPositions.get(0).setDegree(i, 180);
                        } else if (mPointPositions.get(0).mPositionX[i] <= 0) {
                            mPointPositions.get(0).setPositionX(i, 10);
                            mPointPositions.get(0).setDegree(i, 180);
                        } else {
                            mPointPositions.get(0).setPositionX(i, x);
                        }

                        if (mPointPositions.get(0).mPositionY[i] >= mHeight) {
                            mPointPositions.get(0).setPositionY(i, -10);
                            mPointPositions.get(0).setDegree(i, 180);
                        } else if (mPointPositions.get(0).mPositionY[i] <= 0) {
                            mPointPositions.get(0).setPositionY(i, 10);
                            mPointPositions.get(0).setDegree(i, 180);
                        } else {
                            mPointPositions.get(0).setPositionY(i, y);
                        }
                    }
                    invalidate();
                    fresh();
                    break;
            }
        }
    }
}
