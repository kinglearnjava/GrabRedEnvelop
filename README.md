

##���ڵ�����
- �޷��ж�һ������Ƿ��Ѿ�����ȡ���������ε�ԭ�����л�����ʱ���޷�����ͬ�ĺ��������Ŀǰֻ��������ȡ���º����
1.ʹ��AccessibilityNodeInfo��ȡ�ĺ����Ϣ�в�����ʱ�䣬�޷�ʹ��ʱ��������
2.���AccessibilityNodeInfo�����ֻᱻ�ظ����ڲ�ͬ�ĺ���ϣ��޷�ʹ�ö����ClassName+hashCode������
    <1>ͬһ�����������ͬʱ��ʾ�Ĳ�ͬ������ò�ͬ��AccessibilityNodeInfo�����ʾ������ÿ���л����ý��棬��Щ�������ĺ������仯
    <1>ͬһ����������в���ͬʱ��ʾ��������ͬ���������ͬ��AccessibilityNodeInfo�����ʾ
    
- ��ǰ��������յ����ʱ�����ᱻ������
������AccessibilityEvent.TYPE_VIEW_CLICKED�¼�������º���������ں���������һ���ᴥ�����¼����漰����ظ��жϵ��߼�����ʱȡ�����¼��ļ���

##�¼�����
####����������յ���ǰ���ѵ�һ����Ϣʱ��
�����¼�---->TYPE_WINDOW_CONTENT_CHANGED
�¼��İ���---->com.tencent.mm
�¼�������---->android.widget.ListView
�¼����ı�---->
�����¼�---->TYPE_VIEW_SCROLLED
�¼��İ���---->com.tencent.mm
�¼�������---->android.widget.ListView
�¼����ı�---->
�����¼�---->TYPE_WINDOW_CONTENT_CHANGED
�¼��İ���---->com.tencent.mm
�¼�������---->android.widget.ListView
�¼����ı�---->
�����¼�---->TYPE_WINDOW_CONTENT_CHANGED
�¼��İ���---->com.tencent.mm
�¼�������---->android.widget.ListView
�¼����ı�---->
�����¼�---->TYPE_WINDOW_CONTENT_CHANGED
�¼��İ���---->com.tencent.mm
�¼�������---->android.widget.ListView
�¼����ı�---->

####����������յ��������ѵ�һ����Ϣʱ��
�����¼�---->TYPE_WINDOW_CONTENT_CHANGED
�¼��İ���---->com.tencent.mm
�¼�������---->android.widget.ListView
�¼����ı�---->
�����¼�---->TYPE_NOTIFICATION_STATE_CHANGED
�¼��İ���---->com.tencent.mm
�¼�������---->android.app.Notification
�¼����ı�---->�����ǳ�: ������Ϣ

####����Activity���Żᴥ��AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED������΢�ŵ���Ϣ�б���浽������棬�����������¼���˵��û���л�Activity
