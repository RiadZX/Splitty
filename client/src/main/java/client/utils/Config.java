package client.utils;

import java.io.*;

public class Config {
    private static  final String USER_FILENAME="userConfig";

    public static void writeUserConfigFile(User user){
        /*
        File file=new File("./client/build/resources/main", USER_FILENAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
    }
    public  static User readUserConfigFile(){
        File file=new File("./client/build/resources/main", USER_FILENAME);
        User user=null;
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            user = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException ignored) {}
        return  user;
    }
}
