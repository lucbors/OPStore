package mobile;

import java.util.ArrayList;
import java.util.Date;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class Store {
    protected static final String NO_PIC = "/images/missingEmployee_120.png"; 
    protected String name="";
    protected int id=0;
    protected String imagePath = NO_PIC;
    protected String address="";
    protected String city="";
    protected String state="";
    protected String zipCode="";
    protected String contactName="";
    protected String phoneNumber="";
    protected String email="";
    protected String yearOfOrigin="";
    protected String incomeLevel="";
    protected String credit="";
    protected long creditLimit=0;
    protected long lastOrderTotal=0;
    protected long lastYearTotal;
    protected long monthlyAverage;
    protected Date lastOrderDate;
    protected OrderList ordersList;
    protected ArrayList orderHistoryList;
    protected boolean filter = false;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Store() {
        super();
        ordersList = new OrderList();
        orderHistoryList = new ArrayList();
    }
    
    public String getFullAddress(){
        return (address+", "+city+", "+state);
    }
    
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", oldName, name);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        int oldId = this.id;
        this.id = id;
        propertyChangeSupport.firePropertyChange("id", oldId, id);
    }

    public int getId() {
        return id;
    }

    public void setImagePath(String imagePath) {
        String oldImagePath = this.imagePath;
        this.imagePath = imagePath;
        propertyChangeSupport.firePropertyChange("imagePath", oldImagePath, imagePath);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setAddress(String address) {
        String oldAddress = this.address;
        this.address = address;
        propertyChangeSupport.firePropertyChange("address", oldAddress, address);
    }

    public String getAddress() {
        return address;
    }

    public void setState(String state) {
        String oldState = this.state;
        this.state = state;
        propertyChangeSupport.firePropertyChange("state", oldState, state);
    }

    public String getState() {
        return state;
    }

    public void setZipCode(String zipCode) {
        String oldZipCode = this.zipCode;
        this.zipCode = zipCode;
        propertyChangeSupport.firePropertyChange("zipCode", oldZipCode, zipCode);
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setContactName(String contactName) {
        String oldContactName = this.contactName;
        this.contactName = contactName;
        propertyChangeSupport.firePropertyChange("contactName", oldContactName, contactName);
    }

    public String getContactName() {
        return contactName;
    }

    public void setPhoneNumber(String phoneNumber) {
        String oldPhoneNumber = this.phoneNumber;
        this.phoneNumber = phoneNumber;
        propertyChangeSupport.firePropertyChange("phoneNumber", oldPhoneNumber, phoneNumber);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        propertyChangeSupport.firePropertyChange("email", oldEmail, email);
    }

    public String getEmail() {
        return email;
    }

    public void setYearOfOrigin(String yearOfOrigin) {
        String oldYearOfOrigin = this.yearOfOrigin;
        this.yearOfOrigin = yearOfOrigin;
        propertyChangeSupport.firePropertyChange("yearOfOrigin", oldYearOfOrigin, yearOfOrigin);
    }

    public String getYearOfOrigin() {
        return yearOfOrigin;
    }

    public void setIncomeLevel(String incomeLevel) {
        String oldIncomeLevel = this.incomeLevel;
        this.incomeLevel = incomeLevel;
        propertyChangeSupport.firePropertyChange("incomeLevel", oldIncomeLevel, incomeLevel);
    }

    public String getIncomeLevel() {
        return incomeLevel;
    }

    public void setCreditLimit(long creditLimit) {
        long oldCreditLimit = this.creditLimit;
        this.creditLimit = creditLimit;
        propertyChangeSupport.firePropertyChange("creditLimit", oldCreditLimit, creditLimit);
    }

    public long getCreditLimit() {
        return creditLimit;
    }

    public void setLastYearTotal(long lastYearTotal) {
        long oldLastYearTotal = this.lastYearTotal;
        this.lastYearTotal = lastYearTotal;
        propertyChangeSupport.firePropertyChange("lastYearTotal", oldLastYearTotal, lastYearTotal);
    }

    public long getLastYearTotal() {
        return lastYearTotal;
    }

    public void setMonthlyAverage(long monthlyAverage) {
        long oldMonthlyAverage = this.monthlyAverage;
        this.monthlyAverage = monthlyAverage;
        propertyChangeSupport.firePropertyChange("monthlyAverage", oldMonthlyAverage, monthlyAverage);
    }

    public long getMonthlyAverage() {
        return monthlyAverage;
    }

    public void setLastOrderDate(Date lastOrderDate) {
        Date oldLastOrderDate = this.lastOrderDate;
        this.lastOrderDate = lastOrderDate;
        propertyChangeSupport.firePropertyChange("lastOrderDate", oldLastOrderDate, lastOrderDate);
    }

    public Date getLastOrderDate() {
        return lastOrderDate;
    }

    public void setCity(String city) {
        String oldCity = this.city;
        this.city = city;
        propertyChangeSupport.firePropertyChange("city", oldCity, city);
    }

    public String getCity() {
        return city;
    }

    public void setLastOrderTotal(long lastOrderTotal) {
        long oldLastOrderTotal = this.lastOrderTotal;
        this.lastOrderTotal = lastOrderTotal;
        propertyChangeSupport.firePropertyChange("lastOrderTotal", oldLastOrderTotal, lastOrderTotal);
    }

    public long getLastOrderTotal() {
        return lastOrderTotal;
    }

    public void setPropertyChangeSupport(PropertyChangeSupport propertyChangeSupport) {
        PropertyChangeSupport oldPropertyChangeSupport = this.propertyChangeSupport;
        this.propertyChangeSupport = propertyChangeSupport;
        propertyChangeSupport.firePropertyChange("propertyChangeSupport", oldPropertyChangeSupport, propertyChangeSupport);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public void initOrderList() {
        Product[] inventoryProducts = EditInventory.getFullProductList();
        
        for (int index=0; index<inventoryProducts.length; index++)
        {
            Product product = inventoryProducts[index];
            int quantity = (product.getLastOrderQuantity() - product.getInventoryQuantity());
            if (quantity<0)
                quantity = 0;
            OrderItem item = new OrderItem(product, quantity);
            ordersList.addOrder(item);
        }
    }

    public void setOrdersList(OrderList ordersList) {
        OrderList oldOrdersList = this.ordersList;
        this.ordersList = ordersList;
        propertyChangeSupport.firePropertyChange("ordersList", oldOrdersList, ordersList);
    }

    public OrderList getOrdersList() {
        return ordersList;
    }
    
    public OrderList getOrdersListAccordingToLastOrder() {
        OrderItem[] orders = ordersList.getOrders();
        OrderList lastOrderList = new OrderList();
        
        for (int index=0;index<orders.length;index++) {
            OrderItem order = orders[index];
            if (order.getProduct().getLastOrderQuantity()!=0)
                lastOrderList.addOrder(order);
        }
        return lastOrderList;
    }
    
    public OrderList getRecommendedOrdersList() {
        OrderItem[] orders = ordersList.getOrders();
        OrderList recommendedOrderItems = new OrderList();
        
        for (int index=0;index<orders.length;index++) {
            OrderItem order = orders[index];
            if (order.getProduct().getRecommended()>0  && order.getProduct().getLastOrderQuantity()==0)
                recommendedOrderItems.addOrder(order);
        }
        return recommendedOrderItems;
    }
    
    public OrderList getBestSellerOrdersList() {
        OrderItem[] orders = ordersList.getOrders();
        OrderList bestSellerOrderItems = new OrderList();
        
        for (int index=0;index<orders.length;index++) {
            OrderItem order = orders[index];
            if (order.getProduct().getBestSeller()>0 && order.getProduct().getLastOrderQuantity()==0)
                bestSellerOrderItems.addOrder(order);
        }
        return bestSellerOrderItems;
    }

    public OrderList getConfirmedOrdersList() {
        OrderItem[] orders = ordersList.getOrders();
        OrderList confirmedOrderItems = new OrderList();
        
        for (int index=0;index<orders.length;index++) {
            OrderItem order = orders[index];
            if (order.getQuantity()>0)
                confirmedOrderItems.addOrder(order);
        }
        return confirmedOrderItems;
    }

    public void setOrderHistoryList(ArrayList orderHistoryList) {
        ArrayList oldOrderHistoryList = this.orderHistoryList;
        this.orderHistoryList = orderHistoryList;
        propertyChangeSupport.firePropertyChange("orderHistoryList", oldOrderHistoryList, orderHistoryList);
    }

    public ArrayList getOrderHistoryList() {
        return orderHistoryList;
    }
    
    public OrderHistoryItem[] getOrderHistory() {
        OrderHistoryItem s[] = null;

        s = (OrderHistoryItem[])orderHistoryList.toArray(new OrderHistoryItem[orderHistoryList.size()]);

        return s;
    }

    public void setFilter(boolean filter) {
        boolean oldFilter = this.filter;
        this.filter = filter;
        propertyChangeSupport.firePropertyChange("filter", oldFilter, filter);
    }

    public boolean isFilter() {
        return filter;
    }

    public void setCredit(String credit) {
        String oldCredit = this.credit;
        this.credit = credit;
        propertyChangeSupport.firePropertyChange("credit", oldCredit, credit);
    }

    public String getCredit() {
        return credit;
    }
}
