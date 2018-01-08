package us.mifeng.palette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.TextView;

public class PaletteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        ImageView imageView = (ImageView) findViewById(R.id.imageview);
        final TextView textView = (TextView) findViewById(R.id.tView);

        imageView.setImageResource(R.mipmap.icon_four);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // 提取完毕
            /*// 有活力的颜色
            Palette.Swatch vibrant = palette.getVibrantSwatch();
            // 有活力的暗色
            Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
            // 有活力的亮色
            Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
            // 柔和的颜色
            Palette.Swatch muted = palette.getMutedSwatch();
            // 柔和的暗色
            Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
            // 柔和的亮色
            Palette.Swatch lightMuted = palette.getLightMutedSwatch();*/

                if (palette != null) {
                    //获取有活力的颜色
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    //有活力的亮色
                    Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                    //获取诱惑力的亮色的颜色值
                    int colorEas = 0;
                    if (lightVibrant != null) {
                        colorEas = lightVibrant.getRgb();
                    }
                    //将颜色转换成Drawable对象
                    Drawable drawable = changeImageViewShape(colorEas, vibrant.getRgb());
                    //设置文本颜色
                    textView.setTextColor(vibrant.getTitleTextColor());
                    textView.setBackground(drawable);
                    //textView.setBackgroundDrawable(drawable);
                }

            }
            //设置颜色Drawable,并设置Drawable的渐变色。
            private Drawable changeImageViewShape(int rgb, int colorEas) {
                if (colorEas == 0) {
                    colorEas = colorEasy(rgb);
                } else {
                    colorEas = colorBurn(colorEas);
                }
                //获取渐变图形的形状
                GradientDrawable shape = new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR, new int[]{rgb, colorEas});
                //设置渐变样式
                shape.setShape(GradientDrawable.RECTANGLE);
                //设置渐变的方向
                shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                //设置圆角大小
                shape.setGradientRadius(8);
                return shape;

            }
            //颜色加深
            private int colorBurn(int colorEas) {
                int red = colorEas >> 16 & 0xff;
                int gree = colorEas >> 8 & 0xff;
                int blue = colorEas & 0xff;
                int resultRed = (int) (Math.floor(red * (1 - 0.1)));
                int resultGree = (int) (Math.floor(gree * (1 - 0.1)));
                int resultBlue = (int) (Math.floor(blue * (1 - 0.1)));
                return Color.rgb(resultRed, resultGree, resultBlue);
            }
            //颜色变浅
            private int colorEasy(int rgb) {
                int red = rgb >> 16 & 0xff;
                int gree = rgb >> 8 & 0xff;
                int blue = rgb & 0xff;
                //颜色加深一点
                int resultRed = (int) ((Math.floor(red * (1 + 0.1))));
                int resultgree = (int) ((Math.floor(gree * (1 + 0.1))));
                int resultBlue = (int) ((Math.floor(blue * (1 + 0.1))));
                return Color.rgb(resultRed, resultgree, resultBlue);
            }
        });
    }
}
