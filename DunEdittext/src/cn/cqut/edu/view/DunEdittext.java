package cn.cqut.edu.view;

import java.util.regex.Pattern;

import com.example.dunedittext.R;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * @author dun
 * 
 * 使用前复制drawable下的默认正确和错误提示icon
 * 此版本的api leave 最低为12
 * */
public class DunEdittext extends EditText {

	private State mState;

	/**
	 * 是否通过验证，默认为false
	 * */
	private boolean isMatches = false;
	
	/**
	 * 用于保存验证的前一个状态
	 * */
	private boolean beforeMatcher = false;
	
	/**
	 * 错误后edittext是否窗口抖动，默认是false
	 * */
	private boolean isShakeable = false;
	
	/**
	 * 设置是否显示正确或者错误提示，默认是true
	 * */
	private boolean isShowRightOrErrorIcon = true;

	private String EMAIL_CHECK = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	private String PHONENUM_CHECK = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	private String MY_SETTINGPATTERN_CHECK = "";
	private String NUMBER_ONLY_CHECK = "^\\d*";
	private String NUMBER_CHARACTER_ONLY_CHECK = "[0-9a-zA-Z]*";
	private String LOWER_CHAARACTER_ONLY_CHECK = "[a-z]*";
	private String LARGE_CHAARACTER_ONLY_CHECK = "[A-Z]*";
	
	/**
	 * 弱密码，6~18位，包括数字字母下划线
	 * */
	private String WEEK_PASSWORD_CHECK = "^[a-zA-Z]\\w{5,17}$";
	
