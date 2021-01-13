package org.smack.fx.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.smack.util.resource.ResourceConverter;
import org.smack.util.resource.ResourceMap;

import javafx.scene.paint.Color;

/**
 *
 * @author Romain Guy
 * @author Michael Binz
 */
public class FxColorConverter extends ResourceConverter
{
    public FxColorConverter()
    {
        super( Color.class );
    }

    /**
     * Parses colors with an alpha channel and comma separated RGB[A] values.
     * Legal formats for color resources are:
     * "#RRGGBB",  "#AARRGGBB", "R, G, B", "R, G, B, A"
     * or the color plain names defined on {@link Color}.
     * @author Romain Guy
     */
    @Override
    public Object parseString(String s, ResourceMap ignore) throws Exception
    {
        Color result = checkPlainColorName( s );
        if ( result != null )
            return result;

        return Color.web( s );
    }

    private Color checkPlainColorName( String name )
    {
        try
        {
            Field f = Color.class.getField( name );
            if ( ! Color.class.equals( f.getType() ) )
                return null;
            if ( ! Modifier.isStatic( f.getModifiers() ) )
                return null;
            return (Color) f.get( null );
        }
        catch ( Exception e )
        {
            return null;
        }
    }
}
