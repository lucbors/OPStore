package mobile;

import java.util.ArrayList;

import javax.el.ValueExpression;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class EditInventory {
    private static ProductList s_fullInventoryList = null;
    private static ProductList s_filteredInventoryList = null;
    private static String s_currentProductFilter = "";
    private static int s_selectedProductIndexFromFullList = 0;

    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this); //default by location

    public EditInventory() {
        s_fullInventoryList = new ProductList();
        s_fullInventoryList.setProductsDummy();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    protected int getSelectedProductIndex() {
        ValueExpression ve = AdfmfJavaUtilities.getValueExpression("#{pageFlowScope.productIndex}", Integer.class);
        Integer rowIndexObj = (Integer)ve.getValue(AdfmfJavaUtilities.getAdfELContext());
        int productIndex = rowIndexObj.intValue();
        return productIndex;
    }
    
    public String IncrementProductQuantity() {
        int productIndex = getSelectedProductIndex();

        Product clickedProduct = s_filteredInventoryList.getProductByIndex(productIndex);
        int inventoryQuantity = clickedProduct.getInventoryQuantity();
        clickedProduct.setInventoryQuantity((inventoryQuantity+1));

        return "refresh";
    }
    
    public Product getSelectedProductFromFullList() {
        int productIndex = getSelectedProductIndex();

        Product clickedProduct = s_fullInventoryList.getProductByIndex(productIndex);

        return clickedProduct;
    }
    
    public void DecrementProduct(ActionEvent actionEvent) {
        int productIndex = getSelectedProductIndex();
            
        Product clickedProduct = s_filteredInventoryList.getProductByIndex(productIndex);
        int inventoryQuantity = clickedProduct.getInventoryQuantity();
        clickedProduct.setInventoryQuantity((inventoryQuantity-1));
        propertyChangeSupport.firePropertyChange("decrement product", (inventoryQuantity), (inventoryQuantity-1));
        
    }

    public static Product[] getFilteredProductList() {
        
        if (s_filteredInventoryList==null)
        {
            if (s_fullInventoryList!=null)
                s_filteredInventoryList = s_fullInventoryList;
            else {
                s_fullInventoryList = new ProductList();
                s_fullInventoryList.setProductsDummy();
                s_filteredInventoryList = s_fullInventoryList;
            }
        }
        return s_filteredInventoryList.getProducts();
    }

    public static Product[] getFullProductList() {
        
        if (s_fullInventoryList==null)
        {
            s_fullInventoryList = new ProductList();
            s_fullInventoryList.setProductsDummy();
        }
        return s_fullInventoryList.getProducts();
    }

    public void setCurrentProductFilter(String s_currentProductFilter) {
        String oldCurrentProductFilter = EditInventory.s_currentProductFilter;
        EditInventory.s_currentProductFilter = s_currentProductFilter;
        propertyChangeSupport.firePropertyChange("currentProductFilter", oldCurrentProductFilter, s_currentProductFilter);
    }

    public static String getCurrentProductFilter() {
        return s_currentProductFilter;
    }

    public String FilterProductsByNameOrID() {
        if (s_currentProductFilter != null  && s_currentProductFilter.length()>0) {
            String filter = s_currentProductFilter.toLowerCase();
            s_filteredInventoryList = new ProductList();
            ArrayList fullInventoryList = (ArrayList)s_fullInventoryList.getProductsList();
            int count = fullInventoryList.size();
            for (int index=0;index<count;index++) 
            {
                Product product = (Product)fullInventoryList.get(index);
                //contains the name of the product
                if (product.getName().toLowerCase().indexOf(filter)>=0) {
                    s_filteredInventoryList.add(product);
                }
                else {
                    //checkif the search compatible with product ID.
                    String productID = "" +product.getProdID();
                    if (productID.indexOf(filter)>=0) {
                        s_filteredInventoryList.add(product);
                    }
                }
            }
        }
        else {
            s_filteredInventoryList = s_fullInventoryList;
        }
        return "refresh";
    }

    public static Product[] getRecommendedProductList() {
       
        Product[] fullProductList = getFullProductList();
        ProductList recommendedProducts = new ProductList();
        
        for (int index=0;index<fullProductList.length;index++) {
            Product currentProduct = fullProductList[index];
            if (currentProduct.getRecommended()>0)
                recommendedProducts.add(currentProduct);
        }
        return recommendedProducts.getProducts();
    }

    public static Product[] getBestSellerProductList() {
       
        Product[] fullProductList = getFullProductList();
        ProductList bestSellerProducts = new ProductList();
        
        for (int index=0;index<fullProductList.length;index++) {
            Product currentProduct = fullProductList[index];
            if (currentProduct.getBestSeller()>0)
                bestSellerProducts.add(currentProduct);
        }
        return bestSellerProducts.getProducts();
    }
}