	/**
	 * 必须包含大写字母，小写字母，和数字才正确
	 * 强密码，8-18位，数字字母下划线和非特殊字符
	 * */
	private String STRONG_PASSWORD_CHECK = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,18}$";
	
	/**
	 * 邮政编码验证
	 * */
	private String CHINAPOST_NUM_CHECK = "[1-9]\\d{5}(?!\\d)";
	
	/**
	 * 腾讯qq号码验证（qq号从10000开始）
	 * */
	private String TENCENT_QQNUM_CHECK = "[1-9][0-9]{4,}";
	
	/**
	 * 中文验证，模拟器上尚未验证是否有效
	 * */
	private String CHINESE_CHECK = "[\u4e00-\u9fa5]";
	
	public static String STATE_EMALI = "STATE_EMALI";
	public static String STATE_NORMAL = "STATE_NORMAL";
	public static String STATE_PHONENUM = "STATE_PHONENUM";
	public static String STATE_MYSETTING = "STATE_MYSETTING";
	public static String STATE_NUM_ONLY = "NUM_ONLY";
	public static String STATE_NUMBER_CHARACTER_ONLY = "NUMBER_CHARACTER_ONLY";
	public static String STATE_LOWER_CHAARACTER_ONLY = "LOWER_CHAARACTER_ONLY";
	public static String STATE_LARGE_CHAARACTER_ONLY = "LARGE_CHAARACTER_ONLY";
	public static String STATE_WEEK_PASSWORD = "WEEK_PASSWORD";
	public static String STATE_STRONG_PASSWORD = "STRONG_PASSWORD";
	public static String STATE_CHINAPOST_NUM = "CHINAPOST_NUM";
	public static String STATE_TENCENT_QQNUM = "TENCENT_QQNUM";
	public static String STATE_CHINESE = "CHINESE";
	 
	
	private Drawable rightIcon = null;
	private Drawable errorIcon = null;
	

	enum State {
		EMAIL, PHONE_NUMBER, NORMAL, MYSETING,NUM_ONLY,NUMBER_CHARACTER_ONLY,
		LOWER_CHAARACTER_ONLY,LARGE_CHAARACTER_ONLY,WEEK_PASSWORD,STRONG_PASSWORD
		,CHINAPOST_NUM,TENCENT_QQNUM,CHINESE
	}

	public DunEdittext(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DunEdittext(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DunEdittext(Context context) {
		super(context);
		init();
	}

	private void init() {
		mState = State.NORMAL;
		rightIcon = getResources().getDrawable(R.drawable.msp_right_small);
		errorIcon = getResources().getDrawable(R.drawable.msp_error_small);
		errorIcon.setBounds(0, 0, (int) this.getTextSize(),
				(int) this.getTextSize());	
		rightIcon.setBounds(0, 0, (int) this.getTextSize(),
				(int) this.getTextSize());
		
	}
	
	
	/**
	 * 设置正确或错误提示icon，可以传null，则使用默认icon
	 * */
	public void setIcon(Drawable right,Drawable error){
		if(right!=null){
			this.rightIcon = right;
		}else if(error!= null){
			this.errorIcon = error;
		}
	}
	
	
	/**
	 * 设置错误后是否抖动窗口
	 * */
	public void setIsShakeable(boolean is) {
		this.isShakeable = is;
	}
	
	
	/**
	 * 设置自定义正则表达式
	 * */
	public void setMyPattern(String s) {
		this.MY_SETTINGPATTERN_CHECK = s;
	}
	
	/**
	 * 设置是否显示通过验证或不通过验证的正则表达式
	 * */
	public void SetIsShowRightOrErrorIcon(boolean is){
		this.isShowRightOrErrorIcon = is;
	}
	
	/**
	 * 设置edittext的验证模式，
	 * 假若验证模式为STATE_MYSETTING，则必须实现setMyPattern(String s);
	 * */
	public void setInputState(String state) {
		if (state.equals(STATE_EMALI)) {
			mState = State.EMAIL;
		} else if (state.equals(STATE_NORMAL)) {
			mState = State.NORMAL;
		} else if (state.equals(STATE_PHONENUM)) {
			mState = State.PHONE_NUMBER;
		} else if (state.equals(STATE_MYSETTING)) {
			mState = State.MYSETING;
		} else if(state.equals(STATE_NUM_ONLY)){
			mState = State.NUM_ONLY;
		}else if(state.equals(STATE_NUMBER_CHARACTER_ONLY)){
			mState = State.NUMBER_CHARACTER_ONLY;
		}else if(state.equals(STATE_LOWER_CHAARACTER_ONLY)){
			mState = State.LOWER_CHAARACTER_ONLY;
		}else if(state.equals(STATE_LARGE_CHAARACTER_ONLY)){
			mState = State.LARGE_CHAARACTER_ONLY;
		}else if(state.equals(STATE_STRONG_PASSWORD)){
			mState = State.STRONG_PASSWORD;
		}else if(state.equals(STATE_WEEK_PASSWORD)){
			mState = State.WEEK_PASSWORD;
		}else if(state.equals(STATE_CHINAPOST_NUM)){
			mState = State.CHINAPOST_NUM;
		}else if(state.equals(STATE_TENCENT_QQNUM)){
			mState = State.TENCENT_QQNUM;
		}else if(state.equals(STATE_CHINESE)){
			mState = State.CHINESE;
		}

		else {
			Log.e("ERROR", "不存在这个状态---->  " + state);
		}
	}
	
	
	
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		if (!focused) {
			if (!isMatches) {
				doSomtThingAfterCheck(100, true);
			}else{
				setdrawable(null,null,null,null);
			}
		}

	}


	
	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);

		if (!this.getText().toString().equals("")) {

			beforeMatcher = isMatches;

			isMatches = checkPattern(text.toString());

			if (beforeMatcher != isMatches) {
				doSomtThingAfterCheck(100, false);
			}
		}
	}
	
	
	/**
	 * 
	 * 正则表达式的检验
	 * */
	private boolean checkPattern(String s) {
		Pattern p = null;

		if (mState == State.EMAIL) {
			p = Pattern.compile(EMAIL_CHECK);
		} else if (mState == State.PHONE_NUMBER) {
			p = Pattern.compile(PHONENUM_CHECK);
		} else if (mState == State.MYSETING) {
			p = Pattern.compile(MY_SETTINGPATTERN_CHECK);
		} else if(mState == State.NUM_ONLY){
			p = Pattern.compile(NUMBER_ONLY_CHECK);
		} else if(mState == State.NUMBER_CHARACTER_ONLY){
			p = Pattern.compile(NUMBER_CHARACTER_ONLY_CHECK);
		} else if (mState == State.LOWER_CHAARACTER_ONLY){
			p = Pattern.compile(LOWER_CHAARACTER_ONLY_CHECK);
		} else if (mState == State.LARGE_CHAARACTER_ONLY){
			p = Pattern.compile(LARGE_CHAARACTER_ONLY_CHECK);
		} else if(mState == State.WEEK_PASSWORD){
			p = Pattern.compile(WEEK_PASSWORD_CHECK);
		} else if(mState == State.STRONG_PASSWORD){
			p = Pattern.compile(STRONG_PASSWORD_CHECK);
		} else if(mState == State.CHINAPOST_NUM){
			p = Pattern.compile(CHINAPOST_NUM_CHECK);
		} else if(mState == State.TENCENT_QQNUM){
			p = Pattern.compile(TENCENT_QQNUM_CHECK);
		} else if(mState == State.CHINESE){
			p = Pattern.compile(CHINESE_CHECK);
		}

		if (p != null)
			isMatches = p.matcher(s).matches();

		return isMatches;
	}
	
	
	/**
	 * 显示提示icon，edittext的抖动的实现
	 * */
	private void doSomtThingAfterCheck(int alpha, boolean isFromfocuse) {
		if (isShowRightOrErrorIcon)
			if (isMatches) {
				addDrawableAnimation(rightIcon);

			} else if (!isMatches) {
				addDrawableAnimation(errorIcon);
				if(isFromfocuse)
				shakeTheEditText();
			} else {
				this.setCompoundDrawables(null, null, null, null);
			}
	}
	
	
	/**
	 * 
	 * */
	private void addDrawableAnimation(final Drawable d) {

		ValueAnimator va = ValueAnimator.ofInt(0, 200);
		va.setDuration(200);

		va.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				d.setAlpha((Integer) animator.getAnimatedValue());
				setdrawable(null, null, d, null);
			}
		});

		va.start();
	}

	private void setdrawable(Drawable left, Drawable top, Drawable right,
			Drawable bottom) {
		this.setCompoundDrawables(left, top, right, bottom);
	}

	private void shakeTheEditText() {
		if (!isShakeable) {
			return;
		}

		ValueAnimator va = ValueAnimator.ofFloat(0f, 2f);
		va.setDuration(500);
		va.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				float offset = (float) (Math.sin(Math.PI
						* (Float) animator.getAnimatedValue()) * 10);
				DunEdittext.this.setTranslationX(offset);

			}
		});

		va.start();
	}


	
	/**
	 * 返回是否通过验证
	 * */
	public boolean getIsMatches() {
		return isMatches;
	}
	


	
}
