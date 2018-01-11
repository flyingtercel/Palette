# Palette
Palette
```
Palette是一个可以从图片(Bitmap)中提取颜色的帮助类，可以使UI更加美观，根据图片动态的显示相应的颜色。现在最新的api是在版本22.0.0添加的
```
### Palette可以提取的颜色：
```
Vibrant （有活力的）
Vibrant dark（有活力的 暗色）
Vibrant light（有活力的 亮色）
Muted （柔和的）
Muted dark（柔和的 暗色）
Muted light（柔和的 亮色）
```
### 使用方法： module的build.gradle中引用
```
compile 'com.android.support:palette-v7:25.3.1'

```
### 使用步骤：
```
1.获取Palette对象，也就是图像调色板
2.获取从图像调色板生成的色样
3.从色样中提取相应颜色
```
##### 1.获取Palette对象，也就是图像调色板
获取Palette对象有同步和异步两种方式，建议使用异步获取Palette对象
```
 // Synchronous
 Palette p = Palette.from(bitmap).generate();

 // Asynchronous
 Palette.from(bitmap).generate(new PaletteAsyncListener() {
     public void onGenerated(Palette p) {
         // Use generated instance
     }
 });
 ```
#####  2.获取从图像调色板生成的色样
可以获取到六种色样，但是有的时候获取不到对应的色样对象，必须注意非空判断。
```
Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力的

Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();//有活力的，暗色

Palette.Swatch vibrantLight = palette.getLightVibrantSwatch();//有活力的，亮色

Palette.Swatch muted = palette.getMutedSwatch();//柔和的

Palette.Swatch mutedDark = palette.getDarkMutedSwatch();//柔和的，暗色

Palette.Swatch mutedLight = palette.getLightMutedSwatch();//柔和的,亮色
```
##### 3.从色样中提取相应颜色
通过 getRgb() 可以得到最终的颜色值并应用到UI中。getBodyTextColor() 和 getTitleTextColor() 可以得到此颜色下文字适合的颜色，这样很方便我们设置文字的颜色，使文字看起来更加舒服。
```
swatch.getPopulation(): 样本中的像素数量
swatch.getRgb(): 颜色的RBG值
swatch.getHsl(): 颜色的HSL值
swatch.getBodyTextColor(): 主体文字的颜色值
swatch.getTitleTextColor(): 标题文字的颜色值
```
###### 完整代码，分装工具类如下
```
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
```
##### 在Activity中的使用
```
//查找控件
        ImageView imageView = (ImageView) findViewById(R.id.imageview);
        final TextView textView = (TextView) findViewById(R.id.tView);
        imageView.setImageResource(R.mipmap.icon_four);
        
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        //调用发发，并获取取色后的Drawable对象
        PaletteUtil.getInstance().init(bitmap, new PaletteUtil.PatternCallBack() {
            @Override
            public void onCallBack(Drawable drawable, int titleColor) {
                //设置背景颜色
                textView.setBackground(drawable);
                //设置文本颜色
                textView.setTextColor(titleColor);
            }
        });
 ```

