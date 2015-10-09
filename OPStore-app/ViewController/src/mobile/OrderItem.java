package mobile;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class OrderItem {
    
    private Product product = null;
    private int quantity = 0;
    private double paidPrice = 0.0;
    private boolean filter = false;
    private boolean showDelete = false;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public OrderItem(Product product, int quantity) {
        super();
        this.product = product;
        this.quantity = quantity;
        
        if (product!=null)
            paidPrice = product.salePrice;
    }

    public void setPaidPrice(double paidPrice) {
        double oldPaidPrice = this.paidPrice;
        this.paidPrice = paidPrice;
        propertyChangeSupport.firePropertyChange("paidPrice", oldPaidPrice, paidPrice);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public double getPaidPrice() {
        return paidPrice;
    }

    public void setProduct(Product product) {
        Product oldProduct = this.product;
        this.product = product;
        propertyChangeSupport.firePropertyChange("product", oldProduct, product);
    }

    public Product getProduct() {
        return product;
    }

    public void setQuantity(int quantity) {
        int oldQuantity = this.quantity;
        this.quantity = quantity;
        propertyChangeSupport.firePropertyChange("quantity", oldQuantity, quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setFilter(boolean filter) {
        boolean oldFilter = this.filter;
        this.filter = filter;
        propertyChangeSupport.firePropertyChange("filter", oldFilter, filter);
    }

    public boolean isFilter() {
        return filter;
    }
    public void setInventoryQuantity(int inventoryQuantity) {
        int oldInventoryQuantity = this.getProduct().getInventoryQuantity();
        this.getProduct().setInventoryQuantity(inventoryQuantity);
        propertyChangeSupport.firePropertyChange("inventoryQuantity", oldInventoryQuantity, inventoryQuantity);
    }

    public int getInventoryQuantity() {
        return this.getProduct().getInventoryQuantity();
    }

    public void setShowDelete(boolean showDelete) {
        boolean oldShowDelete = this.showDelete;
        this.showDelete = showDelete;
        propertyChangeSupport.firePropertyChange("showDelete", oldShowDelete, showDelete);
    }

    public boolean isShowDelete() {
        return showDelete;
    }
    
    public void incrementQuantity() {
        int quantity = this.getQuantity();
        this.setQuantity(++quantity);

    }
    
    public void deccrementQuantity() {
        int quantity = this.getQuantity();
        this.setQuantity(--quantity);
    }
    
    public void incrementInventoryQuantity() {
        int quantity = this.getInventoryQuantity();
        this.setInventoryQuantity(++quantity);
        //decrease Quantity for purchase
        this.deccrementQuantity();
    }
    
    public void deccrementInventoryQuantity() {
        int quantity = this.getInventoryQuantity();
        this.setInventoryQuantity(--quantity);
        //decrease Quantity for purchase
        this.incrementQuantity();
    }
    
}
