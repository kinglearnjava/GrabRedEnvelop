package com.kinglearnjava.grabredenvelop;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.os.Build;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * �������ҷ���
 * ���� https://github.com/lendylongli/qianghongbao ��д���иĽ�
 */
public class QiangHongBaoService extends AccessibilityService {
    
    // ������TAG
    private static final String TAG = "QiangHongBao";
    private static final String TAG_EVENT_TEST = "�¼�����";
    private static final String TAG_NODE = "AccessibilityNodeInfo����";
    
    // ΢�Ű���
    private static final String WECHAT_PACKAGENAME = "com.tencent.mm";
    
    // ֪ͨ�������Ϣ�ؼ���
    private static final String HONGBAO_TEXT_KEY = "[΢�ź��]";
    
    // ��������İ�ť�ؼ���
    private static final String GET_HONGBAO_KEY = "����";
    
    // ���������˷��ĺ���Ĺؼ���
    private static final String OPEN_OTHERS_HONGBAO_KEY = "��ȡ���";
    
    // ��������Լ����ĺ���Ĺؼ���
    private static final String OPEN_SELF_HONGBAO_KEY = "�鿴���";
    
    // ��һ�������Ǻ������
    // �˳����������棬����������棬����TYPE_WINDOW_STATE_CHANGED�¼��������������
    // һ���������к��������˱��Ϊtrue������ȡ��ͬʱ�Ѵ˱�Ǹ�Ϊfalse
    // �����������޺������Ѵ˱�Ǹ�Ϊfalse
    private boolean lastWindowHongBaoDetail = false;
    
    private Handler handler = new Handler();

    /**
     * �������صķ���
     * ����ϵͳ������AccessbilityEvent���Ѿ����������ļ�����
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        LogUtil.d(TAG, "onAccessibilityEvent-----���ڴ������¼�---->" + event);
        
        // �����ã�����΢���и��ֶ����ᴥ��ʲô�¼�
        String eventText = "";
        String eventPackageName = String.valueOf(event.getPackageName());
        List<CharSequence> eventTexts = event.getText(); 
        String eventClassName = String.valueOf(event.getClassName());
        switch (eventType) {  
        case AccessibilityEvent.TYPE_VIEW_CLICKED:  
            eventText = "TYPE_VIEW_CLICKED";  
            break;  
        case AccessibilityEvent.TYPE_VIEW_FOCUSED:  
            eventText = "TYPE_VIEW_FOCUSED";  
            break;  
        case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:  
            eventText = "TYPE_VIEW_LONG_CLICKED";  
            break;  
        case AccessibilityEvent.TYPE_VIEW_SELECTED:  
            eventText = "TYPE_VIEW_SELECTED";  
            break;  
        case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:  
            eventText = "TYPE_VIEW_TEXT_CHANGED";  
            break;  
        case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:  
            eventText = "TYPE_WINDOW_STATE_CHANGED";  
            break;  
        case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:  
            eventText = "TYPE_NOTIFICATION_STATE_CHANGED";  
            break;  
        case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:  
            eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_END";  
            break;  
        case AccessibilityEvent.TYPE_ANNOUNCEMENT:  
            eventText = "TYPE_ANNOUNCEMENT";  
            break;  
        case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:  
            eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_START";  
            break;  
        case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:  
            eventText = "TYPE_VIEW_HOVER_ENTER";  
            break;  
        case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:  
            eventText = "TYPE_VIEW_HOVER_EXIT";  
            break;  
        case AccessibilityEvent.TYPE_VIEW_SCROLLED:  
            eventText = "TYPE_VIEW_SCROLLED";  
            break;  
        case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:  
            eventText = "TYPE_VIEW_TEXT_SELECTION_CHANGED";  
            break;  
        case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:  
            eventText = "TYPE_WINDOW_CONTENT_CHANGED";  
            break;  
        }  
        LogUtil.i(TAG_EVENT_TEST, "�����¼�---->" + eventText);
        LogUtil.i(TAG_EVENT_TEST, "�¼��İ���---->" + eventPackageName);
        LogUtil.i(TAG_EVENT_TEST, "�¼�������---->" + eventClassName);
        LogUtil.i(TAG_EVENT_TEST, "�¼����ı�---->");
        for (CharSequence s : eventTexts) {
            LogUtil.i(TAG_EVENT_TEST,  String.valueOf(s));
        }
        
        // ֪ͨ���¼�
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            // ֪ͨ����������Ϣ
            // ��ȡ֪ͨ����Ϣ����
            List<CharSequence> texts = event.getText();
            // ����Ƿ��к����Ϣ
            if (!texts.isEmpty()) {
                for (CharSequence t : texts) {
                    String text = String.valueOf(t);
                    if (text.contains(HONGBAO_TEXT_KEY)) {
                        openNotify(event);
                        break;
                    }
                }
            }
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // ��������APP��Activity�ı�ʱ������AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            openHongBao(event);
        } 
    }

    
    /**
     * �������صķ��� 
     * ϵͳ��Ҫ�ж�AccessibilityService��������Ӧʱ�����
     * ���������л���ö��
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "�ж����������", Toast.LENGTH_SHORT).show();
    }

    
    /**
     * ��ѡ�ķ��� 
     * ϵͳ���ڳɹ������Ϸ���ʱ������������
     * ������������������һ�³�ʼ������
     * �����豸�������𶯹���Ҳ���Ե���setServiceInfo()�������ù�����
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "�������������", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * ����֪ͨ����Ϣ��ģ��΢�ź����Ϣ��
     */
    private void sendNotifyEvent() {
        AccessibilityManager manager = (AccessibilityManager)getSystemService(ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) {
            return;
        }
        AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        event.setPackageName(WECHAT_PACKAGENAME);
        event.setClassName(Notification.class.getName());
        CharSequence tickerText = HONGBAO_TEXT_KEY;
        event.getText().add(tickerText);
        manager.sendAccessibilityEvent(event);
    }
    
