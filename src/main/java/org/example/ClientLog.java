package org.example;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

public class ClientLog {
    private ArrayList<String> productNumAndAmount = new ArrayList<String>();
    public void log(int productNum, int amount){
        productNumAndAmount.add(Integer.toString(productNum));
        productNumAndAmount.add(Integer.toString(amount));
    }
    public void exportAsCSV(File txtFile) throws IOException {
        ICSVWriter writer = new CSVWriter(new FileWriter(txtFile));
        String[] productsAmount ={"productNum","amount"};
        writer.writeNext(productsAmount);
        for (int i=0; i<productNumAndAmount.size();i=i+2){
            StringJoiner sj = new StringJoiner(",")
                    .add(productNumAndAmount.get(i))
                    .add(productNumAndAmount.get(i+1));
            String[] products = sj.toString().split(",");
            writer.writeNext(products);
            writer.flush();
        }
    }
}
