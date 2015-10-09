package mobile;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import javax.el.ValueExpression;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.amx.event.ValueChangeEvent;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class Replenishment {
    private int selectMode = 1;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String itemFilter;
    private boolean searchOn=false;

    public Replenishment() {
    }
    
    private int getCurrentProductID() {
        ValueExpression ve = AdfmfJavaUtilities.getValueExpression("#{pageFlowScope.selectedProductID}", Integer.class);
        Integer rowIndexObj = (Integer)ve.getValue(AdfmfJavaUtilities.getAdfELContext());
        int productIndex = rowIndexObj.intValue();
        return productIndex;         
    }

    private OrderItem getCurrentOrderItemByID() {
        int productID = getCurrentProductID();
        
        Store selectedStore = ServicesWrapper.getSelectedStore();
        OrderList orderList = selectedStore.getOrdersList();
        OrderItem item = orderList.getOrderItemByProductID(productID);
        return item;        
    }
    
     public void IncrementInStockByProductID(ActionEvent d) {
        OrderItem item = getCurrentOrderItemByID();
        item.incrementInventoryQuantity();
    }

    public void DecrementInStcokByProductID(ActionEvent d) {
        OrderItem item = getCurrentOrderItemByID();
        item.deccrementInventoryQuantity();
    }
    
    public void IncrementQuantityByProductID(ActionEvent d) {
        OrderItem item = getCurrentOrderItemByID();
        item.incrementQuantity();
    }

    public void DecrementQuantityByProductID(ActionEvent d) {
        OrderItem item = getCurrentOrderItemByID();
        item.deccrementQuantity();
    }

    public String DeleteRowByProductID() {
        try{
            int productIndex = getCurrentProductID();
            Store selectedStore = ServicesWrapper.getSelectedStore();
            OrderList orderList = selectedStore.getOrdersList();
            orderList.removeOrderItemByProductID(productIndex);
            
        }catch(Exception e) {
            e.printStackTrace();;
        }
        // Add event code here...
        return "refresh";
    }

    public void setSelectMode(int selectMode) {
        int oldSelectMode = this.selectMode;
        this.selectMode = selectMode;
        propertyChangeSupport.firePropertyChange("selectMode", oldSelectMode, selectMode);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public int getSelectMode() {
        return selectMode;
    }

    public void setItemFilter(String itemFilter) {
        this.itemFilter = itemFilter;
    }

    public String getItemFilter() {
        return itemFilter;
    }
    
    public String FilterProductsByNameOrID() {
        Store selectedStore = ServicesWrapper.getSelectedStore();
        OrderList orderList = selectedStore.getOrdersList();
        if (itemFilter != null  && itemFilter.length()>0) {
            String filter = itemFilter.toLowerCase();
            OrderItem[] allOrders = orderList.getOrders();
            for (int index=0;index<allOrders.length;index++) 
            {
                OrderItem orderItem = (OrderItem)allOrders[index];
                String productName = orderItem.getProduct().getName().toLowerCase();
                String productID = "" +orderItem.getProduct().getProdID();
                //contains the name of the product
                if ((productName.indexOf(filter)<0) &&(productID.indexOf(filter)<0))
                {
                    orderItem.setFilter(true);
                }
                else {
                    orderItem.setFilter(false);                    
                }
            }
        }
        else{
            OrderItem[] allOrders = orderList.getOrders();
            for (int index=0;index<allOrders.length;index++) 
            {
                OrderItem orderItem = (OrderItem)allOrders[index];
                orderItem.setFilter(false);
            }
        }
        
        return "refresh";
    }

    public void setSearchOn(boolean searchOn) {
        boolean oldSearchOn = this.searchOn;
        this.searchOn = searchOn;
        propertyChangeSupport.firePropertyChange("searchOn", oldSearchOn, searchOn);
    }

    public boolean isSearchOn() {
        return searchOn;
    }
    
    public String IncrementQuantityByProductID() {
        OrderItem item = getCurrentOrderItemByID();
        item.incrementQuantity();
        return "refresh";
    }

    public String DecrementQuantityByProductID() {
        OrderItem item = getCurrentOrderItemByID();
        item.deccrementQuantity();
        return "refresh";
    }

    public String RemoveProductFromRecommendedListByProductID() {
        OrderItem item = getCurrentOrderItemByID();
        item.getProduct().setRecommended(0);
        return "refresh";
    }

    public String RemoveProductFromBestSellerListByProductID() {
        OrderItem item = getCurrentOrderItemByID();
        item.getProduct().setBestSeller(0);
        return "refresh";
    }

    public void FilterProductsByNameOrID(ActionEvent actionEvent) {
        FilterProductsByNameOrID();
    }
}
