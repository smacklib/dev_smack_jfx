package org.smack.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author micbinz
 */
public class SampleFxConsole extends Application
{

    @Override
    public void start(Stage primaryStage)
    {
        var cons1 = new FxConsole3();
        var cons2 = new FxConsole3();

        cons1.setInputReceiver( cons2.getOut() );
        cons2.setInputReceiver( cons1.getOut() );

        primaryStage.setTitle( getClass().getSimpleName() );

        var root = new HBox( cons1, cons2 );

        primaryStage.setScene(new Scene(root, 300, 250));

        primaryStage.show();
    }

    private FxConsole2 makeConsole()
    {
        return new FxConsole2();
    }

    public static void main( String[] argv )
    {
        launch( SampleFxConsole.class, argv );
    }
}
