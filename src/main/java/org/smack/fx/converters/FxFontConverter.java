/* $Id$
 *
 * Unpublished work.
 * Copyright © 2018 Michael G. Binz
 */
package org.smack.fx.converters;

import java.util.function.BiFunction;

import org.smack.fx.FxUtil;
import org.smack.util.resource.ResourceConverter;
import org.smack.util.resource.ResourceMap;

import javafx.scene.text.Font;

public class FxFontConverter extends ResourceConverter
{
    public static final BiFunction<String, ResourceMap, Font> F =
            (a,b) -> { return FxUtil.decodeFont( a ); };

    public FxFontConverter()
    {
        super( Font.class );
    }

    @Override
    public Object parseString( String s, ResourceMap ignore ) throws Exception
    {
        // TODO could load tff if this is the suffix of the name. Later ...
        return F.apply( s, ignore );
    }

    public static void main( String[] args )
    {
        String s = "SansSerif-BOLDITALIC-40.5";
        Font result = F.apply( s, null );
        System.out.println( s + " : " + result.getStyle() );
        System.out.println( s + " : " + FxUtil.encode( result ) );
    }
}
