package br.com.arndroid.etdiet.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.dialog.DateDialog;
import br.com.arndroid.etdiet.utils.DateUtils;

public class ForecastMeterView extends View {

    private final Rect mForegroundBarRect = new Rect();
    private final Rect mBackgroundBarRect = new Rect();
    private int mCurrentWidth;
    private int mCurrentHeight;

    private float mPercentage;
    private int mForecastType;
    private int[] mForegroundColorsArray;
    private Paint mForegroundPaint;
    private Paint mBackgroundPaint;

    public ForecastMeterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ForecastMeterView, 0, 0);

        try {
            mPercentage = attrsArray.getFloat(R.styleable.ForecastMeterView_percentage, 0.0f);
            mForecastType = attrsArray.getInteger(R.styleable.ForecastMeterView_forecastType, 0);
        } finally {
            attrsArray.recycle();
        }

        initGraphics();
    }

    private void initGraphics() {
        final Resources resources = getResources();

        mForegroundColorsArray = new int[] {
                resources.getColor(R.color.forecast_straight_to_goal),
                resources.getColor(R.color.forecast_going_to_goal_with_help),
                resources.getColor(R.color.forecast_out_of_goal_but_can_return),
                resources.getColor(R.color.forecast_out_of_goal)};

        mForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mForegroundPaint.setStyle(Paint.Style.FILL);
        mForegroundPaint.setColor(mForegroundColorsArray[mForecastType]);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(resources.getColor(R.color.forecast_background));
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {

        mCurrentWidth = width;
        mCurrentHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int DEFAULT_BAR_WIDTH_WITHOUT_PADDING_IN_ACCOUNT = 60;
        int DEFAULT_BAR_HEIGHT_WITHOUT_PADDING_IN_ACCOUNT = 260;

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int desiredWidth = DEFAULT_BAR_WIDTH_WITHOUT_PADDING_IN_ACCOUNT + getPaddingLeft() + getPaddingRight();
        final int desiredHeight = DEFAULT_BAR_HEIGHT_WITHOUT_PADDING_IN_ACCOUNT + getPaddingTop() + getPaddingBottom();

        // Measure width:
        int resultWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            resultWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            resultWidth = Math.min(desiredWidth, widthSize);
        } else {
            resultWidth = desiredWidth;
        }

        // Measure Height
        int resultHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            resultHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            resultHeight = Math.min(desiredHeight, heightSize);
        } else {
            resultHeight = desiredHeight;
        }

        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mForegroundPaint.setColor(mForegroundColorsArray[mForecastType]);

        // Calculate 'virtual' background rect:
        mBackgroundBarRect.set(
                getPaddingLeft(),
                getPaddingTop(),
                mCurrentWidth - getPaddingRight(),
                mCurrentHeight - getPaddingBottom()
        );

        // Calculate foreground rect:
        mForegroundBarRect.set(
                mBackgroundBarRect.left,
                (int) (mBackgroundBarRect.bottom * (1.0f - mPercentage)),
                mBackgroundBarRect.right,
                mBackgroundBarRect.bottom
        );

        // Calculate offset:
        mBackgroundBarRect.bottom = mForegroundBarRect.top;
        int FOREGROUND_BAR_OFFSET_BY_SIDE = 3;
        mBackgroundBarRect.left += FOREGROUND_BAR_OFFSET_BY_SIDE;
        mBackgroundBarRect.right -= FOREGROUND_BAR_OFFSET_BY_SIDE;

        canvas.drawRect(mBackgroundBarRect, mBackgroundPaint);
        canvas.drawRect(mForegroundBarRect, mForegroundPaint);
    }

    @SuppressWarnings("UnusedDeclaration")
    public float getPercentage() {
        return mPercentage;
    }

    public void setPercentage(float percentage) {
        mPercentage = percentage;
        invalidate();
    }

    @SuppressWarnings("UnusedDeclaration")
    public int getForecastType() {
        return mForecastType;
    }

    public void setForecastType(int forecastType) {
        mForecastType = forecastType;
        invalidate();
    }
}
