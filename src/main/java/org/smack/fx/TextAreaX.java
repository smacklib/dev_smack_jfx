/*
 *
 */
package org.smack.fx;

import org.smack.util.StringUtil;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * TODO:
 * - Dynamically compute line height.
 *
 * @author MICBINZ
 */
public class TextAreaX extends StackPane
{
    private final Label _label = new Label();
    private final StackPane _lblContainer;
    private final TextArea _textArea = new TextArea();

    private char NEW_LINE_CHAR = 10;
//    private final double NEW_LINE_HEIGHT = 18D;
    private static final double TOP_PADDING = 0; //3D;
    private static final double BOTTOM_PADDING = 0; //6D;

    public TextAreaX()
    {
        setAlignment(
                Pos.TOP_LEFT);

        _textArea.setWrapText(true);
        _textArea.getStyleClass().add("text-area-x");

        _label.setWrapText(true);
        _label.prefWidthProperty().bind(_textArea.widthProperty());
        _label.textProperty().bind(_textArea.textProperty());

        _lblContainer = new StackPane(_label);
        _lblContainer.setAlignment(
                Pos.TOP_LEFT);
        _lblContainer.setPadding(
                new Insets(4,7,7,7));

        // Binding the container width to the TextArea width.
        _lblContainer.maxWidthProperty().bind(_textArea.widthProperty());

        _textArea.textProperty().addListener(
                (ov,o,n) -> handleText( _textArea.getText(), "text" ) );

        _label.heightProperty().addListener(
                (ov,o,n) -> handleHeight( _textArea.getText() ) );

        lineCountProperty.bind(
                Bindings.divide( _label.heightProperty(), lineHeightProperty ) );

        probeLineHeight();

        lineHeightProperty.addListener( (ov,o,n) -> System.out.println( "lineHeight=" + n ) );
        lineCountProperty.addListener( (ov,o,n) -> System.out.println( "lineCount=" + n ) );
    }

    private final SimpleDoubleProperty lineHeightProperty =
            new SimpleDoubleProperty( this, "lineHeight", 0 );

    public final SimpleDoubleProperty lineCountProperty =
            new SimpleDoubleProperty( this, "lineCount", 0 );

    /**
     *
     * @param text
     * @param what
     */
    private void handleText( String text, String what )
    {
        System.out.println( "Entry: " + what );
        System.out.println( _label.getPrefHeight() );
        System.out.println( _label.getHeight() );
        System.out.println( _label.getMinHeight() );

        // Insert the component when is switches from no content to valid content.
        if ( null == _textArea.getParent() && StringUtil.hasContent( text ) )
            getChildren().addAll( new Group( _lblContainer ), _textArea );

        if( text!=null && text.length()>0 && text.charAt(text.length()-1) == NEW_LINE_CHAR )
        {
            System.out.println( "a" );
            _textArea.setPrefHeight(
                    _label.getHeight() +
                    lineHeightProperty.get() +
                    TOP_PADDING +
                    BOTTOM_PADDING);
            _textArea.setMinHeight(
                    _label.getHeight() +
                    lineHeightProperty.get() +
                    TOP_PADDING +
                    BOTTOM_PADDING);
            System.out.println( "labHeight=" + _label.getHeight() );
        }
        else
        {
            System.out.println( "b" );
            _textArea.setPrefHeight(_label.getHeight() + TOP_PADDING + BOTTOM_PADDING);
            _textArea.setMinHeight(_label.getHeight() + TOP_PADDING + BOTTOM_PADDING);
        }
    }

    /**
     *
     * @param text
     * @param what
     */
    private void handleHeight( String text )
    {
        System.out.println( "Entry: height" );
        System.out.println( _label.getHeight() );

        if( text!=null && text.length()>0 && text.charAt(text.length()-1) == NEW_LINE_CHAR )
        {
            System.out.println( "a" );
            _textArea.setPrefHeight(
                    _label.getHeight() +
                    lineHeightProperty.get() +
                    TOP_PADDING +
                    BOTTOM_PADDING);
            _textArea.setMinHeight(
                    _label.getHeight() +
                    lineHeightProperty.get() +
                    TOP_PADDING +
                    BOTTOM_PADDING);
            System.out.println( "labHeight=" + _label.getHeight() );
        }
        else
        {
            System.out.println( "b lh=" + _label.getHeight() );
            _textArea.setPrefHeight(_label.getHeight() + TOP_PADDING + BOTTOM_PADDING);
            _textArea.setMinHeight(_label.getHeight() + TOP_PADDING + BOTTOM_PADDING);
        }
    }

    public Font getFont()
    {
        return _textArea.getFont();
    }

    public ObjectProperty<Font> fontProperty()
    {
        return _textArea.fontProperty();
    }

    public void setPrefRowCount( int i )
    {
        _textArea.setPrefRowCount( i );
    }

    public int getLength()
    {
        return _textArea.getLength();
    }

    public void appendText( String string )
    {
        _textArea.appendText( string );
    }

    public void setText( String substring )
    {
        _textArea.setText( substring );
    }

    public String getText()
    {
        return _textArea.getText();
    }

    private void probeLineHeight()
    {
        Label tmpLabel = new Label("X");

        tmpLabel.heightProperty().addListener(
                (ov,o,n) -> {
                    lineHeightProperty.set( tmpLabel.getHeight() );
                    getChildren().remove( tmpLabel );
                }                );
        getChildren().add( tmpLabel );
    }
}
