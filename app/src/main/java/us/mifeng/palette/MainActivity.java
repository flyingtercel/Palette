package us.mifeng.palette;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_con;
    private TextView tv_palette;
    private int index =0;
    private int [] imageIds=new int[]{R.mipmap.icon_one,R.mipmap.icon_two,R.mipmap.icon_three, R.mipmap.icon_three,R.mipmap.icon_four,R.mipmap.icon_five,R.mipmap.icon_six,R.mipmap.icon_seven,R.mipmap.icon_enght,R.mipmap.icon_nine,R.mipmap.icon_ten,R.mipmap.icon_ten1,R.mipmap.icon_ten2,R.mipmap.icon_ten3,R.mipmap.icon_ten4,R.mipmap.icon_ten5,R.mipmap.icon_ten6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //查找控件
        iv_con = (ImageView) findViewById(R.id.iv_con);
        tv_palette = (TextView) findViewById(R.id.tv_palette);
        tv_palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if (index == imageIds.length){
                    index =0;
                }
                initPalette(index);
            }
        });
        initPalette(index);
    }
    private void initPalette(int index) {
        iv_con.setImageResource(imageIds[index]);
        PaletteUtil.getInstance()
                .init(getResources(), imageIds[index],
                        new PaletteUtil.PatternCallBack() {
                    @Override
                    public void onCallBack(Drawable drawable, int titleColor) {
                        tv_palette.setTextColor(titleColor);
                        tv_palette.setBackgroundDrawable(drawable);
                    }
                });
    }
}
