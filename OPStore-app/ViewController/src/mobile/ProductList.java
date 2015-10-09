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
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/*
 * Copyright © AuraPlayer 2013 All Rights Reserved. 
 * No part of this source code may be reproduced without AuraPlayer's express consent.
 */

public class ProductList {
    private List s_products = null;

    public ProductList() {
        if (s_products == null) {
            s_products = new ArrayList();
        }
    }

    public boolean setProducts() {
        
        return true;
    }

    public boolean setProductsDummy() {
        s_products.clear();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(".adf/META-INF/data_files/Products.csv");
        if (is == null) {
            System.err.println("###### Could not look up : /META-INF/data_files/Products.csv");
        }

        BufferedReader bReader = null;
        

        try {
            bReader = new BufferedReader(new InputStreamReader(is));
            String strLine = "";
            while ((strLine = bReader.readLine()) != null)
            {
                //break comma separated line using ","
                int tokenNumber = 0;
                StringTokenizer st = new StringTokenizer(strLine, ",");
                Product newProduct = new Product();
                while (st.hasMoreTokens())
                {             
                    tokenNumber++;
                    String theToken = st.nextToken();
                    switch (tokenNumber){
                    case 1: int id = 0;try{id=Integer.parseInt(theToken);}catch(Exception e){};newProduct.setProdID(id);break;
                    case 2: newProduct.setImagePath(theToken);break;
                    case 3: newProduct.setName(theToken);break;
                    case 4: newProduct.setDescription(theToken);break;
                    case 5: newProduct.setSubCategory(theToken);break;
                    case 6: newProduct.setCategory(theToken);break;
                    case 7: int bestSeller = 0; try{bestSeller = Integer.parseInt(theToken);}catch(Exception e){};newProduct.setBestSeller(bestSeller);break;
                    case 8: int recommended = 0;  try{recommended = Integer.parseInt(theToken);}catch(Exception e){};newProduct.setRecommended(recommended);break;
                    case 9: double salePrice =0;try{salePrice = Double.parseDouble(theToken);}catch(Exception e){};newProduct.setSalePrice(salePrice);break;
                    case 10: double listPrice =0;try{listPrice = Double.parseDouble(theToken);}catch(Exception e){};newProduct.setListPrice(listPrice);break;
                    case 11: int inventoryQuantity = 0; try{inventoryQuantity = Integer.parseInt(theToken);}catch(Exception e){};newProduct.setInventoryQuantity(inventoryQuantity);break;
                    case 12: int lastOrderQuantity = 0;  try{lastOrderQuantity = Integer.parseInt(theToken);}catch(Exception e){};newProduct.setLastOrderQuantity(lastOrderQuantity);break;
                    }
                    
                }
                s_products.add(newProduct);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    

    public Product[] getProducts() {
        Product p[] = null;

        p = (Product[])s_products.toArray(new Product[s_products.size()]);

        return p;
    }
    
    public Product getProductByIndex(int index) {
        if (s_products.size()>index) {
            return (Product)s_products.get(index);
        }

        return null;        
    }
    
    public List getProductsList() {
        return s_products;
    }

    public void add(Product product) {
        s_products.add(product);
    }


}
