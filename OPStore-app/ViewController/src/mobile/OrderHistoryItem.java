package mobile;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class OrderHistoryItem {
    private String quarter = "";
    private double orderAmount = 0.0;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public OrderHistoryItem() {
        super();
    }

    public void setQuarter(String quarter) {
        String oldQuarter = this.quarter;
        this.quarter = quarter;
        propertyChangeSupport.firePropertyChange("quarter", oldQuarter, quarter);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public String getQuarter() {
        return quarter;
    }

    public void setOrderAmount(double orderAmount) {
        double oldOrderAmount = this.orderAmount;
        this.orderAmount = orderAmount;
        propertyChangeSupport.firePropertyChange("orderAmount", oldOrderAmount, orderAmount);
    }

    public double getOrderAmount() {
        return orderAmount;
    }
}
