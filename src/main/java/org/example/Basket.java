package org.example;
import java.io.*;
import java.util.Arrays;

public class Basket {
    private String[] products;
    private int[] prices;
    private int[] quantity;
    private File textFile;
    public Basket(String[] products,int[] prices){
        this.prices = prices;
        this.products = products;
        quantity = new int[products.length];
    }
    public void addToCart(int productNum,int amount) throws IOException{
        if(textFile!=null){
            if (textFile.length()!=0){
                BufferedReader reader = new BufferedReader(new FileReader(textFile));
                reader.readLine();
                String[] quantityStr = reader.readLine().split(" ");
                quantity[productNum-1]=Integer.parseInt(quantityStr[productNum-1]);
                quantity[productNum-1]+=amount;
            }
        }
        else {
            quantity[productNum-1]+=amount;
        }

    }
    public void printCart() throws IOException {
        int total = 0;
        System.out.println("Ваша корзина:");
        if(textFile.length()!=0){
        BufferedReader reader = new BufferedReader(new FileReader(textFile));
        String[] products = reader.readLine().split(" ");
        String[] quantityStr = reader.readLine().split(" ");
        String[] pricesStr = reader.readLine().split(" ");
        for(int i=0;i<prices.length;i++){
            prices[i] = Integer.parseInt(pricesStr[i]);
        }
        Basket basket = new Basket(products,prices);
        for(int i=0;i<quantityStr.length;i++){
            quantity[i]=Integer.parseInt(quantityStr[i]);
            basket.addToCart(i+1,quantity[i]);
        }
        for(int i =0;i<products.length;i++){
            if (quantity[i]!=0){
                int sum = (quantity[i]*prices[i]);
                System.out.println(products[i] + " " + quantity[i] + " шт " + prices[i] + " руб/шт " + sum + " в сумме");
                total += sum;
            }
        }
    }
        else{
            for(int i =0;i<products.length;i++){
                if (quantity[i]!=0){
                    int sum = (quantity[i]*prices[i]);
                    System.out.println(products[i] + " " + quantity[i] + " шт " + prices[i] + " руб/шт " + sum + " в сумме");
                    total += sum;
                }
            }
            System.out.println("Итого: " + total + "руб");
        }

        System.out.println("Итого: " + total + "руб");

    }
    public void saveTxt(File textFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(textFile));
        for (int i = 0;i<products.length;i++){
            writer.write(products[i]+" ");
        }
        writer.newLine();
        for (int i = 0;i<quantity.length;i++){
            writer.write(quantity[i]+" ");
        }
        writer.newLine();
        for(int i=0; i<prices.length;i++){
            writer.write(prices[i]+" ");
        }
        writer.flush();
    }
    public static Basket loadFromTxtFile(File textFile)throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(textFile));
        String[] products1 = reader.readLine().split(" ");
        String[] quantityStr = reader.readLine().split(" ");
        String[] pricesStr = reader.readLine().split(" ");
        int[] prices1 = new int[pricesStr.length];
        int[] quantity1 = new int[quantityStr.length];
        for(int i=0;i<prices1.length;i++){
            prices1[i] = Integer.parseInt(pricesStr[i]);
        }
        Basket basket = new Basket(products1,prices1);
        for(int i=0;i<quantityStr.length;i++){
            quantity1[i]=Integer.parseInt(quantityStr[i]);
            basket.addToCart(i+1,quantity1[i]);
        }
        return basket;

    }
    public void getFile(File textFile){
        this.textFile = textFile;
    }

}
