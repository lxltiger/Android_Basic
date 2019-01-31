package shine.com.advance.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import shine.com.advance.R;


/**
 * 自定义的标题栏 左右各一个按钮 中间是标题文字
 */
public class TopBar extends RelativeLayout{

    private ImageButton mLeftButton,mRightButton;
    private TextView mTextViewTitle;
    private LayoutParams mLeftParams,mRightParams,mTitleParams;

    private int leftButtonResId,rightButtonResId;
    private String mTitle;
    private float mTitleSize;
    private int mTitleColor;

    private OnTopBarClickListener listener;

    public interface OnTopBarClickListener{
        void leftButtonClick();
        void rightButtonClick();
    }

    public void setOnTopBarClickListener(OnTopBarClickListener listener){
        this.listener=listener;
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.TopBar);
            int n=typedArray.getIndexCount();
        for (int i=0;i<n;i++){
            switch (typedArray.getIndex(i)){
                case R.styleable.TopBar_leftBackground:
                    leftButtonResId=typedArray.getResourceId(R.styleable.TopBar_leftBackground, 0);
                            break;
                case R.styleable.TopBar_rightBackground:
                    rightButtonResId=typedArray.getResourceId(R.styleable.TopBar_rightBackground,0);
                    break;
                case R.styleable.TopBar_titleText:
                    mTitle=typedArray.getString(R.styleable.TopBar_titleText);
                    break;
                case R.styleable.TopBar_titleColor:
                    mTitleColor=typedArray.getColor(R.styleable.TopBar_titleColor,0);
                    break;
                case R.styleable.TopBar_titleSize:
                    /*
                   * getDimension和getDimensionPixelOffset的功能差不多,都是获取某个dimen的值,如果是dp或sp的单位,将其乘以density,
                   * 如果是px,则不乘;两个函数的区别是一个返回float,一个返回int.getDimensionPixelSize则不管写的是dp还是sp还是px,都会乘以denstiy.
                     */
                    mTitleSize=typedArray.getDimension(R.styleable.TopBar_titleSize,0);
                    break;
            }
        }
        typedArray.recycle();
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mLeftButton=new ImageButton(context);
        mLeftParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        mLeftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(mLeftButton, mLeftParams);

        mRightButton=new ImageButton(context);
        mRightParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        mRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mRightParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(mRightButton, mRightParams);

        mTextViewTitle=new TextView(context);
        mTitleParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        mTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mTextViewTitle,mTitleParams);

        mLeftButton.setBackgroundResource(leftButtonResId);
        mRightButton.setBackgroundResource(rightButtonResId);
        mTextViewTitle.setText(mTitle);
        mTextViewTitle.setTextColor(mTitleColor);
        mTextViewTitle.setTextSize(mTitleSize);

        mLeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.leftButtonClick();
                }
            }
        });
        mRightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.rightButtonClick();
                }
            }
        });

    }
}
