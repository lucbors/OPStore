package mobile;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class Product {
    protected static final String NO_PIC = "/images/missingEmployee_120.png"; 
    protected int prodID = 0;
    protected String imagePath = NO_PIC;
    protected String name = "";
    protected String description="";
    protected double listPrice = 0.0;
    protected String category = "";
    protected String subCategory = "";
    protected int bestSeller = 0;
    protected int recommended = 0;
    protected double salePrice = 0.0;
    protected int inventoryQuantity=0;
    protected int lastOrderQuantity=0;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Product() {
        super();
    }

    public void setProdID(int prodID) {
        int oldProdID = this.prodID;
        this.prodID = prodID;
        propertyChangeSupport.firePropertyChange("prodID", oldProdID, prodID);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public int getProdID() {
        return prodID;
    }

   public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", oldName, name);
    }

    public String getName() {
        return name;
    }

    public void setListPrice(double listPrice) {
        double oldListPrice = this.listPrice;
        this.listPrice = listPrice;
        propertyChangeSupport.firePropertyChange("listPrice", oldListPrice, listPrice);
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setCategory(String category) {
        String oldCategory = this.category;
        this.category = category;
        propertyChangeSupport.firePropertyChange("category", oldCategory, category);
    }

    public String getCategory() {
        return category;
    }

    public void setSubCategory(String subCategory) {
        String oldSubCategory = this.subCategory;
        this.subCategory = subCategory;
        propertyChangeSupport.firePropertyChange("subCategory", oldSubCategory, subCategory);
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setBestSeller(int bestSeller) {
        int oldBestSeller = this.bestSeller;
        this.bestSeller = bestSeller;
        propertyChangeSupport.firePropertyChange("bestSeller", oldBestSeller, bestSeller);
    }

    public int getBestSeller() {
        return bestSeller;
    }

    public void setRecommended(int recommended) {
        int oldRecommended = this.recommended;
        this.recommended = recommended;
        propertyChangeSupport.firePropertyChange("recommended", oldRecommended, recommended);
    }

    public int getRecommended() {
        return recommended;
    }

    public void setSalePrice(double salePrice) {
        double oldSalePrice = this.salePrice;
        this.salePrice = salePrice;
        propertyChangeSupport.firePropertyChange("salePrice", oldSalePrice, salePrice);
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setImagePath(String imagePath) {
        String oldImagePath = this.imagePath;
        this.imagePath = imagePath;
        propertyChangeSupport.firePropertyChange("imagePath", oldImagePath, imagePath);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setDescription(String description) {
        String oldDescription = this.description;
        this.description = description;
        propertyChangeSupport.firePropertyChange("description", oldDescription, description);
    }

    public String getDescription() {
        return description;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        int oldInventoryQuantity = this.inventoryQuantity;
        this.inventoryQuantity = inventoryQuantity;
        propertyChangeSupport.firePropertyChange("inventoryQuantity", oldInventoryQuantity, inventoryQuantity);
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setLastOrderQuantity(int lastOrderQuantity) {
        int oldLastOrderQuantity = this.lastOrderQuantity;
        this.lastOrderQuantity = lastOrderQuantity;
        propertyChangeSupport.firePropertyChange("lastOrderQuantity", oldLastOrderQuantity, lastOrderQuantity);
    }

    public int getLastOrderQuantity() {
        return lastOrderQuantity;
    }
    public void incrementInventory() {
        int inventory = this.getInventoryQuantity();
        this.setInventoryQuantity(++inventory);
    }
    public void decrementInventory() {
        int inventory = this.getInventoryQuantity();
        this.setInventoryQuantity(--inventory);
    }
}
