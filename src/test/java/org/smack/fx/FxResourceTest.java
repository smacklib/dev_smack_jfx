package org.smack.fx;

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
    @Test
    public void testFont()
    {
        assertNotNull( fontResource );
    }
    @Test
    public void testKeyCombination()
    {
        assertNotNull( keyCombinationResource );
    }
}
