/* $Id$
 *
 * Copyright © 2018-21 Michael G. Binz
 */
package org.smack.fx;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.smack.util.MathUtil;
import org.smack.util.ServiceManager;
import org.smack.util.StringUtil;
import org.smack.util.resource.ResourceManager;
import org.smack.util.resource.ResourceManager.Resource;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FxUtil
{
    public static javafx.scene.text.Font to( java.awt.Font font )
    {
        FontWeight weight;
        if ( font.isBold() )
            weight = FontWeight.BOLD;
        else
            weight = FontWeight.NORMAL;

        FontPosture posture;
        if ( font.isItalic() )
            posture = FontPosture.ITALIC;
        else
            posture = FontPosture.REGULAR;

        return javafx.scene.text.Font.font(
                font.getFamily(),
                weight,
                posture,
                font.getSize2D() );
    }

    public static java.awt.Font to( Font font )
    {
        // TODO check if decode() and style are compatible.
        return java.awt.Font.decode( font.getStyle() );
    }

    public static String encode(Font str) {
        String style = str.getStyle().toUpperCase( Locale.ENGLISH );

        boolean isBold =
                style.indexOf( FontWeight.BOLD.name() ) != -1;
        boolean isItalic =
                style.indexOf( FontPosture.ITALIC.name() ) != -1;

        if ( isBold && isItalic )
            style = "bolditalic";
        else if ( isBold )
            style = "bold";
        else if ( isItalic )
            style = "italic";
        else
            style = "plain";

        String result = String.format(
                Locale.ROOT,
                "%s-%s-%.1f",
                str.getFamily(),
                style,
                str.getSize() );
        return result;
    }

    /**
     * This is from AWT. Extend to support javafx, esp. weight.
     * @param str
     * @return
     * @see java.awt.Font#decode
     */
    public static Font decodeFont(String str) {
        String fontName = str;
        String styleName = "";
        double fontSize = 12;
        FontWeight fontWeight = FontWeight.NORMAL;
        FontPosture fontPosture = FontPosture.REGULAR;

        if ( StringUtil.isEmpty( str ) ) {
            return new Font( fontSize );
        }

        int lastHyphen = str.lastIndexOf('-');
        int lastSpace = str.lastIndexOf(' ');
        char sepChar = (lastHyphen > lastSpace) ? '-' : ' ';
        int sizeIndex = str.lastIndexOf(sepChar);
        int styleIndex = str.lastIndexOf(sepChar, sizeIndex-1);
        int strlen = str.length();

        if (sizeIndex > 0 && sizeIndex+1 < strlen) {
            try {
                fontSize =
                    Double.valueOf(str.substring(sizeIndex+1)).doubleValue();
                if (fontSize <= 0) {
                    fontSize = 12;
                }
            } catch (NumberFormatException e) {
                /* It wasn't a valid size, if we didn't also find the
                 * start of the style string perhaps this is the style */
                styleIndex = sizeIndex;
                sizeIndex = strlen;
                if (str.charAt(sizeIndex-1) == sepChar) {
                    sizeIndex--;
                }
            }
        }

        if (styleIndex >= 0 && styleIndex+1 < strlen) {
            styleName = str.substring(styleIndex+1, sizeIndex);
            styleName = styleName.toLowerCase(Locale.ENGLISH);
            if (styleName.equals("bolditalic")) {
                fontWeight = FontWeight.BOLD;
                fontPosture = FontPosture.ITALIC;
            } else if (styleName.equals("italic")) {
                fontPosture = FontPosture.ITALIC;
            } else if (styleName.equals("bold")) {
                fontWeight = FontWeight.BOLD;
            } else if (styleName.equals("plain")) {
                // Nothing to do.
            } else {
                /* this string isn't any of the expected styles, so
                 * assume its part of the font name
                 */
                styleIndex = sizeIndex;
                if (str.charAt(styleIndex-1) == sepChar) {
                    styleIndex--;
                }
            }
            fontName = str.substring(0, styleIndex);

        } else {
            int fontEnd = strlen;
            if (styleIndex > 0) {
                fontEnd = styleIndex;
            } else if (sizeIndex > 0) {
                fontEnd = sizeIndex;
            }
            if (fontEnd > 0 && str.charAt(fontEnd-1) == sepChar) {
                fontEnd--;
            }
            fontName = str.substring(0, fontEnd);
        }

        return Font.font( fontName, fontWeight, fontPosture, fontSize );
    }

    private static double map( int pByte )
    {
        if ( pByte > 0xff )
            throw new IllegalArgumentException( "pByte > 255: " + pByte );
        if ( pByte < 0 )
            throw new IllegalArgumentException( "pByte < 0: " + pByte );

        double d = 1.0 / 0xff;

        return pByte * d;
    }

    public static Color to( java.awt.Color from )
    {
        return new Color(
                map( from.getRed() ),
                map( from.getGreen() ),
                map( from.getBlue() ),
                map( from.getAlpha() ) );
    }

    private static int map( double in )
    {
        // 0 1 -> 0 255
        if ( in > 1 )
            throw new IllegalArgumentException( "in > 1: " + in );
        if ( in < 0 )
            throw new IllegalArgumentException( "in < 0: " + in );

        double d = 1.0 / 0xff;

        return MathUtil.round( in / d );
    }

    public static java.awt.Color to( Color from )
    {
        return new java.awt.Color(
                map( from.getRed() ),
                map( from.getGreen() ),
                map( from.getBlue() ),
                map( from.getOpacity() ) );
    }

    public static Background getBackground( Color color )
    {
        return new Background(
                new BackgroundFill(
                        color, null, null) );
    }

    public static Node createHorizontalGlue()
    {
        Region result = new Region();
        HBox.setHgrow( result, Priority.ALWAYS );
        return result;
    }

    /**
     * Convert an AWT Shape to a list of JavaFx path elements.
     *
     * @param shape The shape to convert.
     * @return The path elements.
     */
    public static List<PathElement> convert( Shape shape )
    {
        float[] coords =
                new float[6];
        List<PathElement> result =
                new ArrayList<>();

        for ( PathIterator pi = shape.getPathIterator(null) ;
                !pi.isDone() ;
                pi.next())
        {
            int type = pi.currentSegment(coords);

            switch ( type )
            {
            case PathIterator.SEG_LINETO:
                result.add( new LineTo(
                        coords[0],
                        coords[1] ));
                break;
            case PathIterator.SEG_CLOSE:
                result.add( new ClosePath() );
                break;
            case PathIterator.SEG_CUBICTO:
                result.add( new  CubicCurveTo(
                        coords[0],
                        coords[1],
                        coords[2],
                        coords[3],
                        coords[4],
                        coords[5] ) );
                break;
            case PathIterator.SEG_QUADTO:
                result.add( new QuadCurveTo(
                        coords[0],
                        coords[1],
                        coords[2],
                        coords[3] ) );
                break;
            case PathIterator.SEG_MOVETO:
                result.add( new MoveTo(
                        coords[0],
                        coords[1] ));
                break;
            default:
                throw new InternalError( "" + type );
            }
        }

        return result;
    }

    public static List<PathElement> makeTextPath(
            java.awt.Font font,
            String text )
    {
        BufferedImage result = ImageUtil.makeImage( 1, 1 );

        Graphics2D g = ImageUtil.antialiasedGfx(
                result );

        try
        {
            GlyphVector gv = font.createGlyphVector(
                    g.getFontRenderContext(),
                    text.toCharArray() );

            return convert( gv.getOutline() );
        }
        finally
        {
            g.dispose();
        }
    }

    public  static Region lockPreferredSize( Region region )
    {
        if (! region.isResizable() )
            return region;

        region.setMinHeight( Region.USE_PREF_SIZE );
        region.setMaxHeight( Region.USE_PREF_SIZE );
        region.setMinWidth( Region.USE_PREF_SIZE );
        region.setMaxHeight( Region.USE_PREF_SIZE );

        return region;
    }

    public static Dimension2D to( Dimension d )
    {
        return new Dimension2D(
                d.getWidth(),
                d.getHeight() );
    }

    @Resource
    private static String _fatalError;

    /**
     * Terminate the application with a last message.
     *
     * @param message Last words.
     */
    public static void handleFatalError( String message )
    {
        Alert fatal = new Alert(
                Alert.AlertType.NONE,
                message,
                ButtonType.CLOSE);
        fatal.setHeaderText( _fatalError );

        fatal.showAndWait();
        System.exit( 1 );
    }

    static
    {
        ServiceManager.getApplicationService( ResourceManager.class )
            .injectResources( FxUtil.class );
    }

    private FxUtil()
    {
        throw new AssertionError();
    }
}
