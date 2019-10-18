package listener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private onClickListener mOnClickListener;

    public interface onClickListener {
        /**
         * On click recycler view perform description action
         * @param view
         * @param position
         */
        void onClick(View view, int position);
    }

    public RecyclerListener(Context context, onClickListener pOnClickListener) {
        mOnClickListener = pOnClickListener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        View view = rv.findChildViewUnder(e.getX(), e.getY());
        if (view != null && mOnClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mOnClickListener.onClick(view, rv.getChildAdapterPosition(view));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
