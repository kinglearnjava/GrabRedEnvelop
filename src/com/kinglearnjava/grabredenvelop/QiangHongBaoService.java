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
        LogUtil.d(TAG, "�¼�---->" + event);
        
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
            // ���ڸı䣨��һ����΢�ţ�������жϽ��棩��������򿪺������ 
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
    private void openHongBao(AccessibilityEvent event) {
        // ��ȡ�������
        if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            // ��ȡ���
            getPacket();
        } else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
            // ����������ϸ�ļ�¼����
            // ʲô������
        } else if ("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            // �������
            openPacket();
        }
    }
    
    /**
     * ����
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            LogUtil.w(TAG, "rootWindowΪ��");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("����");
        for (AccessibilityNodeInfo n :list) {
            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }
    
    /**
     * ����������е���
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            LogUtil.w(TAG, "rootWindowΪ��");
            return;
        }
        // �ҵ���ȡ����ĵ���¼�
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("��ȡ���");
        if (list.isEmpty()) {
            list = nodeInfo.findAccessibilityNodeInfosByText(HONGBAO_TEXT_KEY);
            for(AccessibilityNodeInfo n : list) {
                LogUtil.i(TAG, "-->΢�ź��:" + n);
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        } else {
            // ���µĺ������
            for (int i = list.size() - 1 ; i >= 0; i--) {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                LogUtil.i(TAG, "-->��ȡ���:" + parent);
                if (parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }

}
