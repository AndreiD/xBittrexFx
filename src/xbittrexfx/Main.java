package xbittrexfx;

import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("root.fxml"));
    Parent root = fxmlLoader.load();

    Controller controller = fxmlLoader.getController();
    controller.setStage(primaryStage);

    primaryStage.setTitle("xBittrex New Coin Alerter v.1");
    primaryStage.getIcons().add(new Image("/resources/bitcoin.png"));
    primaryStage.setOnCloseRequest(e -> Platform.exit());

    Scene mainScene = new Scene(root, 320, 175);

    URL url = this.getClass().getResource("/resources/main.css");
    if (url == null) {
      System.out.println("Resource css not found. Aborting.");
      System.exit(-1);
    }
    String css = url.toExternalForm();
    mainScene.getStylesheets().add(css);
    primaryStage.setScene(mainScene);

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
