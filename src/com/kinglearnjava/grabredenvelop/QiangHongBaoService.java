package com.kinglearnjava.grabredenvelop;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;

/**
 * �������ҷ���
 * ���� https://github.com/lendylongli/qianghongbao ��д
 */
public class QiangHongBaoService extends AccessibilityService {
    
    private static final String TAG = "QiangHongBao";
    
    // ΢�Ű���
    private static final String WECHAT_PACKAGENAME = "com.tencent.mm";
    
    // �����Ϣ�ؼ���
    private static final String HONGBAO_TEXT_KEY = "[΢�ź��]";
    
    private Handler handler = new Handler();

    /**
     * �������صķ���
     * ����ϵͳ������AccessbilityEvent���Ѿ����������ļ�����
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        
    }

    @Override
    public void onInterrupt() {
    }

}
