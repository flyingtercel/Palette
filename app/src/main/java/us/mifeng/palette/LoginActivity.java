package us.mifeng.palette;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void click(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.btn1:
                intent = new Intent(LoginActivity.this,PaletteActivity.class);
                break;
            case R.id.btn2:
                intent = new Intent(LoginActivity.this,MainActivity.class);
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
    }
}
