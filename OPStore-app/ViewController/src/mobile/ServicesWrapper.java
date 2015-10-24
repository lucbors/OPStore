package mobile;

import java.text.DateFormat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.el.ValueExpression;

import oracle.adfmf.dc.ws.soap.SoapGenericType;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.exception.AdfException;
import oracle.adfmf.framework.exception.AdfInvocationException;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;
import oracle.adfmf.util.GenericType;

/*
 * Copyright � AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class ServicesWrapper 
{    
    private static String username = "";
    private static String password = "";
    private  boolean submitOrderSucces=false;
    private static boolean offlineMode=false;
    private  String submitOrderErrorMessage = "";
    private  String submitOrderConfirmationNumber="";
    private static boolean getStoresListByStateSucces = false;
    private static String getStoresListByStateErrorMessage = "";
    
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


    public ServicesWrapper() {
        super();
    
        }

    public OrderItem[] getOrdersList() {
        //return getSelectedStore().getOrdersList().getOrders();        
        return getSelectedStore().getOrdersListAccordingToLastOrder().getOrders();        
    }
    
    public static OrderItem[] getConfirmedOrdersList() {
        return getSelectedStore().getConfirmedOrdersList().getOrders();        
    }

    public static double getConfirmedOrdersTotalAmount() {
        double total = 0;
        OrderItem[] orderItems = getSelectedStore().getConfirmedOrdersList().getOrders();
        for (int index=0;index<orderItems.length;index++) {
            total += (orderItems[index].getQuantity()*orderItems[index].getPaidPrice());
        }
        return total;
    }
    public static String getConfirmedOrdersTotalAmountAsString() {
        double total = getConfirmedOrdersTotalAmount();
        DecimalFormat df = new DecimalFormat("#.00");
        String totalOrderStr = "$"+df.format(total);
        return totalOrderStr;
    }
    
    public static int getConfirmedOrdersItemsSize() {
        OrderItem[] orderItems = getSelectedStore().getConfirmedOrdersList().getOrders();
        return orderItems.length;
    }
    
    public static OrderItem[] getRecommendedOrdersList() {
        return getSelectedStore().getRecommendedOrdersList().getOrders();        
    }
    
    public static OrderItem[] getBestSellerOrdersList() {
        return getSelectedStore().getBestSellerOrdersList().getOrders();        
    }
/*    
    public static Product[] getRecommendedProductsList() {
        return EditInventory.getRecommendedProductList();
        
    }

    public static Product[] getBestSellerProductsList() {
        return EditInventory.getBestSellerProductList();
        
    }
*/
    public Store[] getStoresList() {
        return StoreLocator.getFilteredStoresList();        
    }

    public static Store getSelectedStore(){
        return StoreLocator.s_getSelectedStore();
    }

    public Store getStoreByIndex(int index){
        try{
        StoreLocator.s_setSelectedStoreIndex(index);
        return StoreLocator.s_getSelectedStore();
        }catch(Exception e) {
            e.printStackTrace();
            return StoreLocator.getFilteredStoresList()[0];
    }
    }

    public OrderHistoryItem[] getOrderHistoryOfStoreByIndex(int index){
        try{
        StoreLocator.s_setSelectedStoreIndex(index);
        Store store = StoreLocator.s_getSelectedStore();
        return store.getOrderHistory();
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public  boolean submitOrderToFormsSystem(int StoreID, Date orderDate, String totalOrder, String paymentType) {
        
       setSubmitOrderSucces ( false);
        setSubmitOrderConfirmationNumber( "-1");
        setSubmitOrderErrorMessage ("");
        
        if (offlineMode)
        {
            setSubmitOrderSucces ( true );                
            setSubmitOrderConfirmationNumber("00000");
            return true;
        }

        List parameterNames = new ArrayList();
        List parameters = new ArrayList();
        List parameterTypes = new ArrayList();
        
        parameterNames.add("MAIN_USERNAME_0");
        parameterNames.add("MAIN_PASSWORD_0");
        parameterNames.add("S_CUSTOMER_ID_0");
        parameterNames.add("S_ORD_DATE_ORDERED_1");
        parameterNames.add("S_ORD_SALES_REP_ID_1");
        parameterNames.add("S_ORD_TOTAL_1");
        parameterNames.add("S_ORD_PAYMENT_TYPE_1");

        String StoreIdString = String.valueOf(StoreID);
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formatedDate = df.format(orderDate);
        
        parameters.add("mia");
        parameters.add("oracle");
        parameters.add(StoreIdString);
        parameters.add(formatedDate);
        parameters.add("10");
        parameters.add(totalOrder);
        parameters.add(paymentType);
            
        parameterTypes.add(String.class);
        parameterTypes.add(String.class);
        parameterTypes.add(String.class);
        parameterTypes.add(String.class);
        parameterTypes.add(String.class);
        parameterTypes.add(String.class);
        parameterTypes.add(String.class);
        
        try {           
            /*
             * Expected response: 
             * <?xml version="1.0" encoding="UTF-8" ?>
                    <macroreply>
                        <S_ORD_ID_0>null</S_ORD_ID_0>
                        <S_ORD_ID_1>4720</S_ORD_ID_1>
                        <PopupMessages></PopupMessages>
                        <StatusBarMessages>Enter a query;  press Ctrl+F11 to execute, F4 to cancel.;FRM-40400: Transaction complete: 1 records applied and saved.;</StatusBarMessages>
                        <Error/>
                    </macroreply>

                response validation make sure: 
                    1. StatusBarMessages contains: the string: FRM-40400: Transaction complete: 1 records applied and saved.
                    2. we need to get the order id: S_ORD_ID_1
             */

            GenericType result = (GenericType)AdfmfJavaUtilities.invokeDataControlMethod("CreateNewOrder", null, "s_createNewOrder2",
                                                                parameterNames, parameters, parameterTypes);            
            
            SoapGenericType soapResponse = (SoapGenericType)result.getAttribute(0);
            SoapGenericType soapResultObject = (SoapGenericType)result.getAttribute(1);
            
            String popupMessage = (String)soapResponse.getAttribute(1);//"PopupMessages"            
            String status = (String)soapResponse.getAttribute(2);//"StatusBarMessage"
            
            if (popupMessage.length()>0){
               SoapGenericType elementArray = (SoapGenericType)soapResponse.getAttribute(1);
            }
            String orderID = (String)soapResultObject.getAttribute(0);

            
            if (status.indexOf("FRM-40400: Transaction complete: 1 records applied and saved")>=0){
                setSubmitOrderSucces(true);                
                setSubmitOrderConfirmationNumber(orderID);
            }
            else {
                if (popupMessage.length()>0){
                    setSubmitOrderErrorMessage (popupMessage.substring(0, (popupMessage.length()-1)));                    
                }else{                    
                    setSubmitOrderErrorMessage(status);
                }
            }

        } catch (AdfInvocationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSubmitOrderSucces();        

    }
        
    
    public  boolean getStoresListByStateWS(String state) {

        if (offlineMode)
            return true;
        //StoreLocator.clearStoreList();
        ServicesWrapper.getStoresListByStateSucces = false;
        ServicesWrapper.getStoresListByStateErrorMessage = "";
        
        List parameterNames = new ArrayList();
        List parameters = new ArrayList();
        List parameterTypes = new ArrayList();
        
        parameterNames.add("MAIN_USERNAME_0");
        parameterNames.add("MAIN_PASSWORD_0");
        parameterNames.add("S_CUSTOMER_STATE_0");

        parameters.add("mia");
        parameters.add("oracle");
        parameters.add(state);
            
        parameterTypes.add(String.class);
        parameterTypes.add(String.class);
        parameterTypes.add(String.class);
        
        try {           

            GenericType result = (GenericType)AdfmfJavaUtilities.invokeDataControlMethod("getCustomersByState", null, "y_SearchCustomerState",
                                                                parameterNames, parameters, parameterTypes);            
            

            SoapGenericType soapResponse = (SoapGenericType)result.getAttribute(0);
           

            String status = (String)soapResponse.getAttribute(2);//"StatusBarMessage"
   
            SoapGenericType soapResultStoreList = (SoapGenericType)result.getAttribute(2);
            
            int listSize = soapResultStoreList.getAttributeCount();
            for (int index=0;index<listSize;index++) 
            {
                SoapGenericType item = (SoapGenericType)soapResultStoreList.getAttribute(index); 
                String storeID = (String)item.getAttribute(0);
                int id = Integer.parseInt(storeID);
                String storeName = (String)item.getAttribute(1);
                String storePhone = (String)item.getAttribute(2);
                String storeAddress = (String)item.getAttribute(3);
                String storeCity = (String)item.getAttribute(4);
                //String storeCountry = (String)item.getAttribute(5);
                String storeZipCode = (String)item.getAttribute(6);
                String storeCredit = (String)item.getAttribute(7);
                
                Store store = StoreLocator.s_getStoreByID(id);
                if (store==null)
                {
                    store = new Store();
                    StoreLocator.addStore(store);
                }
                store.setId(id);
                store.setName(storeName);
                store.setPhoneNumber(storePhone);
                store.setAddress(storeAddress);
                store.setCity(storeCity);
                store.setZipCode(storeZipCode);
                store.setCredit(storeCredit);
                
            }
            setSubmitOrderErrorMessage( status);
            
            
            //if (status.indexOf("FRM-40400: Transaction complete: 1 records applied and saved")>=0){
            //    ServicesWrapper.submitOrderSucces = true;                
            //    ServicesWrapper.submitOrderConfirmationNumber = orderID;
            //}
            //else {
            //        ServicesWrapper.submitOrderErrorMessage = status;
            //}

        } catch (AdfInvocationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSubmitOrderSucces();        

        //return true;
    }
       
       
    public static String Logon(String userName, String passWord) {        

        String response = "";
        try
        {
            ServicesWrapper.username = userName;
            ServicesWrapper.password = passWord.toUpperCase();

            if ((username.length()>0) && password.length()>0)
            {
                if (username.equalsIgnoreCase("offline") && password.equalsIgnoreCase("offline")) {
                    offlineMode = true;
                    String title = "Logon offline";
                    String errorMessage = "You are in offline mode";
     
                    AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
                                             AdfmfJavaUtilities.getFeatureName(),
                                             "adf.mf.api.amx.addMessage",
                                             new Object[] {AdfException.ERROR,
                                             errorMessage,
                                             null,
                                             null });
     //               AdfmfContainerUtilities.invokeContainerJavaScriptFunction("Logon", 
     //                        "navigator.notification.alert", new Object[] {errorMessage,null,title, "Ok"});

                }
                return "success";
            }
            else {
                String title = "Logon Failed";
                String errorMessage = "Please enter a valid username and password";
                
                AdfmfContainerUtilities.invokeContainerJavaScriptFunction("Logon", 
                         "navigator.notification.alert", new Object[] {errorMessage,null,title, "Ok"});
                
                return "fail";
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            return "fail";
        }
        //return "success";

    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }


   


    public void setSubmitOrderSucces(boolean submitOrderSucces) {
        boolean oldSubmitOrderSucces = this.submitOrderSucces;
        this.submitOrderSucces = submitOrderSucces;
        propertyChangeSupport.firePropertyChange("submitOrderSucces", oldSubmitOrderSucces, submitOrderSucces);
    }

    public  boolean isSubmitOrderSucces() {
        return submitOrderSucces;
    }

    public void setSubmitOrderConfirmationNumber(String submitOrderConfirmationNumber) {
        String oldSubmitOrderConfirmationNumber = this.submitOrderConfirmationNumber;
        this.submitOrderConfirmationNumber = submitOrderConfirmationNumber;
        propertyChangeSupport.firePropertyChange("submitOrderConfirmationNumber", oldSubmitOrderConfirmationNumber, submitOrderConfirmationNumber);
   
   //Luc Bors Quick and Dirty
   ValueExpression ve = AdfmfJavaUtilities.getValueExpression("#{pageFlowScope.orderConfirmationNumber}", String.class);
   ve.setValue(AdfmfJavaUtilities.getAdfELContext(), submitOrderConfirmationNumber);

   
    }

    public String getSubmitOrderConfirmationNumber() {
        return submitOrderConfirmationNumber;
    }

    public void setSubmitOrderErrorMessage(String submitOrderErrorMessage) {
        String oldSubmitOrderErrorMessage = this.submitOrderErrorMessage;
        this.submitOrderErrorMessage = submitOrderErrorMessage;
        propertyChangeSupport.firePropertyChange("submitOrderErrorMessage", oldSubmitOrderErrorMessage, submitOrderErrorMessage);

        //Luc Bors Quick and Dirty
        ValueExpression ve = AdfmfJavaUtilities.getValueExpression("#{pageFlowScope.orderErrorMessage}", String.class);
        ve.setValue(AdfmfJavaUtilities.getAdfELContext(), submitOrderErrorMessage);


    }

    public  String getSubmitOrderErrorMessage() {
        return submitOrderErrorMessage;
    }
    
    public  String getStoreSearchFilter() {
        return StoreLocator.getCurrentFilter();
    }
        }
