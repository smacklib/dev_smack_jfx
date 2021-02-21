/*
 *
 */
package org.smack.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class TextAreaX extends StackPane
{
    private Label label;
    private StackPane lblContainer ;
    private TextArea textArea;

    private char NEW_LINE_CHAR = 10;
    private final double NEW_LINE_HEIGHT = 18D;
    private final double TOP_PADDING = 3D;
    private final double BOTTOM_PADDING = 6D;

    public TextAreaX(){
        super();
        configure();
    }

    public TextAreaX(String text)
    {
        configure();
        textArea.setText(text);
    }

    private void configure(){
        setAlignment(Pos.TOP_LEFT);

        this.textArea =new TextArea();
        this.textArea.setWrapText(true);
        this.textArea.getStyleClass().add("scroll-free-text-area");


        this.label =new Label();
        this.label.setWrapText(true);
        this.label.prefWidthProperty().bind(this.textArea.widthProperty());
        this.label.textProperty().bind(this.textArea.textProperty());

        this.lblContainer = new StackPane(label);
        lblContainer.setAlignment(Pos.TOP_LEFT);
        lblContainer.setPadding(new Insets(4,7,7,7));

        // Binding the container width to the TextArea width.
        lblContainer.maxWidthProperty().bind(textArea.widthProperty());

        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> paramObservableValue,   String paramT1, String value) {
                layoutForNewLine(textArea.getText());
            }
        });

        label.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> paramObservableValue,   Number paramT1, Number paramT2) {
                layoutForNewLine(textArea.getText());
            }
        });

        getChildren().addAll( new Group( lblContainer ),textArea);
    }

    private void layoutForNewLine(String text){
        if(text!=null && text.length()>0 &&
                    text.charAt(text.length()-1) == NEW_LINE_CHAR )
        {
            textArea.setPrefHeight(label.getHeight() + NEW_LINE_HEIGHT + TOP_PADDING + BOTTOM_PADDING);
            textArea.setMinHeight(label.getHeight() + NEW_LINE_HEIGHT + TOP_PADDING + BOTTOM_PADDING);
        }
        else
        {
            textArea.setPrefHeight(label.getHeight() + TOP_PADDING + BOTTOM_PADDING);
            textArea.setMinHeight(label.getHeight() + TOP_PADDING + BOTTOM_PADDING);
        }
    }

    public Font getFont()
    {
        return textArea.getFont();
    }

    public ObjectProperty<Font> fontProperty()
    {
        return textArea.fontProperty();
    }

    public void setPrefRowCount( int i )
    {
        textArea.setPrefRowCount( i );
    }

    public void setWrapText( boolean b )
    {
        textArea.setWrapText( b );
    }

    public void setEditable( boolean b )
    {
        textArea.setEditable( b );
    }

    public int getLength()
    {
        return textArea.getLength();
    }

    public void appendText( String string )
    {
        textArea.appendText( string );
    }

    public void setText( String substring )
    {
        textArea.setText( substring );
    }

    public String getText()
    {
        return textArea.getText();
    }

//    public int getPrefRowCount()
//    {
//        return textArea.getPrefRowCount();
//    }
}
