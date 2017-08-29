package com.ljt.viewframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by 1 on 2017/8/29.
 */

public class DiscrollView extends ScrollView {


    public static String TAG="DiscrollView";
    private DiscrollViewContent mContent;

    public DiscrollView(Context context) {
        super(context);
    }

    public DiscrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupFirstView();
        Log.d(TAG,"onSizeChanged");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG,"onFinishInflate");
        if (getChildCount() != 1) {
            throw new IllegalStateException("Discrollview must host one child.");
        }
        View content = getChildAt(0);
        if (!(content instanceof DiscrollViewContent)) {
            throw new IllegalStateException("Discrollview must host a DiscrollViewContent.");
        }
        mContent = (DiscrollViewContent) content;
        if (mContent.getChildCount() < 2) {
            throw new IllegalStateException("Discrollview must have at least 2 children.");
        }
    }

    private void setupFirstView() {
        View first = mContent.getChildAt(0);
        if(first !=null){
            first.getLayoutParams().height=getHeight();
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.d(TAG,"onScrollChanged(int l, int t, int oldl, int oldt)");
        onScrollChanged(t);
    }
    private int getAbsoluteBottom() {
        View last = getChildAt(getChildCount() - 1);
        if (last == null) {
            return 0;
        }
        return last.getBottom();
    }

    private void onScrollChanged(int top){
        Log.d(TAG,"onScrollChanged(int top)");
        int scrollViewHeight = getHeight();
        int scrollViewBottom = getAbsoluteBottom();
        int scrollViewHalfHeight = scrollViewHeight / 2;

        Log.d(TAG," TTTT　scrollViewHeight="+scrollViewHeight+" scrollViewBottom="
                +scrollViewBottom+" scrollViewHalfHeight="+scrollViewHalfHeight);


        // starts at 1 because the first View is a static non Discrollvable
        // view.
        for (int index = 1; index < mContent.getChildCount(); index++) {
            View child = mContent.getChildAt(index);
            if (!(child instanceof Discrollvable)) {
                // it's a static view, doesn't care about
                continue;
            }
            Discrollvable discrollvable = (Discrollvable) child;
            int discrollvableTop = child.getTop();
            int discrollvableHeight = child.getHeight();
            int discrollvableAbsoluteTop = discrollvableTop - top;

            // the Discrollvable is too big to be discrollved when its center is
            // reached
            // the Discrollvable center. Discrollve it by its top.
            if (scrollViewBottom - child.getBottom() < discrollvableHeight + scrollViewHalfHeight) {
                // the Discrollvable top reaches the DiscrollView bottom
                if (discrollvableAbsoluteTop <= scrollViewHeight) {
                    int visibleGap = scrollViewHeight - discrollvableAbsoluteTop;
                    discrollvable.onDiscrollve(clamp(visibleGap / (float) discrollvableHeight,
                            0.0f, 1.0f));
                } else {
                    discrollvable.onResetDiscrollve();
                }
            } else {
                if (discrollvableAbsoluteTop <= scrollViewHalfHeight) {
                    // the Discrollvable center reaches the DiscrollView center
                    int visibleGap = scrollViewHalfHeight - discrollvableAbsoluteTop;
                    discrollvable.onDiscrollve(clamp(visibleGap / (float) discrollvableHeight,
                            0.0f, 1.0f));
                } else {
                    discrollvable.onResetDiscrollve();
                }
            }
        }
    }

    public static float clamp(float value, float max, float min) {
        Log.d(TAG,"clamp(float value, float max, float min)");
        return Math.max(Math.min(value, min), max);
    }

}
