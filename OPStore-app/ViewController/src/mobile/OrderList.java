package mobile;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class OrderList {
    private List orders = null;

    public OrderList() {
        if (orders == null) {
            orders = new ArrayList();
        }
    }

    public boolean setOrders() {
        
        return true;
    }
        
    public OrderItem[] getOrders() {
        OrderItem p[] = null;

        p = (OrderItem[])orders.toArray(new OrderItem[orders.size()]);

        return p;
    }
    
    public void addOrder(OrderItem order) {
        orders.add(order);
    }
    
    public OrderItem getOrderItemAt(int index) {
        if (index<orders.size())
            return (OrderItem)orders.get(index);
        else
            return null;
    }
    
    public OrderItem removeOrderItemAt(int index) {
        if (index<orders.size())
            return (OrderItem)orders.remove(index);
        else
            return null;
    }
    
    public OrderItem getOrderItemByProductID(int id) {
        for (int index=0;index<orders.size();index++)
        {
            OrderItem order = (OrderItem)orders.get(index);
            if (order.getProduct().getProdID()==id)
                return order;
        }            
        return null;
    }
    
    public boolean removeOrderItemByProductID(int id) {
        for (int index=0;index<orders.size();index++)
        {
            OrderItem order = (OrderItem)orders.get(index);
            if (order.getProduct().getProdID()==id)
            {
                orders.remove(index);
                return true;
            }
        }            
        return false;
    }
    

}
