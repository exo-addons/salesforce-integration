package org.exoplatform.salesforce.integ.util;

import java.lang.reflect.Method;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author dev.zaouiahmed@gmail.com
 *
 */
public class PicklistEnumConverter implements Converter {

    private static final String FACTORY_METHOD = "fromValue";

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Class<?> aClass = o.getClass();
        try {
            Method getterMethod = aClass.getMethod("value");
            writer.setValue((String) getterMethod.invoke(o));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Exception writing pick list value %s of type %s: %s",
                            o, o.getClass().getName(), e.getMessage()), e);
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String value = reader.getValue();
        Class<?> requiredType = context.getRequiredType();
        try {
            Method factoryMethod = requiredType.getMethod(FACTORY_METHOD, String.class);
            // use factory method to create object
            return factoryMethod.invoke(null, value);
        } catch (SecurityException e) {
            throw new IllegalArgumentException(
                    String.format("Security Exception reading pick list value %s of type %s: %s",
                            value, context.getRequiredType().getName(), e.getMessage()), e);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Exception reading pick list value %s of type %s: %s",
                            value, context.getRequiredType().getName(), e.getMessage()), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canConvert(Class aClass) {
        try {
            return Enum.class.isAssignableFrom(aClass) && aClass.getMethod(FACTORY_METHOD, String.class) != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

}