    /**
     * ��֪ͨ����Ϣ
     */
    private void openNotify(AccessibilityEvent event){
        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        // ��΢�ŵ�֪ͨ����Ϣ��
        // ��ȡNotification���� 
        Notification notification = (Notification) event.getParcelableData();
        // �������е�PendingIntent����΢�Ž���
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ��΢�ź��ж���ʲô���棬������Ӧ�Ķ���
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openHongBao(AccessibilityEvent event) {
        if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            // ��������
            getPacket();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
            // ����������ϸ�ļ�¼����
            LogUtil.e(TAG, "���������棬�ı�lastWindowHongBaoDetail����ʱֵΪ" + lastWindowHongBaoDetail);
            lastWindowHongBaoDetail = true;
        } else if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName()) ) {
            // �������
            openPacket();
//            // �����ã������ǰ����AccessibilityNodeInfo�����������Ϣ
//            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
//            recycle(nodeInfo);
        } 
    }
    
    /**
     * �ڲ��������д򿪺��
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            LogUtil.w(TAG, "getPacket()------rootWindowΪ��");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(GET_HONGBAO_KEY);
        for (AccessibilityNodeInfo n :list) {
            LogUtil.v(TAG + "����", "getPacket()-------->΢�ź��--------->" + n);
            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }
    
    /**
     * ����������д򿪺��
     * ��AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED���� 
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        LogUtil.d(TAG, "RootWindow��ClassName:" + nodeInfo.getClassName());
        if (nodeInfo == null) {
            LogUtil.w(TAG + "�������", "openPacket()-------->rootWindowΪ��");
            return;
        }
        // �ҵ���ȡ����ĵ���¼������˷��ĺ��
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(OPEN_OTHERS_HONGBAO_KEY);
        // �������Լ����ĺ��
        if (list.isEmpty()) {            
            list = nodeInfo.findAccessibilityNodeInfosByText(OPEN_SELF_HONGBAO_KEY);
        } 
        // ��û�к��������������أ�����ȡ�����ı���
        if (list.isEmpty() || lastWindowHongBaoDetail) {
            LogUtil.e(TAG, "������棬�ı�lastWindowHongBaoDetail����ʱֵΪ" + lastWindowHongBaoDetail);
            lastWindowHongBaoDetail = false;
            return;
        } else {
            // ��ǰ��������ж�������ֻ�����µġ�
            for (int i = list.size() - 1 ; i >= 0; i--) {
                // ͨ�����Կ�֪[��ȡ���]��text�������ɱ��������getParent()��ȡ�ɱ�����Ķ���
                AccessibilityNodeInfo parent = list.get(i).getParent();
                // �Ӹ��жϣ������ָ��
                if ( parent != null) {
                    LogUtil.e(TAG, "��������" + getOriginToString(parent));
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break; // ֻ�����µ�һ�����
                }
            }
        }
    }
    
    
    /**
     * JavaĬ�ϵ�toString()����������ʶ���Ƿ�ͬһ������
     * Android��д��toString()�����Լ�ʵ��һ��
     */
    private String getOriginToString(Object o) {
        return o.getClass().getName() + '@' + Integer.toHexString(o.hashCode());
    }
    
    /**
     * ����AccessibilityNodeInfo�����
     * @param nodeInfo
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void recycle(AccessibilityNodeInfo nodeInfo) {
        LogUtil.i(TAG_NODE, "------------------------------------------");
        LogUtil.i(TAG_NODE, "nodeInfo.getChildCount()----->" + nodeInfo.getChildCount());
        LogUtil.i(TAG_NODE, "nodeInfo.getWindowId()----->" + nodeInfo.getWindowId());
        LogUtil.i(TAG_NODE, "nodeInfo.getClassName()----->" + nodeInfo.getClassName());
        LogUtil.i(TAG_NODE, "nodeInfo.getContentDescription()----->" + nodeInfo.getContentDescription());
        LogUtil.i(TAG_NODE, "nodeInfo.getPackageName()----->" + nodeInfo.getPackageName());
        LogUtil.i(TAG_NODE, "nodeInfo.getText()----->" + nodeInfo.getText());
//        LogUtil.i(TAG_NODE, "nodeInfo.getViewIdResourceName()----->" + nodeInfo.getViewIdResourceName());
//        LogUtil.i(TAG_NODE, "nodeInfo.getActions()----->" + nodeInfo.getActions());
//        LogUtil.i(TAG_NODE, "nodeInfo.getMovementGranularities()----->" + nodeInfo.getMovementGranularities());
//        LogUtil.i(TAG_NODE, "nodeInfo.getgetTextSelectionEnd()----->" + nodeInfo.getTextSelectionEnd());
//        LogUtil.i(TAG_NODE, "nodeInfo.getTextSelectionStart()----->" + nodeInfo.getTextSelectionStart());
//        LogUtil.i(TAG_NODE, "nodeInfo.getViewIdResourceName()----->" + nodeInfo.getViewIdResourceName());
//        LogUtil.i(TAG_NODE, "nodeInfo.getLabeledBy()----->" + nodeInfo.getLabeledBy());
//        LogUtil.i(TAG_NODE, "nodeInfo.getLabelFor()----->" + nodeInfo.getLabelFor());
        if (nodeInfo.getChildCount() > 0) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                recycle(nodeInfo.getChild(i));
            }
        }
    }

}
