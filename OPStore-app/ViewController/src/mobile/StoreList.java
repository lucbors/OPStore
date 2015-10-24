package mobile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/*
 * Copyright � AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class StoreList {
    private ArrayList s_storess = null;

    public StoreList() {
        if (s_storess == null) {
            s_storess = new ArrayList();
        }
    }

    public boolean setStores() {
        
        return true;
    }
    
    public void clear(){
        s_storess.clear();
    }
    public int size() {
        return s_storess.size();
    }
    
    public boolean setOrderHistoryDummy() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(".adf/META-INF/data_files/order_history.csv");
        BufferedReader bReader = null;
 
        try {
            bReader = new BufferedReader(new InputStreamReader(is));
            String strLine = "";
            while ((strLine = bReader.readLine()) != null)
            {
                //break comma separated line using ";"
                int tokenNumber = 0;
                StringTokenizer st = new StringTokenizer(strLine, ";");
                OrderHistoryItem otderHistory = new OrderHistoryItem();
                String storeName = null;
                while (st.hasMoreTokens())
                {             
                    tokenNumber++;
                    String theToken = st.nextToken();
                    switch (tokenNumber){
                    case 1: otderHistory.setQuarter(theToken);break;
                    case 2: storeName = theToken;break;
                    case 3: double orderAmount = 0; try{orderAmount = Double.parseDouble(theToken);}catch(Exception e){};otderHistory.setOrderAmount(orderAmount);break;
                    }   
                }
                Store store = getStoreByName(storeName);
                if (store!=null) {
                    store.getOrderHistoryList().add(otderHistory);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public boolean setStoresDummy() {
        s_storess.clear();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(".adf/META-INF/data_files/Store_info.csv");
        if (is == null) {
            System.err.println("###### Could not look up : /META-INF/data_files/Store_info.csv");
        }

        BufferedReader bReader = null;
        

        try {
            bReader = new BufferedReader(new InputStreamReader(is));
            String strLine = "";
            while ((strLine = bReader.readLine()) != null)
            {
                //break comma separated line using ";"
                int tokenNumber = 0;
                StringTokenizer st = new StringTokenizer(strLine, ";");
                Store newStore = new Store();
                while (st.hasMoreTokens())
                {             
                    tokenNumber++;
                    String theToken = st.nextToken();
                    switch (tokenNumber){
                    case 1: int id = 0;try{id=Integer.parseInt(theToken);}catch(Exception e){};newStore.setId(id);break;
                    case 2: newStore.setImagePath(theToken);break;
                    case 3: newStore.setName(theToken);break;
                    case 4: newStore.setAddress(theToken);break;
                    case 5: newStore.setCity(theToken);break;
                    case 6: newStore.setState(theToken);break;
                    case 7: newStore.setZipCode(theToken);break;
                    case 8: newStore.setContactName(theToken);break;
                    case 9: newStore.setPhoneNumber(theToken);break;
                    case 10: newStore.setEmail(theToken);break;
                    case 11: newStore.setYearOfOrigin(theToken);break;
                    case 12: newStore.setIncomeLevel(theToken);break;
                    case 13: long creditLimit = 0; try{creditLimit = Long.parseLong(theToken);}catch(Exception e){};newStore.setCreditLimit(creditLimit);break;
                    case 14: double lastOrderTotal = 0; try{lastOrderTotal = Double.parseDouble(theToken);}catch(Exception e){};newStore.setLastOrderTotal((long)lastOrderTotal);break;
                    case 15: Date date = null;
                        try {
                            date = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse(theToken);
                        } catch (ParseException e) {
                            date = null;
                            e.printStackTrace();
                        }
                        newStore.setLastOrderDate(date);break;
                    case 16: double lastYearTotal = 0; try{lastYearTotal = Double.parseDouble(theToken);}catch(Exception e){};newStore.setLastYearTotal((long)lastYearTotal);break;
                    case 17: double monthlyAverage = 0; try{monthlyAverage = Double.parseDouble(theToken);}catch(Exception e){};newStore.setMonthlyAverage((long)monthlyAverage);break;
                    }
                    
                }
                
                newStore.initOrderList();
                    
                s_storess.add(newStore);
            }
            
            setOrderHistoryDummy();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public Store[] getStores() {
        Store s[] = null;

        s = (Store[])s_storess.toArray(new Store[s_storess.size()]);

        return s;
    }
    

    public Store getStoreById(int id) {
        Iterator iter = s_storess.iterator();
        while (iter.hasNext()) {
            Store currentStore = (Store)iter.next();
            if (currentStore.getId()==id)
                return currentStore;
        }

        //if no store found,return first visible store
        iter = s_storess.iterator();
        while (iter.hasNext()) {
            Store currentStore = (Store)iter.next();
            if (currentStore.isFilter()==false)
                return currentStore;
        }
        //else return null
        return null;        
    }

    public Store getStoreByName(String name) {
        Iterator iter = s_storess.iterator();
        while (iter.hasNext()) {
            Store currentStore = (Store)iter.next();
            String currentStoreName = currentStore.getName();
            if (currentStoreName.equals(name))
                return currentStore;
        }

        return null;        
    }
/*
    public Store getStoreByIndex(int index) {
        if (s_storess.size()>index) {
            return (Store)s_storess.get(index);
        }

        return null;        
    }
*/
    public ArrayList getStoresArrayList() {
        return s_storess;        
    }
    
    public void add(Store store) {
        s_storess.add(store);
    }
}

