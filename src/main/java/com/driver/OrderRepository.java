package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderHashMap;
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap;
    HashMap<String,List<String>> orderPartnerHashMap;

   // HashMap<String,String> orderId_partnerId_Map;

    public OrderRepository() {
        this.orderHashMap = new HashMap<>();
        this.deliveryPartnerHashMap = new HashMap<>();
        this.orderPartnerHashMap = new HashMap<>();
       // this.orderId_partnerId_Map = new HashMap<>();
    }

    public void addOrder(Order order){
        orderHashMap.put(order.getId(), order);
    }

    public void addPartner(String partnerId){
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        deliveryPartnerHashMap.put(partnerId,partner);
    }

    public void addOrderPartnerPair(String orderId,String partnerId){
        int numberOfOrders = deliveryPartnerHashMap.get(partnerId).getNumberOfOrders();
        if(!orderPartnerHashMap.containsKey(partnerId)){
            List<String> list = new ArrayList<>();
            list.add(orderId);
            orderPartnerHashMap.put(partnerId,list);
            numberOfOrders++;
        }
        else{
            orderPartnerHashMap.get(partnerId).add(orderId);
            numberOfOrders++;
        }
       // orderId_partnerId_Map.put(orderId,partnerId);
        deliveryPartnerHashMap.get(partnerId).setNumberOfOrders(numberOfOrders);
    }

    public Order getOrderById(String orderId){
        return orderHashMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerHashMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){
        int count=0;
        for(String s : orderPartnerHashMap.get(partnerId)){
            count++;
        }
        return count;
    }

//    public List<Order> getOrdersByPartnerId(String partnerId){
//        List<Order> list = new ArrayList<>();
//        for(String s : orderPartnerHashMap.get(partnerId)){
//            Order order = orderHashMap.get(s);
//            list.add(order);
//        }
//        return list;
//    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> list = new ArrayList<>();
        for(String order : orderPartnerHashMap.get(partnerId)){
            list.add(order);
        }
        return list;
    }

//    public List<Order> getAllOrders(){
//        List<Order> list = new ArrayList<>();
//        for(Order order : orderHashMap.values()){
//            list.add(order);
//        }
//        return list;
//    }

    public List<String> getAllOrders(){
        List<String> list = new ArrayList<>();
        for(String order : orderHashMap.keySet()){
            list.add(order);
        }
        return list;
    }

    public int getCountOfUnassignedOrders(){
        int totalNumberOfOrders=0;
        for(String s : deliveryPartnerHashMap.keySet()){
            int n = deliveryPartnerHashMap.get(s).getNumberOfOrders();
            totalNumberOfOrders+=n;
        }
        return orderHashMap.size()-totalNumberOfOrders;
          //int count = 0;
//        for(String orderID : orderHashMap.keySet()){
//            if(!orderPartnerHashMap.containsValue(orderID)){
//                count++;
//            }
//        }
//        return count;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        int count = 0;
        int deliveryTime = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));
        for(String s : orderPartnerHashMap.get(partnerId)){
            if(orderHashMap.get(s).getDeliveryTime() > deliveryTime){
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String time ="";
        List<String> list = orderPartnerHashMap.get(partnerId);
        int n = list.size();
        String s = list.get(n-1);
        int deliveryTime = orderHashMap.get(s).getDeliveryTime();

        int hour = deliveryTime / 60;
        String sHour = "";
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }
        int min = deliveryTime % 60;
        String sMin = "";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }

        time = sHour + ":" + sMin;

        return time;

    }

    public void deletePartnerById(String partnerId){
        orderPartnerHashMap.remove(partnerId);
        deliveryPartnerHashMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId){
        boolean flag =false;
        for(String s : orderPartnerHashMap.keySet()){
            for(String s1 : orderPartnerHashMap.get(s)){
                if(s1.equals(orderId)){
                    orderPartnerHashMap.remove(s);
                    flag=true;
                    break;
                }
            }
            if(flag==true) break;
//            if(orderPartnerHashMap.get(s).contains(orderId)){
//                orderPartnerHashMap.remove(s);
//            }
        }
        if(flag==false) return;
        orderHashMap.remove(orderId);
    }

}
