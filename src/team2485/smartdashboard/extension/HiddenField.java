package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.util.HashMap;

public class HiddenField extends Widget {
    public static final String NAME = "Hidden Public Field";
    public static final DataType[] TYPES = { DataType.BOOLEAN, DataType.NUMBER, DataType.STRING };

    private static HashMap<String, Object> publicFields = new HashMap<>();

    public static Object getField(String fieldName) {
        return publicFields.get(fieldName);
    }

    @Override
    public void init() {
    }

    @Override
    public void setValue(Object o) {
        publicFields.put(this.getFieldName(), o);
    }

    @Override
    public void propertyChanged(Property prprt) {
    }
}
