/* $Id$
 *
 * Common.
 *
 * Released under Gnu Public License
 * Copyright © 2003-15 Michael G. Binz
 */
package org.smack.fx;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.smack.util.io.OutputStreamForwarder;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

/**
 * Handles only output.  Experimental.
 *
 * A console ui component. Connects stream oriented in- and output to a
 * text component.
 *
 * Experimental.
 *
 * @author Michael Binz
 */
public final class FxConsoleOut extends BorderPane
{
    private final Logger LOG = Logger.getLogger( getClass().getName() );

    /**
     * The maximum text length a console is allowed to handle.  Adding more text results in
     * the console discarding the oldest text.
     */
    private final static int MAX_TEXT_LENGTH = 250000;

    /**
     * The stream that receives input from the console. This is passed in by the client.
     */
    private OutputStream _outPipe  = null;

    /**
     * The console's output stream that is connected to the text area.
     */
    private final OutputStream _out = new OutputStream()
    {

        @Override
        public void write( int b ) throws IOException
        {
            write( new byte[] { (byte)(b & 0xff) } );
        }

        @Override
        public void write( byte b[] ) throws IOException
        {
            threadSavePrint( new String( b ) );
        }

        @Override
        public void write( byte[] b, int off, int len ) throws IOException
        {
            threadSavePrint( new String( b, off, len  ) );
        }
    };

    /**
     * Get a reference to the output stream.
     *
     * @return A reference to the output stream.
     */
    public OutputStream getOut() {
        return _out;
    }

    /**
     * The document position where the editable content starts.
     */
    private int _cmdStart = 0;

    /**
     * The text component used.
     */
    private final TextArea _text;

    /**
     * Creates a console.
     */
    public FxConsoleOut() {
        this( false );
    }

    /**
     * Creates a console. Allows to select whether carriage
     * returns are displayed in the console window as they are entered.
     *
     * @param showCr If true then entered carriage returns are shown in the
     * console window. Otherwise carriage returns are not displayed.
     */
    public FxConsoleOut( boolean showCr ) {

        _text = new TextArea() {

            @Override
            public void cut() {
                // If not in the edit area map the cut action to copy.
                if ( _text.getCaretPosition() < _cmdStart) {
                    super.copy();
                }
                else {
                    super.cut();
                }
            }

            @Override
            public void paste() {
                // Paste is ignored.
                return;
            }
        };

        _text.setWrapText( true );
        _text.setEditable( false );

        _text.setFont(
                new Font("Monospaced", 12));

        setCenter( _text );
    }

    /**
     * Appends the passed text to the console display.
     */
    private void append(String string) {

        StringBuilder d =
                new StringBuilder( _text.getText() );

        int documentLength = d.length();

        // Check if we reached our maximum buffer size and release data if this
        // is the case.
        if (documentLength + string.length() > FxConsoleOut.MAX_TEXT_LENGTH) {
            d.delete(
                    0,
                    // Under extreme circumstances it may be the case that the
                    // passed strings are larger than the entire buffer, for
                    // example if the buffer is defined relatively small and a
                    // lot of data is sent.
                    Math.min( string.length(), documentLength ) );
        }

        d.insert( d.length(), string );

        _text.setText( d.toString() );
    }

    /**
     *
     */
    private void replaceEditArea(String s) {
        _text.selectRange(_cmdStart, _text.getText().length());
        _text.replaceSelection(s);
        _text.positionCaret(_text.getText().length());
    }

    /**
     * Set an OutputStream that receives the lines entered by the user.
     *
     * @param os
     *            The output stream receiving lines entered by the user.
     */
    public void setInputReceiver(OutputStream os) {
        _outPipe = os == null ?
                null :
                    new OutputStreamForwarder( os, 10 );

        // Set back to default background color.
        if ( _outPipe != null )
        {
            //            _text.setBackground( new JTextArea().getBackground() );
            _text.setEditable( true );
        }
        else
        {
            //            _text.setBackground( _toolbar.getBackground() );
            _text.setEditable( false );
        }
    }

    /**
     * A buffer used to carry the bytes across the border between non-edt and edt threads.
     * Since the edt event queue is processed very slow this buffer is used to collect incoming
     * data while the event queue has not processed the insert operation.
     * Used only in {@link #threadSavePrint(String)}.
     */
    private final StringBuffer _crossEdtBuffer = new StringBuffer();

    /**
     * Output the passed string to the console.
     *
     * @param string The string to print. The passed string buffer is locked
     * and modified.
     */
    private void printTakeover( StringBuffer string ) {

        if ( ! javafx.application.Platform.isFxApplicationThread() )
            throw new InternalError( "Not on EDT." );

        int caretPosition;

        if ( _text.getSelection().getLength() != 0 )
            caretPosition = -1;
        else
            caretPosition = _text.getCaretPosition();

        // Atomically read and reset the data to display.
        synchronized ( string )
        {
            append(string.toString());
            string.setLength( 0 );
        }

        if ( caretPosition < 0 )
            ;
        else if ( lockedProperty.get() )
            _text.positionCaret( caretPosition );
        else
            _text.positionCaret( _cmdStart );
    }

    /**
     * Append to the console from a thread different from the EDT.
     *
     * @param string The text to append.
     */
    private synchronized void threadSavePrint( String string ) {

        boolean edtNotifyNeeded = true;

        synchronized ( _crossEdtBuffer )
        {
            // If our edt buffer was empty, we have to send a notification to
            // JavaFx.
            // If it was not empty, Fx is already notified.
            edtNotifyNeeded = _crossEdtBuffer.length() == 0;
            _crossEdtBuffer.append( string );
        }

        // If no Fx notification is needed...
        if ( ! edtNotifyNeeded )
        {
            // ...we're done.
            return;
        }

        // Notify Fx of the new data.
        Platform.runLater( () ->
            printTakeover( _crossEdtBuffer ) );
    }

    /**
     * If true then scroll lock is active.
     */
    private final SimpleBooleanProperty lockedProperty =
            new SimpleBooleanProperty( this, "locked", false );
    {
        lockedProperty.addListener( (a,b,c) -> setLocked( a,b,c ) );
    }

    /**
     * Sets the scroll lock status.
     *
     * @param what The scroll lock status. {@code true} is scroll lock active.
     */
    private void setLocked( Object o, boolean was, boolean what )
    {
        // Clear the selection if the lock status changed.
        _text.deselect();

        // If not longer locked navigate to the end of the text.
        if ( !what )
            _text.positionCaret( _cmdStart );
    }
}
