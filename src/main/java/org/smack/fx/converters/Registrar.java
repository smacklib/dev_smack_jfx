package org.smack.fx.converters;

import org.smack.fx.FxUtil;
import org.smack.util.resource.ResourceConverterExtension;
import org.smack.util.resource.ResourceConverterRegistry;

import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;

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
            registry.put(
                    Font.class,
                    FxUtil::decodeFont );
        }
        {
            var c = new FxImageConverter();
            registry.put( c.getType(), c );
        }
        {
            registry.put(
                    KeyCombination.class,
                    KeyCombination::valueOf );
        }
    }
}
