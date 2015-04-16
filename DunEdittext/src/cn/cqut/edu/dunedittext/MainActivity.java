package cn.cqut.edu.dunedittext;

import cn.cqut.edu.view.DunEdittext;

import com.example.dunedittext.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	DunEdittext ed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		ed = (DunEdittext) findViewById(R.id.dunEdittext1);
		ed.setInputState(DunEdittext.STATE_CHINESE);
		ed.setIsShakeable(true);
//		ed.SetIsShowRightOrErrorIcon(false);
		
	}


}
