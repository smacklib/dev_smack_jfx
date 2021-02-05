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
    private final FxConsole cons1 = new FxConsole();
    private final FxConsole cons2 = new FxConsole();

    @Override
    public void init() throws Exception
    {
        cons1.setInputReceiver( cons2.getOut() );
        cons2.setInputReceiver( cons1.getOut() );
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle( getClass().getSimpleName() );

        var root = new HBox( cons1, cons2 );

        primaryStage.setScene(new Scene(root, 300, 250));

        primaryStage.show();
    }

    public static void main( String[] argv )
    {
        launch( SampleFxConsole.class, argv );
    }
}
