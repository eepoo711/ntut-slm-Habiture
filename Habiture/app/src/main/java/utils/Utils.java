package utils;


import java.io.Closeable;
import java.io.IOException;

import utils.exception.CleanupException;

public class Utils {

    public static void closeIO(Closeable c) {
        if(c != null)
            try {
                c.close();
            } catch (IOException e) {
                throw new CleanupException(e);
            }
    }

}
