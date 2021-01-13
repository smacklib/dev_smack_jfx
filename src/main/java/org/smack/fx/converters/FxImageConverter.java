/* $Id$
 *
 * Unpublished work.
 * Copyright © 2018 Michael G. Binz
 */
package org.smack.fx.converters;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.smack.util.resource.ResourceConverter;
import org.smack.util.resource.ResourceMap;

import javafx.scene.image.Image;

public class FxImageConverter extends ResourceConverter
{
    static private final Logger LOG =
            Logger.getLogger( FxImageConverter.class.getName() );

    public FxImageConverter()
    {
        super( Image.class );
    }

    @Override
    public Object parseString( String s, ResourceMap unused ) throws Exception
    {
        URL url = new URL( s );

        // Loading the image using the Image( URL ) ctor is not
        // working when used in a OneJar-jar-file, thus the
        // workaround using the stream.
        try ( InputStream is = url.openStream() )
        {
            Image result = new Image( is );

            if ( result.isError() )
                LOG.log( Level.SEVERE, "Image error.", result.getException() );

            return result;
        }
    }
}
