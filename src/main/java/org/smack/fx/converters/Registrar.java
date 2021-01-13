package org.smack.fx.converters;

import org.smack.util.resource.ResourceConverterExtension;
import org.smack.util.resource.ResourceConverterRegistry;

/**
 * Registers the JavaFx converters.
 *
 * @author MICBINZ
 */
public class Registrar extends ResourceConverterExtension
{
    @Override
    public void extendTypeMap( ResourceConverterRegistry registry )
    {
        {
            var c = new FxColorConverter();
            registry.put( c.getType(), c );
        }
        {
            var c = new FxFontConverter();
            registry.put( c.getType(), c );
        }
        {
            var c = new FxImageConverter();
            registry.put( c.getType(), c );
        }
        {
            var c = new KeyCombinationConverter();
            registry.put( c.getType(), c );
        }
    }
}
