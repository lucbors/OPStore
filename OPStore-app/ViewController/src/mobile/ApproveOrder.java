package mobile;

import java.text.DecimalFormat;

import java.util.Calendar;
import java.util.Date;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.amx.event.ValueChangeEvent;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.exception.AdfException;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
/*
 * Copyright � AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class ApproveOrder {
    private Date orderDate = null;
    private Date expecteDeliveryDate = null;
    private Date chequeDate = null;
    private int paymentTypeEnum = 0;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String cardNumber="";
    private String CVV="";
    private String bank="";
    private String chequeNum="";
    private Date expiryDate = null;
    private boolean showCheque=false;


    private String signature="";
    
    public ApproveOrder() {
        setOrderDate(new Date());
        chequeDate = new Date();
    }

    public String SendOrderToFormAndValidate() {
        
        //validate that the fields are not empty
        boolean validatePayment=false;
        String validationErrorMessage = "";
  
       switch(paymentTypeEnum){
        //credit
        case 0: validatePayment = ((cardNumber.length()>0)&&(CVV.length()>0)&&(expiryDate!=null));
                validationErrorMessage = "Please Enter valid credit card details";
                break;
        //cash
        case 1:validatePayment=true;break;

        //Cheque
        case 2: validatePayment = ((bank.length()>0)&&(chequeNum.length()>0)&&(chequeDate!=null));
                validationErrorMessage = "Please Enter valid cheque details";
                break;

        }
        
        if (validatePayment==false)
        {
            String title = "Payment Details Validation";
                    
            AdfmfContainerUtilities.invokeContainerJavaScriptFunction("Logon", 
                  "navigator.notification.alert", new Object[] {validationErrorMessage,null,title, "Ok"});

            return null;
        }
        
        
        ServicesWrapper srvwrp = new ServicesWrapper();
        
        //send to Oracle Forms Server
        Store selectedStore = srvwrp.getSelectedStore();
        int StoreID = selectedStore.getId();
        double totalOrder = srvwrp.getConfirmedOrdersTotalAmount();
        DecimalFormat df = new DecimalFormat("#.00");
        String totalOrderStr = df.format(totalOrder);


        boolean response = srvwrp.submitOrderToFormsSystem(StoreID, orderDate, totalOrderStr, getPaymentTypeString());
        if (response){
            return "confirm";
        }
        else {
            String title = "Order Confirmatoin Failed";
            String errorMessage = srvwrp.getSubmitOrderErrorMessage();
            
            AdfmfContainerUtilities.invokeContainerJavaScriptFunction("Logon", 
                     "navigator.notification.alert", new Object[] {errorMessage,null,title, "Ok"});
        }
        return null;
    }

    public void setOrderDate(Date orderDate) {
        Date oldOrderDate = this.orderDate;
        this.orderDate = orderDate;
        Calendar c = Calendar.getInstance();
        c.setTime(orderDate);
        c.add(Calendar.DATE, 15);
        setExpecteDeliveryDate(c.getTime());
        
        propertyChangeSupport.firePropertyChange("orderDate", oldOrderDate, orderDate);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setExpecteDeliveryDate(Date expecteDeliveryDate) {
        Date oldExpecteDeliveryDate = this.expecteDeliveryDate;
        this.expecteDeliveryDate = expecteDeliveryDate;
        propertyChangeSupport.firePropertyChange("expecteDeliveryDate", oldExpecteDeliveryDate, expecteDeliveryDate);
    }

    public Date getExpecteDeliveryDate() {
        return expecteDeliveryDate;
    }

    protected String getPaymentTypeString() {
        switch(paymentTypeEnum){
        case 0: return "CREDIT"; 
        case 1: return "CASH";
        case 2: return "CHECK";
        }
        return "CREDIT";
    }

    public void setChequeDate(Date chequeDate) {
        Date oldChequeDate = this.chequeDate;
        this.chequeDate = chequeDate;
        propertyChangeSupport.firePropertyChange("chequeDate", oldChequeDate, chequeDate);
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getCVV() {
        return CVV;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBank() {
        return bank;
    }

    public void setChequeNum(String chequeNum) {
        this.chequeNum = chequeNum;
    }

    public String getChequeNum() {
        return chequeNum;
    }

    public void setExpiryDate(Date expiryDate) {
        Date oldExpiryDate = this.expiryDate;
        this.expiryDate = expiryDate;
        propertyChangeSupport.firePropertyChange("expiryDate", oldExpiryDate, expiryDate);
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void doClear(ActionEvent actionEvent) {
     
        
        //obsolete

    }

    public void ReciptTypePressed(ValueChangeEvent valueChangeEvent) {
        
        String value = (String)valueChangeEvent.getNewValue();
        String message = "";    
        String title = "Sending Receipt Process";
        if (value.equalsIgnoreCase("email")) {
            //send receipt to email.
            message = "Receipt would be sent to your email";
        }
        else if (value.equalsIgnoreCase("print")) {
            //send receipt to printer.
            message = "No printer was found";
        }
        else if (value.equalsIgnoreCase("sms")) {
            //send receipt to sms.
            message = "Receipt would be sent to your phone";
        }
        
       
 //       AdfmfContainerUtilities.invokeContainerJavaScriptFunction("Logon",
 //       "function(){navigator.notification.alert(message,  function(){}, 'Information', 'OK');}", new Object[] { });
       
      
        
        AdfmfContainerUtilities.invokeContainerJavaScriptFunction("Logon", 
                 "navigator.notification.alert", new Object[] {message,null,title, "Ok"});

    }

    public void setPaymentTypeEnum(int paymentTypeEnum) {
        int oldPaymentTypeEnum = this.paymentTypeEnum;
        this.paymentTypeEnum = paymentTypeEnum;
        
        setShowCheque(this.paymentTypeEnum==2);
        propertyChangeSupport.firePropertyChange("paymentTypeEnum", oldPaymentTypeEnum, paymentTypeEnum);
    }

    public int getPaymentTypeEnum() {
        return paymentTypeEnum;
    }


    public void setSignature(String signature) {
        System.out.println("new signature = " + signature);
        String oldSignature = this.signature;
        this.signature = signature;
        propertyChangeSupport.firePropertyChange("signature", oldSignature, signature);
    }

    public String getSignature() {
        return signature;
    }


    public void setPropertyChangeSupport(PropertyChangeSupport propertyChangeSupport) {
        PropertyChangeSupport oldPropertyChangeSupport = this.propertyChangeSupport;
        this.propertyChangeSupport = propertyChangeSupport;
        propertyChangeSupport.firePropertyChange("propertyChangeSupport", oldPropertyChangeSupport, propertyChangeSupport);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public void setShowCheque(boolean showCheque) {
        boolean oldShowCheque = this.showCheque;
        this.showCheque = showCheque;
        propertyChangeSupport.firePropertyChange("showCheque", oldShowCheque, showCheque);
    }

    public boolean isShowCheque() {
        return showCheque;
    }
}
