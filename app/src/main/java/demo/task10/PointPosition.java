package demo.task10;

/**
 * Created by Ha on 2016/3/23.
 */
public class PointPosition {
    public int mNumber;
    public int[] mPositionX;
    public int[] mPositionY;
    public int[] mDegree;

    public PointPosition(int number, int[] positionX, int[] positionY, int[] degree) {
        mNumber = number;
        mPositionX = positionX;
        mPositionY = positionY;
        mDegree = degree;
    }
    public int getPositionX(int number) {
        return mPositionX[number];
    }

    public void setPositionX(int number, int valueChanged) {
        mPositionX[number] += valueChanged;
    }

    public int getPositionY(int number) {
        return mPositionY[number];
    }

    public void setPositionY(int number, int valueChanged) {
        mPositionY[number] += valueChanged;
    }

    public int getDegree(int number) {
        return mDegree[number];
    }

    public void setDegree(int number, int valueChanged) {
        mDegree[number] += valueChanged;
    }
}
