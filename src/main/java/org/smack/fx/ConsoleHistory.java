package org.smack.fx;

import java.util.ArrayList;
import java.util.List;

import org.smack.util.StringUtil;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

class ConsoleHistory extends ScrollPane
{
    private int MAX_LEN = 100;

    enum LineType { History, Output };

    private final VBox _vbox = new VBox();

    private List<String> _history =
            new ArrayList<>();
    private int _currentLine =
            -1;

    ConsoleHistory()
    {
        setContent( _vbox );
        setFitToWidth( true );
        setHbarPolicy( ScrollBarPolicy.NEVER );

        _vbox.heightProperty().addListener(
                (ov,o,n) ->
                setVvalue( getVmax() ) );
    }

    /**
     * Add a command line to the history.  The strategy is to not keep any
     * duplicate lines in the history and to add recent lines always to the
     * recent history.  This means that rarely used entries move towards
     * the old end of the history and are thrown out first.
     *
     * @param commandLine The command line to put into the history.
     */
    void addHistoryLine( String line )
    {
        var label = makeLabel( line, LineType.History );

        var list = _vbox.getChildren();

        list.add( label );

        //
        // History handling.
        //

        // No empty strings in history.
        if ( StringUtil.isEmpty( line ) )
            return;

        // No duplicate strings in history.
        _history.remove( line );

        // Bound size.
        if ( _history.size() == MAX_LEN )
            _history.remove( 0 );

        _history.add( line );

        // Reset current position.
        _currentLine = _history.size();
    }

    String previousHistoryLine()
    {
        if ( _currentLine < 0 )
            return null;
        if ( _currentLine > 0 )
            _currentLine--;

        return _history.get( _currentLine );
    }

    /**
     * @return A newer history line or null at the begin of history.
     */
    String nextHistoryLine()
    {
        if ( _currentLine < 0 )
            return null;
        if ( _currentLine == _history.size() )
            return null;
        if ( ++_currentLine == _history.size() )
            return null;

        return _history.get( _currentLine );
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

        setUserData( s, text, type );

        return s;
    }

    private void setUserData( Node node, String text, LineType type )
    {
        node.setUserData( new Pair<LineType, String>( type, text ) );
    }

    @SuppressWarnings("unchecked")
    private Pair<LineType, String> getUserData( Node node )
    {
        return (Pair<LineType, String>)node.getUserData();
    }
}

