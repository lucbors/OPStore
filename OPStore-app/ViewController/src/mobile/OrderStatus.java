package mobile;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class OrderStatus {
    public static final int OPEN=0;
    public static final int PAID=1;
    public static final int SHIPPED=2;
    public static final int CLOSED=3;
    
    public static final String OPEN_STR="Open";
    public static final String PAID_STR="Paid";
    public static final String SHIPPED_STR="Shipped";
    public static final String CLOSED_STR="Closed";
    
    int status = OPEN;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public OrderStatus() {
        super();
    }

    public void setStatus(int status) {
        int oldStatus = this.status;
        this.status = status;
        propertyChangeSupport.firePropertyChange("status", oldStatus, status);
    }

    public void setStatus(String statusStr) {
        int oldStatus = this.status;
        statusStr = statusStr.toUpperCase();
        if (statusStr.equals(OPEN_STR))
            this.status = OPEN;
        else if (statusStr.equals(PAID_STR))
            this.status = PAID;
        else if (statusStr.equals(SHIPPED_STR))
            this.status = SHIPPED;
        else if (statusStr.equals(CLOSED_STR))
            this.status = CLOSED;

        propertyChangeSupport.firePropertyChange("status", oldStatus, status);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public int getStatus() {
        return status;
    }
    
    public String getStatusStr() {
        switch (status){
        case OPEN: return OPEN_STR;
        case PAID: return PAID_STR;
        case SHIPPED: return SHIPPED_STR;
        case CLOSED: return CLOSED_STR;
        }
        return "";
    }
}
