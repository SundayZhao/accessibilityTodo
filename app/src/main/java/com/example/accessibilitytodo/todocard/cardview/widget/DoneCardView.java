package com.example.accessibilitytodo.todocard.cardview.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.accessibilitytodo.R;
import com.example.accessibilitytodo.todocard.cardview.drawable.BottomDrawable;
import com.example.accessibilitytodo.todocard.cardview.drawable.CenterDrawable;
import com.example.accessibilitytodo.todocard.cardview.drawable.TopDrawable;
import com.example.accessibilitytodo.todocard.cardview.utils.ScreenSizeUtil;

public class DoneCardView extends LinearLayout {
    RelativeLayout topLayout;
    ImageView centerLayout;
    TextView content;
    TextView header;

    TopDrawable topDrawable;
    CenterDrawable centerDrawable;
    BottomDrawable bottomDrawable;
    GradientDrawable myGrad;
    GradientDrawable myHeader;
    float fontSize=20;
    int fontColor=0x000000ff;
    public void setCardFont(float fontSize,int fontColor){
        this.fontColor=fontColor;
        this.fontSize=fontSize;
        if(content!=null){
            content.setTextColor(this.fontColor);
            content.setTextSize(this.fontSize);
            header.setTextColor(this.fontColor);
            header.setTextSize(this.fontSize);
        }
    }

    public DoneCardView(Context context) {
        super(context);
        init(context);
    }

    public DoneCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoneCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DoneCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(final Context context) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.card_done, null);
        layout.setLayoutParams(new LayoutParams(ScreenSizeUtil.Dp2Px(getContext(), 320), LayoutParams.WRAP_CONTENT));
        layout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//没有这句不显示

        topLayout = (RelativeLayout) layout.findViewById(R.id.top);
        centerLayout = (ImageView) layout.findViewById(R.id.center);
        content = (TextView) layout.findViewById(R.id.content);
        header = (TextView) layout.findViewById(R.id.header);
        myGrad = (GradientDrawable) content.getBackground();
        myHeader=(GradientDrawable) header.getBackground();
        topDrawable = new TopDrawable();
        topLayout.setBackground(topDrawable);

        centerDrawable = new CenterDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.quote));
        centerLayout.setBackground(centerDrawable);

        bottomDrawable = new BottomDrawable();

        RelativeLayout bottomLayout = (RelativeLayout) layout.findViewById(R.id.bottomLayout);
        bottomLayout.setBackground(bottomDrawable);
        addView(layout);
    }

    public void changeTheme(final int color) {
        //文字背景颜色
        myGrad.setColor(color);
        myHeader.setColor(color);
        //顶部阴影颜色
        topDrawable.setColor(color);
//        中部阴影颜色
        centerDrawable.setColor(color);
//        //底部阴影颜色
        bottomDrawable.setColor(color);

    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
