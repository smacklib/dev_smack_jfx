package org.smack.fx;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

class TextAreaX extends TextArea
{
    private final Label _label = new Label();

    TextAreaX()
    {
        _label.textProperty().bind( textProperty() );

        prefHeightProperty().bind( _label.prefHeightProperty() );
    }
}
