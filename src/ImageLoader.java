import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

public class ImageLoader {

    public static Image load(String filename, int width, int height) {

        // user.dir is always the Eclipse project root folder
        String projectRoot = System.getProperty("user.dir");

        String[] searchPaths = {
            projectRoot + File.separator + "images" + File.separator + filename,
            projectRoot + File.separator + filename,
            "images" + File.separator + filename,
            filename
        };

        for (String path : searchPaths) {
            File f = new File(path);
            if (f.exists()) {
                System.out.println("[ImageLoader] Found: " + f.getAbsolutePath());
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                return icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
        }

        System.err.println("[ImageLoader] WARNING: Could not find image: " + filename);
        System.err.println("[ImageLoader] Make sure 'images' folder is at: " + projectRoot + File.separator + "images");
        return null;
    }
}