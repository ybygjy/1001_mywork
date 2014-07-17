package org.ybygjy.jcip.chap3;

import java.util.ArrayList;
import java.util.List;

/**
 * ����this�ݳ���֤
 * @author WangYanCheng
 * @version 2014-7-17
 */
public class ThisEscapeTest {
    public static void main(String[] args) {
        ThisEscape thisEscape = new ThisEscape();
        thisEscape.fireEvent();
    }
    /**
     * ��������ʽthis�����ݳ�
     * @author WangYanCheng
     * @version 2014-7-17
     */
    static class ThisEscape implements EventSource {
        /** �ڲ�ʵ������*/
        private long price;
        /** ���̰߳�ȫ�¼�����*/
        private List<EventSourceEvent> eventArray = new ArrayList<EventSourceEvent>();
        /**
         * ���캯��
         */
        public ThisEscape() {
            //�ڹ��캯����ע���ⲿ�¼�
            registerListener(new EventSourceEvent(){
                //ThisEscape�ݳ�ʵ��
                private ThisEscape thisEscape;
                /** ע���ʱ{@link ThisEscape}���캯����δ����*/
                {
                    System.out.println(thisEscape.eventArray);
                    //���Ϲ淨����_Begin
                    //thisEscape.eventArray = Collections.synchronizedList(new ArrayList<EventSourceEvent>());
                    //thisEscape.price = 0;
                    //���Ϲ淨����_End
                }
                @Override
                public void onEvent(Event e) {
                    thisEscape = ThisEscape.this;
                    doSomething(e);
                    innerDanger(ThisEscape.this);
                }
                public void innerDanger(ThisEscape thisEscape) {
                    thisEscape.doSomething(new Event("VirtualOtherEvent", this));
                }
            });
        }
        @Override
        public String toString() {
            return "ThisEscape [price=" + price + ", eventArray=" + eventArray + "]";
        }
        /**
         * �¼�����
         */
        public void fireEvent() {
            for (EventSourceEvent ese : eventArray) {
                ese.onEvent(new Event("org.ybygjy.jcip.chap3.ThisEscapeTest.ThisEscape.fireEvent()", this));
            }
        }
        /**
         * DoSomething
         * @param event {@link Event}
         */
        public void doSomething(Event event) {
            System.out.println(event.toString());
        }
        @Override
        public void registerListener(EventSourceEvent event) {
            eventArray.add(event);
        }
    }
}
interface EventSource {
    public void registerListener(EventSourceEvent event);
}
interface EventSourceEvent {
    public void onEvent(Event e);
}
class Event {
    private String eventToken;
    private Object srcObj;
    public Event(String eventToken, Object srcObj) {
        this.eventToken = eventToken;
        this.srcObj = srcObj;
    }
    @Override
    public String toString() {
        return "Event [eventToken=" + eventToken + ", srcObj=" + srcObj + "]";
    }
    
}