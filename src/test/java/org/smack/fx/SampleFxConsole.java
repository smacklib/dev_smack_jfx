package org.smack.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author micbinz
 */
public class SampleFxConsole extends Application
{
    private void hBoxEven( HBox hbox )
    {
        for ( var c : hbox.getChildren() )
            HBox.setHgrow( c, Priority.ALWAYS );
    }

    @Override
    public void start(Stage primaryStage)
    {
        var root = new VBox();

//        {
//            var cons1 = new FxConsole();
//            var cons2 = new FxConsole();
//
//            cons1.setInputReceiver( cons2.getOut() );
//            cons2.setInputReceiver( cons1.getOut() );
//
//
//            var pair = new HBox( cons1, cons2 );
//            hBoxEven( pair );
//
//            root.getChildren().add(
//                    new Label(cons1.getClass().getSimpleName()) );
//            root.getChildren().add( pair );
//        }
//        {
//            var cons1 = new FxConsole2();
//            var cons2 = new FxConsole2();
//
//            cons1.setInputReceiver( cons2.getOut() );
//            cons2.setInputReceiver( cons1.getOut() );
//
//            var pair = new HBox( cons1, cons2 );
//            hBoxEven( pair );
//
//            root.getChildren().add(
//                    new Label(cons1.getClass().getSimpleName()) );
//            root.getChildren().add( pair );
//        }
        {
            var cons1 = new FxConsole3();
            var cons2 = new FxConsole3();

            cons1.setInputReceiver( cons2.getOut() );
            cons2.setInputReceiver( cons1.getOut() );

            var pair = new HBox( cons1, cons2 );
            hBoxEven( pair );

            root.getChildren().add(
                    new Label(cons1.getClass().getSimpleName()) );
            root.getChildren().add( pair );
        }

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setTitle( getClass().getSimpleName() );
        primaryStage.show();
    }

    public static void main( String[] argv )
    {
        launch( SampleFxConsole.class, argv );
    }
}
