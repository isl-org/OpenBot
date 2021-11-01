package org.openbot.original;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.openbot.R;
import org.openbot.env.SharedPreferencesManager;

public class PlayActivity extends DefaultActivity {
    private LinearLayout bottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    protected ImageView bottomSheetArrowImageView;
    private LinearLayout gestureLayout;
    private SharedPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        bottomSheetArrowImageView = findViewById(R.id.bottom_sheet_arrow);
        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        gestureLayout = findViewById(R.id.gesture_layout);
        preferencesManager = new SharedPreferencesManager(this);

        ViewTreeObserver vto = gestureLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gestureLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // int width = bottomSheetLayout.getMeasuredWidth();
                int height = gestureLayout.getMeasuredHeight();

                sheetBehavior.setPeekHeight(height);
            }
        });
        sheetBehavior.setHideable(false);

        sheetBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_HIDDEN:
                                break;
                            case BottomSheetBehavior.STATE_EXPANDED:
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_down);
                                preferencesManager.setSheetExpanded(true);
                                break;
                            case BottomSheetBehavior.STATE_COLLAPSED:
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                                preferencesManager.setSheetExpanded(false);
                                break;
                            case BottomSheetBehavior.STATE_DRAGGING:
                                break;
                            case BottomSheetBehavior.STATE_SETTLING:
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
                });
    }
}