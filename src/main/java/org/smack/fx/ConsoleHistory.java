package org.smack.fx;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

class ConsoleHistory extends ScrollPane
{
    enum LineType { History, Output };

    private final VBox _vbox = new VBox();

    ConsoleHistory()
    {
        setContent( _vbox );
        setFitToWidth( true );
        setHbarPolicy( ScrollBarPolicy.NEVER );

        _vbox.heightProperty().addListener(
                (ov,o,n) ->
                setVvalue( getVmax() ) );
    }

    void addHistoryLine( String line )
    {
        var label = makeLabel( line, LineType.History );
        _vbox.getChildren().add( label );
    }

    void addOutput( String text )
    {
        var label = makeLabel( text, LineType.Output );
        label.setBackground( FxUtil.getBackground( Color.AQUAMARINE ) );
        _vbox.getChildren().add( label );
    }

    private Label makeLabel( String text, LineType type )
    {
        var s = new Label( text );

        s.setWrapText( true );
        s.setFocusTraversable( true );
        s.setUserData( type );
        return s;
    }
}
