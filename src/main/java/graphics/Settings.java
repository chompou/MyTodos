package graphics;

import java.io.*;

public class Settings implements Serializable {
    private boolean darkTheme = false;
    private int textSize = 12;

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void saveSettings() {
        try (FileOutputStream fs = new FileOutputStream("settings.todo")) {
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings loadSettings() {
        Settings settings = null;
        try (FileInputStream fs = new FileInputStream("settings.todo")) {
            ObjectInputStream os = new ObjectInputStream(fs);
            settings = (Settings) os.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return settings;
    }
}