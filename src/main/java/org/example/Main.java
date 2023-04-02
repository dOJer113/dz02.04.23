package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ParseException, ParserConfigurationException, SAXException, TransformerException{

        String[] products = {"Хлеб","Яблоко","Молоко"};
        int[] prices ={40,10,60};
        int[] quantity = new int[products.length];
        int total = 0;

        Scanner scanner = new Scanner(System.in);

        JSONObject jsonBasket = new JSONObject();
        JSONArray jsonProducts = new JSONArray();

        Basket basket = new Basket(products,prices);


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        boolean copy = false;
        boolean save = false;
        boolean log = false;
        String filenameLoad = null;
        String filenameSave= null;
        String filenameLog = null;
        String fileNameSave = null;
        boolean saveAsJson = false;
        boolean loadFromJson = false;
        Document doc = (Document) builder.parse(new File("./shop.xml"));
        NodeList nodeList = doc.getChildNodes().item(0).getChildNodes();
        for(int i =0; i<nodeList.getLength();i++){
            Node node =nodeList.item(i);
            if(node.ELEMENT_NODE == node.getNodeType()){
                Element element = (Element) node;
                if(i==1){
                    String load= element.getElementsByTagName("enabled").item(0).getTextContent();
                    if(load.equals("false")){
                        copy = false;
                    }
                    else{
                        copy = true;
                    }
                    filenameLoad = element.getElementsByTagName("fileName").item(0).getTextContent();
                    String loadFormat = element.getElementsByTagName("format").item(0).getTextContent();
                    String fileNameLoad = "./src/" + filenameLoad + '.'+ loadFormat;
                    if(loadFormat.equals("json")){
                         loadFromJson = true;
                    }
                    else {
                        loadFromJson = false;
                    }
                }
                if(i==3){
                    String saveSTR = element.getElementsByTagName("enabled").item(0).getTextContent();
                    if(saveSTR.equals("false")){
                         save = false;
                    }
                    else{
                        save = true;
                    }
                    filenameSave = element.getElementsByTagName("fileName").item(0).getTextContent();
                    String saveFormat = element.getElementsByTagName("format").item(0).getTextContent();
                    fileNameSave = "./src/" + filenameSave;
                    if(saveFormat.equals("json")){
                        saveAsJson = true;

                    }
                    else {
                        saveAsJson = false;
                    }
                }
                if(i==5){
                    String logSTR = element.getElementsByTagName("enabled").item(0).getTextContent();
                    if(logSTR.equals("false")){
                        log = false;
                    }
                    else{
                        log = true;
                        filenameLog = "./src/" + element.getElementsByTagName("fileName").item(0).getTextContent();

                    }

                }

            }

        }
        File txtFile = new File(filenameLog);
        ClientLog cl = new ClientLog();


        if(copy==true) {
            if (loadFromJson == true) {
                File jsonTxt = new File(filenameLoad);
                if (jsonTxt.length() != 0) {
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(new FileReader(jsonTxt));

                    JSONObject BasketParserJson = (JSONObject) obj;
                    JSONArray ProductsJson = (JSONArray) BasketParserJson.get("Product");
                    int p = 0;
                    int i = 0;
                    for (Object o : ProductsJson) {
                        JSONObject productJson = (JSONObject) o;
                        if (p == 0) {
                            products[i] = (String) productJson.get("Name");
                            prices[i] = Math.toIntExact((Long) productJson.get("price"));
                            quantity[i] = Math.toIntExact((Long) productJson.get("quantity"));
                            p++;
                            i++;
                        }
                        if (p == 2) {
                            products[i] = (String) productJson.get("Name");
                            prices[i] = Math.toIntExact((Long) productJson.get("price"));
                            quantity[i] = Math.toIntExact((Long) productJson.get("quantity"));
                            p++;
                            i++;
                        }
                        if (p == 3) {
                            products[i] = (String) productJson.get("Name");
                            prices[i] = Math.toIntExact((Long) productJson.get("price"));
                            quantity[i] = Math.toIntExact((Long) productJson.get("quantity"));
                        }

                    }
                }
                else{
                    jsonTxt.createNewFile();
                }
            } else {
                File textFile = new File(filenameSave);
                basket.getFile(textFile);
                if (textFile.length() != 0) {
                    basket = Basket.loadFromTxtFile(textFile);
                } else {
                    textFile.createNewFile();
                }
                basket.getFile(textFile);
            }

        }


        for(int i=0;i< products.length;i++){
            System.out.println(i+1+". " + products[i]+" "+ prices[i]+" руб/шт");
        }

        while (true){
            System.out.println("Введите товар и количество или введите 'end'");
            String input  = scanner.nextLine();
            if (input.equals("end")) {
                if (save==true){
                if (saveAsJson == true) {
                    for (int i = 0; i < products.length; i++) {
                        JSONObject product = new JSONObject();
                        product.put("Name", products[i]);
                        product.put("price", prices[i]);
                        product.put("quantity", quantity[i]);

                        jsonProducts.add(product);
                    }
                    FileWriter writer = new FileWriter(new File(fileNameSave));
                    jsonBasket.put("Product", jsonProducts);
                    writer.write(jsonBasket.toJSONString());
                    writer.flush();
                }
                if (saveAsJson == false) {
                    basket.saveTxt(new File(filenameSave));
                }
            }
                if(saveAsJson==false){
                    basket.printCart();
                }
                else {
                    for (int i = 0; i < products.length; i++) {
                        if (quantity[i] != 0) {
                            int sum = (quantity[i] * prices[i]);
                            System.out.println(products[i] + " " + quantity[i] + " шт " + prices[i] + " руб/шт " + sum + " в сумме");
                            total += sum;
                        }
                    }
                    System.out.println("Итого: " + total + "руб");
                }

                break;
            }
            String[] parts = input.split(" ");
            basket.addToCart(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
            quantity[Integer.parseInt(parts[0])-1]+=Integer.parseInt(parts[1]);
            if(log==true){
                cl.log(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
                cl.exportAsCSV(txtFile);
            }


        }



    }
}