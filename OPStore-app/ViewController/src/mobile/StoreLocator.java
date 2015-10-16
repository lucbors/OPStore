package mobile;

import java.util.ArrayList;

import java.util.Iterator;

import javax.el.ValueExpression;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.amx.event.SelectionEvent;
import oracle.adfmf.amx.event.ValueChangeEvent;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

/*
 * Copyright � AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class StoreLocator {
    
    private static int searchBySelection = 0;
    private static int selectedStoreIndex = 0;
    private static StoreList s_fullStoresList = null;
    private static String s_currentFilter="";
    public static boolean searchOn = false;
 
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    public StoreLocator() {
        init();
        searchBySelection = 0;
    }
    public static void init(){
        if (s_fullStoresList==null)
        {
            ServicesWrapper srvwr = new ServicesWrapper();
            s_fullStoresList = new StoreList();
            s_fullStoresList.setStoresDummy();
            srvwr.getStoresListByStateWS("CA");
        }
    }

    public static void clearStoreList()
    {
        s_fullStoresList.clear();
    }
    
    public static void addStore(Store store){
        s_fullStoresList.add(store);
    }
    
    public void setSearchBySelection(int searchBySelection) {
        int oldSearchBySelection = this.searchBySelection;
        this.searchBySelection = searchBySelection;
        propertyChangeSupport.firePropertyChange("searchBySelection", oldSearchBySelection, searchBySelection);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public int getSearchBySelection() {
        return searchBySelection;
    }
    /*
    public void selectStoreByID(int storeID) {
        int oldstoreID = this.selectedStoreID;
        this.selectedStoreID = storeID;
        propertyChangeSupport.firePropertyChange("selectStore", oldstoreID, selectedStoreID);
    }
    */
    public Store getSelectedStore() {
        try{
            return s_fullStoresList.getStoreById(selectedStoreIndex);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Store s_getStoreByID(int id) {
        try{
            getFilteredStoresList();
            return s_fullStoresList.getStoreById(id);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Store s_getSelectedStore() {
        try{
            return s_getStoreByID(selectedStoreIndex);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String SelectStore() {
        int storeIndex = 0;
        try{
        ValueExpression ve = AdfmfJavaUtilities.getValueExpression("#{pageFlowScope.StoreIndex}", Integer.class);
        Integer rowIndexObj = (Integer)ve.getValue(AdfmfJavaUtilities.getAdfELContext());
        storeIndex = rowIndexObj.intValue();
        }catch(Exception e){
            e.printStackTrace();
        }
        int oldStoreIndex = this.selectedStoreIndex;
        this.selectedStoreIndex = storeIndex;
        searchBySelection = 0;
        return "refresh";
    }

    public static Store[] getFilteredStoresList() {
        try{
            if (s_fullStoresList==null)
            {                
                init();
            }

            return s_fullStoresList.getStores();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void setCurrentFilter(String s_currentFilter) {
        String oldCurrentFilter = StoreLocator.s_currentFilter;
        StoreLocator.s_currentFilter = s_currentFilter;
        propertyChangeSupport.firePropertyChange("currentFilter", oldCurrentFilter, s_currentFilter);
    }

    public static String getCurrentFilter() {
        return s_currentFilter;
    }

    public String FilterStoreByName() {
        try{
            if (s_currentFilter != null  && s_currentFilter.length()>0) {
                String filter = s_currentFilter.toLowerCase();
                ArrayList fullStoresList = s_fullStoresList.getStoresArrayList();
                int count = fullStoresList.size();
                for (int index=0;index<count;index++) 
                {
                    Store store = (Store)fullStoresList.get(index);
                    if (store.getName().toLowerCase().startsWith(filter)) {
                        store.setFilter(false);
                    }
                    else{
                        store.setFilter(true);
                    }
                }
            }
            else {
                ArrayList fullStoresList = s_fullStoresList.getStoresArrayList();
                int count = fullStoresList.size();
                for (int index=0;index<count;index++) 
                {
                    Store store = (Store)fullStoresList.get(index);
                    store.setFilter(false);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return "refresh";
    }

    public void setSearchOn(boolean searchOn) {
        boolean oldSearchOn = StoreLocator.searchOn;
        StoreLocator.searchOn = searchOn;
        propertyChangeSupport.firePropertyChange("searchOn", oldSearchOn, searchOn);
    }

    public static boolean isSearchOn() {
        return searchOn;
    }

    public String ResetFilterByName() {
        ArrayList fullStoresList = s_fullStoresList.getStoresArrayList();
        int count = fullStoresList.size();
        for (int index=0;index<count;index++) 
        {
            Store store = (Store)fullStoresList.get(index);
            store.setFilter(false);
        }
        return "refresh";
    }

    public static void s_setSelectedStoreIndex(int selectedStoreIndex) {
        StoreLocator.selectedStoreIndex = selectedStoreIndex;
    }

    public void setSelectedStoreIndex(int selectedStoreIndex) {
        StoreLocator.selectedStoreIndex = selectedStoreIndex;
    }

    public int getSelectedStoreIndex() {
        return selectedStoreIndex;
    }

    public void FilterStoreBynameListener(ActionEvent actionEvent) {
        try{
                FilterStoreByName();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void StoreSearchFilterChanged(ValueChangeEvent valueChangeEvent) {
        String searchFilter = (String)valueChangeEvent.getNewValue();
        setCurrentFilter(searchFilter);
    }

    public String Logon() {
        ValueExpression ve = AdfmfJavaUtilities.getValueExpression("#{bindings.userName.inputValue}", String.class);
        String userName = (String)ve.getValue(AdfmfJavaUtilities.getAdfELContext());
        
        ve = AdfmfJavaUtilities.getValueExpression("#{bindings.passWord.inputValue}", String.class);
        String password = (String)ve.getValue(AdfmfJavaUtilities.getAdfELContext());
        
        if (ServicesWrapper.Logon(userName, password)=="success")
            return "success";
        
        return null;
    }
}
