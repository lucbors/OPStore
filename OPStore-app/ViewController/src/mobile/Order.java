package mobile;

import java.util.Date;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class Order {
    protected static final String NO_PIC = "/images/missingEmployee_120.png"; 
    protected int id=0;
    protected Date date;
    protected String imagePath = NO_PIC;
    protected String status="";
    protected String paymentTpe="";
    protected String salesman="";
    protected String storeName="";
    protected String productName;
    protected double price;
    protected int quantity=0;
    protected int promotion=0;
    protected double totalAmount=0;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Order() {
        super();
    }

    public void setId(int id) {
        int oldId = this.id;
        this.id = id;
        propertyChangeSupport.firePropertyChange("id", oldId, id);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public int getId() {
        return id;
    }

    public void setDate(Date date) {
        Date oldDate = this.date;
        this.date = date;
        propertyChangeSupport.firePropertyChange("date", oldDate, date);
    }

    public Date getDate() {
        return date;
    }

    public void setStatus(String status) {
        String oldStatus = this.status;
        this.status = status;
        propertyChangeSupport.firePropertyChange("status", oldStatus, status);
    }

    public String getStatus() {
        return status;
    }

    public void setPaymentTpe(String paymentTpe) {
        String oldPaymentTpe = this.paymentTpe;
        this.paymentTpe = paymentTpe;
        propertyChangeSupport.firePropertyChange("paymentTpe", oldPaymentTpe, paymentTpe);
    }

    public String getPaymentTpe() {
        return paymentTpe;
    }

    public void setSalesman(String salesman) {
        String oldSalesman = this.salesman;
        this.salesman = salesman;
        propertyChangeSupport.firePropertyChange("salesman", oldSalesman, salesman);
    }

    public String getSalesman() {
        return salesman;
    }

    public void setStoreName(String storeName) {
        String oldStoreName = this.storeName;
        this.storeName = storeName;
        propertyChangeSupport.firePropertyChange("storeName", oldStoreName, storeName);
    }

    public String getStoreName() {
        return storeName;
    }

    public void setQuantity(int quantity) {
        int oldQuantity = this.quantity;
        this.quantity = quantity;
        propertyChangeSupport.firePropertyChange("quantity", oldQuantity, quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPromotion(int promotion) {
        int oldPromotion = this.promotion;
        this.promotion = promotion;
        propertyChangeSupport.firePropertyChange("promotion", oldPromotion, promotion);
    }

    public int getPromotion() {
        return promotion;
    }

    public void setTotalAmount(double totalAmount) {
        double oldTotalAmount = this.totalAmount;
        this.totalAmount = totalAmount;
        propertyChangeSupport.firePropertyChange("totalAmount", oldTotalAmount, totalAmount);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setImagePath(String imagePath) {
        String oldImagePath = this.imagePath;
        this.imagePath = imagePath;
        propertyChangeSupport.firePropertyChange("imagePath", oldImagePath, imagePath);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setProductName(String productName) {
        String oldProductName = this.productName;
        this.productName = productName;
        propertyChangeSupport.firePropertyChange("productName", oldProductName, productName);
    }

    public String getProductName() {
        return productName;
    }

    public void setPrice(double price) {
        double oldPrice = this.price;
        this.price = price;
        propertyChangeSupport.firePropertyChange("price", oldPrice, price);
    }

    public double getPrice() {
        return price;
    }

    public void setPropertyChangeSupport(PropertyChangeSupport propertyChangeSupport) {
        PropertyChangeSupport oldPropertyChangeSupport = this.propertyChangeSupport;
        this.propertyChangeSupport = propertyChangeSupport;
        propertyChangeSupport.firePropertyChange("propertyChangeSupport", oldPropertyChangeSupport, propertyChangeSupport);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
}
