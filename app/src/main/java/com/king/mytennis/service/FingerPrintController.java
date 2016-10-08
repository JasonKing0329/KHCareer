package com.king.mytennis.service;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;
import com.samsung.android.sdk.pass.SpassFingerprint.IdentifyListener;

import android.content.Context;
import android.util.Log;

public class FingerPrintController implements IdentifyListener {

	private final String TAG = "FingerPrintController";
	private SpassFingerprint mSpassFingerprint;
	private Spass mSpass;
	private boolean isSupported;
	private boolean isFeatureEnabled;
	private Context context;
	private SimpleIdentifyListener simpleIdentifyListener;

    public interface SimpleIdentifyListener {
    	public void onSuccess();
    	public void onFail();
    	public void onCancel();
    }
    
	public FingerPrintController(Context context) {
		
		this.context = context;
		mSpass = new Spass();
        try {
            mSpass.initialize(context);
            isSupported = true;
        } catch (SsdkUnsupportedException e) {
            Log.d(TAG, "Exception: " + e);
        } catch (UnsupportedOperationException e){
        	isSupported = false;
        	Log.d(TAG, "Fingerprint Service is not supported in the device");
        }
        isFeatureEnabled = mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);
        
        if (isSupported()) {
        	mSpassFingerprint = new SpassFingerprint(context);
		}
	}
	
	public boolean isSupported() {
		
		return isSupported && isFeatureEnabled;
	}
	
	public boolean hasRegistered() {
		
		boolean hasRegisteredFinger = mSpassFingerprint.hasRegisteredFinger();
		return hasRegisteredFinger;
	}
	
	public void showIdentifyDialog(boolean withPW, SimpleIdentifyListener listener) {

		this.simpleIdentifyListener = listener;
        mSpassFingerprint.startIdentifyWithDialog(context, this, withPW);
	}

    public String getEventStatusName(int eventStatus) {
        switch (eventStatus) {
        case SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS:
            return "STATUS_AUTHENTIFICATION_SUCCESS";
        case SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS:
            return "STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS";
        case SpassFingerprint.STATUS_TIMEOUT_FAILED:
            return "STATUS_TIMEOUT";
        case SpassFingerprint.STATUS_SENSOR_FAILED:
            return "STATUS_SENSOR_ERROR";
        case SpassFingerprint.STATUS_USER_CANCELLED:
            return "STATUS_USER_CANCELLED";
        case SpassFingerprint.STATUS_QUALITY_FAILED:
            return "STATUS_QUALITY_FAILED";
        case SpassFingerprint.STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE:
            return "STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE";
        case SpassFingerprint.STATUS_AUTHENTIFICATION_FAILED:
        default:
            return "STATUS_AUTHENTIFICATION_FAILED";
        }
        
    }
    
    public int getFingerprintIndex() {
    	return mSpassFingerprint.getIdentifiedFingerprintIndex();
    }
    
    public boolean identyfySuccess(int eventStatus) {
    	return eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS
    			|| eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS;
    }

	@Override
	public void onFinished(int eventStatus) {
		if (simpleIdentifyListener != null) {
			if (identyfySuccess(eventStatus)) {
				simpleIdentifyListener.onSuccess();
			}
			else {
				simpleIdentifyListener.onFail();
			}
		}
	}

	@Override
	public void onReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStarted() {
		// TODO Auto-generated method stub
		
	}
    
}
