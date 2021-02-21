package org.smack.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

class TextAreaX extends StackPane
{
    private final Label _label = new Label();

    private final TextArea _textArea = new TextArea();

    public final StringProperty _textProperty =
            _textArea.textProperty();
    public final ObjectProperty<Font> _fontProperty =
            _textArea.fontProperty();

    TextAreaX()
    {
        getChildren().addAll( _label, _textArea );

        _label.textProperty().bind( _textProperty );
        _label.fontProperty().bind( _fontProperty );

        prefHeightProperty().bind( _label.prefHeightProperty() );
    }

    public ObjectProperty<Font> fontProperty()
    {
        return _fontProperty;
    }
    public StringProperty textProperty()
    {
        return _textProperty;
    }
}
