package org.smack.fx.converters;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;

import org.smack.fx.FxUtil;
import org.smack.util.converters.StringConverter;
import org.smack.util.converters.StringConverterExtension;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Registers the JavaFx converters.
 *
 * @author MICBINZ
 */
public class Registrar extends StringConverterExtension
{
    private Color convertColor( String name )
    {
        try
        {
            Field f = Color.class.getField( name );
            if ( Color.class.equals( f.getType() ) && Modifier.isStatic( f.getModifiers() ) )
                return (Color) f.get( null );
        }
        catch ( Exception ignore )
        {
            // The field did not exist.
        }

        return Color.web( name );
    }

    private Image convertImage( String s ) throws Exception
    {
        URL url = new URL( s );

        try ( InputStream is = url.openStream() )
        {
            Image result = new Image( is );

            if ( result.isError() )
                throw result.getException();

            return result;
        }
    }

    @Override
    public void extendTypeMap( StringConverter registry )
    {
        registry.put(
                Color.class,
                this::convertColor );
        registry.put(
                Font.class,
                FxUtil::decodeFont );
        registry.put(
                KeyCombination.class,
                KeyCombination::valueOf );
        var c = new FxImageConverter();
        registry.put( Image.class, this::convertImage );
    }
}
