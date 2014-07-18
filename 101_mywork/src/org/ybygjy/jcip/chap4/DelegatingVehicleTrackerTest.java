package org.ybygjy.jcip.chap4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ����ί�е��̰߳�ȫ�ĳ���׷����
 * @author WangYanCheng
 * @version 2014-7-18
 */
public class DelegatingVehicleTrackerTest {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        int vehicleNums = 3;
        Map<String, Point> points = new HashMap<String, Point>();
        String vehicleNamePrefix = "VehicleThread_";
        for (int i = 1; i < vehicleNums; i++) {
            String vehicleName = vehicleNamePrefix + i;
            points.put(vehicleName, new Point(0, 0));
        }
        DelegatingVehicleTracker vehicleTracker = new DelegatingVehicleTracker(points);
        for (int i = 1; i < vehicleNums; i++) {
            String vehicleName = vehicleNamePrefix + i;
            new Vehicle(vehicleName, vehicleTracker).start();
        }
        new ViewControll(vehicleTracker).start();
    }
}

/**
 * ��������Ϣ���ݲɼ�
 * @author WangYanCheng
 * @version 2014-7-18
 */
class DelegatingVehicleTracker {
    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;
    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<String, Point>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }
    /**
     * ȡ����λ����Ϣ��ʵʱ�ǿ��շ�ʽ
     * @return
     */
    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }
    public Point getLocation(String id) {
        return locations.get(id);
    }
    public void setLocation(String id, int x, int y) {
        if (null == locations.replace(id, new Point(x, y))) {
            throw new IllegalArgumentException("Invalid vehicle name: " + id);
        }
    }
}

/**
 * �����峵�������߳�
 * @author WangYanCheng
 * @version 2014-7-18
 */
class Vehicle extends Thread {
    private final DelegatingVehicleTracker vehicleTracker;
    private Random random = new Random(500);
    public Vehicle(String vehicleName, DelegatingVehicleTracker vehicleTracker) {
        super(vehicleName);
        this.vehicleTracker = vehicleTracker;
    }
    @Override
    public void run() {
        while (true) {
            vehicleTracker.setLocation(getName(), random.nextInt(), random.nextInt());
            try {
                sleep((long)(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * ǰ����Ⱦ�߳�
 * @author WangYanCheng
 * @version 2014-7-18
 */
class ViewControll extends Thread {
    private final DelegatingVehicleTracker vehicleTracker;
    public ViewControll(DelegatingVehicleTracker vehicleTracker) {
        this.vehicleTracker = vehicleTracker;
    }
    @Override
    public void run() {
        while (true) {
            Map<String, Point> vehicleLocations = vehicleTracker.getLocations();
            for (Map.Entry<String, Point> entry : vehicleLocations.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}