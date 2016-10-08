package com.king.mytennis.update_v_6_0;

import com.king.mytennis.view.publicview.DragSideBarTrigger;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class CardPage extends FrameLayout {

	private final boolean DEBUG = true;
	private final String TAG = "CardPage";
	private final float SCALE_HIDE = 0.2f;
	private final float ALPHA_HIDE = 0.4f;
	private final float AVAI_SCROLL_DIS = 0.6f;

	private DragSideBarTrigger dragSidBarTrigger;
	private CardListener cardListener;
	private OnTouchListener cardViewTouchListener;

	private int curCardIndex;

	public CardPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CardPage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void reset() {
		removeAllViews();
	}

	private void init() {
		cardViewTouchListener = new CardViewTouchListener();
	}

	public void addCardListener(CardListener listener) {
		cardListener = listener;
	}

	public int getForgroundIndex() {
		return curCardIndex;
	}

	private void addCardAt(CardView cardView, int index) {
		cardView.setTag(getChildCount());
		curCardIndex = getChildCount();
		if (index < 0) {
			addView(cardView);
		}
		else {
			ViewHelper.setScaleX(cardView, SCALE_HIDE);
			ViewHelper.setScaleY(cardView, SCALE_HIDE);
			ViewHelper.setAlpha(cardView, ALPHA_HIDE);
			addView(cardView, index);
		}
		cardView.setOnTouchListener(cardViewTouchListener);

		if (getChildCount() > 1) {
			ViewHelper.setScaleX(getChildAt(curCardIndex - 1), SCALE_HIDE);
			ViewHelper.setScaleY(getChildAt(curCardIndex - 1), SCALE_HIDE);
			ViewHelper.setAlpha(getChildAt(curCardIndex - 1), ALPHA_HIDE);
			getChildAt(curCardIndex - 1).setVisibility(View.GONE);
		}
	}

	public void addCard(CardView cardView) {
		addCardAt(cardView, -1);
	}

	public void addCard(CardView cardView, int position)
			throws ArrayIndexOutOfBoundsException {
		if (position >= getChildCount() || position < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}

		addCardAt(cardView, position);
	}

	public int getCardNumber() {
		return getChildCount();
	}

	public CardView getCardView(int index) {
		return (CardView) getChildAt(index);
	}

	/**
	 * 由于alpha的存在，卡片的层叠排列会使切换过程中能看到下面好几层的卡片，因此在切换完成后隐藏非焦点卡片
	 */
	private void hideUnFocusCards() {
		for (int i = 0; i < getChildCount(); i ++) {
			if (i != curCardIndex) {
				getChildAt(i).setVisibility(View.GONE);
			}
		}
	}

	private class CardViewTouchListener implements OnTouchListener {

		private boolean isDragSide = false;
		private float startY;
		private float moveY, moveScale, moveAlpha;

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			if (dragSidBarTrigger != null) {
				isDragSide = dragSidBarTrigger.onTriggerTouch(event);
			}

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (!isDragSide) {
						startY = event.getRawY();
						if (DEBUG) {
							Log.d(TAG, "ACTION_DOWN startY=" + startY + ", index=" + view.getTag());
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (!isDragSide) {
						moveCards(event.getRawY());
					}
					break;
				case MotionEvent.ACTION_UP:
					if (!isDragSide) {
						changeCards(event.getRawY());
					}
					break;
				case MotionEvent.ACTION_OUTSIDE:
					if (!isDragSide) {
						changeCards(event.getRawY());
					}
					break;

				default:
					break;
			}
			return true;
		}

		private void moveCards(float y) {
			float dely = y - startY;
			View backView = null;
			if (dely > 0) {//scroll down
				if (curCardIndex < getChildCount() - 1) {
					backView = getChildAt(curCardIndex + 1);
					if (backView.getVisibility() == View.GONE) {
						backView.setVisibility(View.VISIBLE);
					}
				}
			}
			else if (dely < 0) {//scroll up
				if (curCardIndex > 0) {
					backView = getChildAt(curCardIndex - 1);
					if (backView.getVisibility() == View.GONE) {
						backView.setVisibility(View.VISIBLE);
					}
				}
			}

//			if (DEBUG) {
//				Log.d(TAG, "moveCards index=" + curCardIndex + ", dely=" + dely);
//			}
			if (backView != null) {
				float scale = SCALE_HIDE;
				float alphaScale = ALPHA_HIDE;
				float factor = dely / (getHeight() * AVAI_SCROLL_DIS);
				if (dely > 0) {//scroll down
					scale = (1 - SCALE_HIDE) * (1 - factor) + SCALE_HIDE;
					alphaScale = (1 - ALPHA_HIDE) * (1 - factor) + ALPHA_HIDE;

					moveY = -getHeight() + dely;
					ViewHelper.setTranslationY(backView, moveY);
					if (scale < SCALE_HIDE) {
						scale = SCALE_HIDE;
					}
					moveScale = scale;
					ViewHelper.setScaleX(getChildAt(curCardIndex), moveScale);
					ViewHelper.setScaleY(getChildAt(curCardIndex), moveScale);
					ViewHelper.setAlpha(getChildAt(curCardIndex), moveScale);

					moveAlpha = ALPHA_HIDE + (1 - alphaScale);
					ViewHelper.setAlpha(backView, moveAlpha);

				}
				else if (dely < 0) {//scroll up
					factor = -factor;
					scale = SCALE_HIDE + (1 - SCALE_HIDE) * factor;
					alphaScale = ALPHA_HIDE + (1 - ALPHA_HIDE) * factor;

					moveY = dely;
					ViewHelper.setTranslationY(getChildAt(curCardIndex), moveY);
					if (scale > 1) {
						scale = 1;
					}
					moveScale = scale;
					ViewHelper.setScaleX(backView, moveScale);
					ViewHelper.setScaleY(backView, moveScale);
					ViewHelper.setAlpha(backView, moveScale);

					moveAlpha = ALPHA_HIDE + (1 - alphaScale);
					ViewHelper.setAlpha(getChildAt(curCardIndex), moveAlpha);
				}

			}
		}

		private void changeCards(float y) {
			float dis = y - startY;
			if (DEBUG) {
				Log.d(TAG, "changeCards index=" + curCardIndex + ", dis=" + dis);
			}
			if (dis > 0) {//scroll down
				if (curCardIndex < getChildCount() - 1) {
					if (dis > getHeight() * AVAI_SCROLL_DIS) {//on change

						new AnimationEndTask(getChildAt(curCardIndex + 1), getChildAt(curCardIndex)
								, 0, 1).execute();
//						ViewHelper.setScaleX(getChildAt(curCardIndex), 1);
//						ViewHelper.setScaleY(getChildAt(curCardIndex), 1);
						if (cardListener != null) {
							cardListener.onCardChanged(curCardIndex, curCardIndex + 1);
						}
						curCardIndex ++;
//						ViewHelper.setTranslationY(getChildAt(curCardIndex), 0);
					}
					else {//not change
						new AnimationEndTask(getChildAt(curCardIndex + 1), getChildAt(curCardIndex)
								, -getHeight(), 1).execute();
//						ViewHelper.setScaleX(getChildAt(curCardIndex), 1);
//						ViewHelper.setScaleY(getChildAt(curCardIndex), 1);
//						ViewHelper.setTranslationY(getChildAt(curCardIndex + 1), -getHeight());
					}
					ViewHelper.setAlpha(getChildAt(curCardIndex), 1);
				}
			}
			else if (dis < 0) {//scroll up
				dis = -dis;

				if (curCardIndex > 0) {
					if (dis > getHeight() * AVAI_SCROLL_DIS) {//not change
						new AnimationEndTask(getChildAt(curCardIndex), getChildAt(curCardIndex - 1)
								, -getHeight(), 1).execute();
//						ViewHelper.setTranslationY(getChildAt(curCardIndex), -getHeight());
						if (cardListener != null) {
							cardListener.onCardChanged(curCardIndex, curCardIndex - 1);
						}
						curCardIndex --;
//						ViewHelper.setScaleX(getChildAt(curCardIndex), 1);
//						ViewHelper.setScaleY(getChildAt(curCardIndex), 1);
					}
					else {//not change
						new AnimationEndTask(getChildAt(curCardIndex), getChildAt(curCardIndex - 1)
								, 0, SCALE_HIDE).execute();
//						ViewHelper.setScaleX(getChildAt(curCardIndex - 1), SCALE_HIDE);
//						ViewHelper.setScaleY(getChildAt(curCardIndex - 1), SCALE_HIDE);
//						ViewHelper.setTranslationY(getChildAt(curCardIndex), 0);
					}
					ViewHelper.setAlpha(getChildAt(curCardIndex), 1);
				}
			}
		}

		/**
		 * 采用异步更新transView和scaleView的方法来完成action up后的动画收尾工作
		 * @author Administrator
		 *
		 */
		private class AnimationEndTask extends AsyncTask<Void, Float, Void> {

			private final int FRAME_TIME = 20;
			private View transView;
			private View scaleView;
			private float toy;
			private float toScale;
			public AnimationEndTask(View transView, View scaleView
					, float toy, float toScale) {
				this.transView = transView;
				this.scaleView = scaleView;
				this.toy = toy;
				this.toScale = toScale;
			}
			@Override
			protected Void doInBackground(Void... arg0) {

				int totalTime = (int) (toy - moveY < 0 ? moveY - toy : toy - moveY) / 3;
				int time = 0;
				float spaceTrans = (toy - moveY) / (totalTime / FRAME_TIME);
				float spaceScale = (toScale - moveScale) / (totalTime / FRAME_TIME);
				float startY = moveY;
				float startScale = moveScale;
				while (time < totalTime) {
					try {
						Thread.sleep(FRAME_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					startScale += spaceScale;
					startY += spaceTrans;
					time += FRAME_TIME;
					publishProgress(startScale, startY);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Float... values) {

				ViewHelper.setScaleX(scaleView, values[0]);
				ViewHelper.setScaleY(scaleView, values[0]);
				ViewHelper.setTranslationY(transView, values[1]);
				super.onProgressUpdate(values);
			}
			@Override
			protected void onPostExecute(Void result) {

				ViewHelper.setScaleX(scaleView, toScale);
				ViewHelper.setScaleY(scaleView, toScale);
				ViewHelper.setTranslationY(transView, toy);
				hideUnFocusCards();
				super.onPostExecute(result);
			}

		}

	}
	public void setDragSidBarTrigger(DragSideBarTrigger trigger) {
		this.dragSidBarTrigger = trigger;
	}


}
