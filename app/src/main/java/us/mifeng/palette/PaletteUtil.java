package us.mifeng.palette;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;

/**
 * Created by yuan on 2016/8/28.
 * 获取图片背景色
 */
public class PaletteUtil implements Palette.PaletteAsyncListener{

    private static PaletteUtil instance;

    private PatternCallBack patternCallBack;

    public static PaletteUtil getInstance(){
        if(instance==null){
            instance=new PaletteUtil();
        }
        return instance;
    }

    public synchronized void init(Bitmap bitmap,PatternCallBack patternCallBack){
        Palette.from(bitmap).generate(this);
        this.patternCallBack=patternCallBack;
    }

    public synchronized void init(Resources resources, int resourceId,PatternCallBack patternCallBack){
        Bitmap bitmap = BitmapFactory.decodeResource(resources,resourceId);
        Palette.from(bitmap).generate(this);
        this.patternCallBack=patternCallBack;
    }

    @Override
    public synchronized void onGenerated(Palette palette) {
        Palette.Swatch a = palette.getVibrantSwatch();//有活力的
        Palette.Swatch b=palette.getLightVibrantSwatch();//明亮的
        int colorEasy=0;//定义一个颜色
        if(b!=null){
            colorEasy=b.getRgb();//如果明亮的不为空，则复制颜色
        }
        patternCallBack.onCallBack(changedImageViewShape(a.getRgb(),colorEasy)
                ,a.getTitleTextColor());
    }

    /**
     * 创建Drawable对象
     * @param RGBValues
     * @param two
     * @return
     */
    //因为要设置Drawable颜色，所以需要三个颜色值 分别标识startColor centerColor endColor
    private  Drawable changedImageViewShape(int RGBValues, int two){
        if(two==0){
            //获取了融合之后的颜色，并稍稍加深颜色值
            two=colorEasy(RGBValues);
        }else {
            //否则稍稍变淡颜色的值
            two = colorBurn(two);
        }
        //获取渐变的图形形状
        GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.TL_BR
                ,new int[]{RGBValues,two});
        //设置形状的样式
        shape.setShape(GradientDrawable.RECTANGLE);
        //设置渐变方式
        shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        //圆角
        shape.setCornerRadius(8);
        return shape;
    }


    /**
     * 颜色变浅处理
     * @param RGBValues
     * @return
     * 因为颜色是RGB标识，用来3个字节标识，一个字节占8位，
     * 所以，如果取红色，向右移动两个字节（16位）然后&0xff（用来过滤低位元素）即可，
     * 如果去绿色，向右移动一个字节（8位）然后&0xff（用来过滤高位元素）
     * 如果取蓝色直接&0xff即可除去高位值。

     * 经过测试，下面的返回结果一致
     *
        int color = Color.parseColor("#ffff0000");
        int num = color>>16 &0xff;
        int red1 = Color.red(color);
     */
    private  int colorEasy(int RGBValues) {
        int red = RGBValues >> 16 & 0xff;//获取红色

        int green = RGBValues >> 8 & 0xff;//获取绿色
        int blue = RGBValues & 0xff;      //获取蓝色
        if(red==0){
            red=10;
        }
        if(green==0){
            green=10;
        }
        if(blue==0){
            blue=10;
        }
        red = (int) Math.floor(red * (1 + 0.1));
        green = (int) Math.floor(green * (1 + 0.1));
        blue = (int) Math.floor(blue * (1 + 0.1));
        return Color.rgb(red, green, blue);
    }

    /**
     * 颜色加深处理
     * @param RGBValues
     * @return
     */
    private  int colorBurn(int RGBValues) {
        int red = RGBValues >> 16 & 0xff;
        int green = RGBValues >> 8 & 0xff;
        int blue = RGBValues & 0xff;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    public interface PatternCallBack{
        void onCallBack(Drawable drawable, int titleColor);
    }
}
