package it.gamerover.nbs.core.configuration.property;

import ch.jalu.configme.properties.SetProperty;
import ch.jalu.configme.properties.types.PrimitivePropertyType;

import java.util.Set;

/**
 * @author gamerover98
 * In the current version of ConfigMe, there's no SetProperty for strings.
 */
@SuppressWarnings("unused") // some methods are not used
public class StringSetProperty extends SetProperty<String> {

    public StringSetProperty(String path, String... defaultValue) {
        super(path, PrimitivePropertyType.STRING, defaultValue);
    }

    public StringSetProperty(String path, Set<String> defaultValue) {
        super(path, PrimitivePropertyType.STRING, defaultValue);
    }

    @Override
    public Object toExportValue(Set<String> value) {
        return value;
    }

}
