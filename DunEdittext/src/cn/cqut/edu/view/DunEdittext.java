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
 * ʹ��ǰ����drawable�µ�Ĭ����ȷ�ʹ�����ʾicon
 * �˰汾��api leave ���Ϊ12
 * */
public class DunEdittext extends EditText {

	private State mState;

	/**
	 * �Ƿ�ͨ����֤��Ĭ��Ϊfalse
	 * */
	private boolean isMatches = false;
	
	/**
	 * ���ڱ�����֤��ǰһ��״̬
	 * */
	private boolean beforeMatcher = false;
	
	/**
	 * �����edittext�Ƿ񴰿ڶ�����Ĭ����false
	 * */
	private boolean isShakeable = false;
	
	/**
	 * �����Ƿ���ʾ��ȷ���ߴ�����ʾ��Ĭ����true
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
	 * �����룬6~18λ������������ĸ�»���
	 * */
	private String WEEK_PASSWORD_CHECK = "^[a-zA-Z]\\w{5,17}$";
	
	/**
	 * ���������д��ĸ��Сд��ĸ�������ֲ���ȷ
	 * ǿ���룬8-18λ��������ĸ�»��ߺͷ������ַ�
	 * */
	private String STRONG_PASSWORD_CHECK = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,18}$";
	
	/**
	 * ����������֤
	 * */
	private String CHINAPOST_NUM_CHECK = "[1-9]\\d{5}(?!\\d)";
	
	/**
	 * ��Ѷqq������֤��qq�Ŵ�10000��ʼ��
	 * */
	private String TENCENT_QQNUM_CHECK = "[1-9][0-9]{4,}";
	
	/**
	 * ������֤��ģ��������δ��֤�Ƿ���Ч
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
	 * ������ȷ�������ʾicon�����Դ�null����ʹ��Ĭ��icon
	 * */
	public void setIcon(Drawable right,Drawable error){
		if(right!=null){
			this.rightIcon = right;
		}else if(error!= null){
			this.errorIcon = error;
		}
	}
	
	
	/**
	 * ���ô�����Ƿ񶶶�����
	 * */
	public void setIsShakeable(boolean is) {
		this.isShakeable = is;
	}
	
	
	/**
	 * �����Զ���������ʽ
	 * */
	public void setMyPattern(String s) {
		this.MY_SETTINGPATTERN_CHECK = s;
	}
	
	/**
	 * �����Ƿ���ʾͨ����֤��ͨ����֤��������ʽ
	 * */
	public void SetIsShowRightOrErrorIcon(boolean is){
		this.isShowRightOrErrorIcon = is;
	}
	
	/**
	 * ����edittext����֤ģʽ��
	 * ������֤ģʽΪSTATE_MYSETTING�������ʵ��setMyPattern(String s);
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
			Log.e("ERROR", "���������״̬---->  " + state);
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
	 * ������ʽ�ļ���
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
	 * ��ʾ��ʾicon��edittext�Ķ�����ʵ��
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
	 * �����Ƿ�ͨ����֤
	 * */
	public boolean getIsMatches() {
		return isMatches;
	}
	


	
}
