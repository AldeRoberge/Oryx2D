package oryx2D.properties;

import alde.commons.properties.Property;
import alde.commons.properties.PropertyFileManager;

public class Properties {

    static PropertyFileManager propertyFileManager = new PropertyFileManager("Oryx2D.properties");

    public static final Property PASSWORD = new Property("PASSWORD", "", propertyFileManager);
    public static final Property EMAIL = new Property("EMAIL", "", propertyFileManager);

    public static final Property AUTOMATICALLY_CONNECT = new Property("AUTOMATICALLY_CONNECT", Property.FALSE, propertyFileManager);
}
