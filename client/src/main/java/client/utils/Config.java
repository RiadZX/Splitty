package client.utils;

import java.io.*;
import java.util.Objects;

public class Config {
    private static  final String USER_FILENAME="userConfig";

    public static void writeUserConfigFile(User user){
        String userDirectory = new File("").getAbsolutePath();
        String[] sp=userDirectory.split("/");
        File file;
        if (Objects.equals(sp[sp.length - 1], "client")) {
            file = new File("./build/resources/main", USER_FILENAME);
        }
        else {
            file = new File("./client/build/resources/main", USER_FILENAME);
        }
        System.out.println(userDirectory);
        try {
            if (file.createNewFile()){
                System.out.println("User config file was created");
            }
            else {
                System.out.println("User config file already exists");
            }
        } catch (IOException e) {
            System.out.println("Error creating user config file");
        }
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public  static User readUserConfigFile(){
        File file=new File("./build/resources/main", USER_FILENAME);
        User user=null;
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            user = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException ignored) {}
        return  user;
    }
}
