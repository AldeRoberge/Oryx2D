package oryx2D.properties;

import alde.commons.properties.Property;
import alde.commons.properties.PropertyFileManager;

public class Properties {

	static PropertyFileManager propertyFileManager = new PropertyFileManager("ORYX2D.txt");

	public static final Property PASSWORD = new Property("PASSWORD", "", propertyFileManager);
	public static final Property EMAIL = new Property("EMAIL", "", propertyFileManager);

}
