package org.smack.fx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.smack.util.ServiceManager;
import org.smack.util.resource.ResourceManager;
import org.smack.util.resource.ResourceManager.Resource;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FxResourceTest
{
    private final ResourceManager _rm =
            ServiceManager.getApplicationService( ResourceManager.class );

    @Resource
    private Image imageResource;
    @Resource
    private Color colorResource;
    @Resource
    private Font fontResource;
    @Resource
    private KeyCombination keyCombinationResource;
    @Resource
    private short shortResource;

    @Before
    public void testInit()
    {
        _rm.injectResources( this );
    }

    @Test
    public void testImage()
    {
        assertNotNull( imageResource );
    }
    @Test
    public void testColor()
    {
        assertNotNull( colorResource );
    }

    /**
     * FxResourceTest.fontResource = SansSerif-BOLDITALIC-40.5
     */
    @Test
    public void testFont()
    {
        assertNotNull( fontResource );

        assertEquals( 40.5, fontResource.getSize(), 0.0 );
        assertEquals( "SansSerif", fontResource.getFamily() );
        assertEquals( "Bold Italic", fontResource.getStyle() );
    }

    @Test
    public void testKeyCombination()
    {
        assertNotNull( keyCombinationResource );
    }
    /**
     * Ensure that the primitive converters are available.
     */
    @Test
    public void testShortResource()
    {
        assertEquals( 313, shortResource );
    }
}
