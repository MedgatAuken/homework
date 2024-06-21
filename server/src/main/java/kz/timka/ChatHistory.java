package kz.timka;

import java.io.*;

public class ChatHistory {

    private String filePath;

    public ChatHistory(String filePath) {
        this.filePath = filePath;
    }

    public void writeMessage(String message){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readHistory(){
        StringBuilder history = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return history.toString();
    }

}
