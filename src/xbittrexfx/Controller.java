package xbittrexfx;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javazoom.jl.player.Player;
import org.json.JSONException;
import org.json.JSONObject;
import xbittrexfx.model.Markets;
import xbittrexfx.model.Result;
import xbittrexfx.utils.TimeAgo;

public class Controller {

  @FXML
  private Label lblStatus;

  @FXML
  private Label lblLastChecked;

  private Date oldDate = new Date();
  private Stage stage;

  private static final OkHttpClient client = new OkHttpClient();
  private static final Gson gson = new Gson();
  private List<Result> oldList;
  private boolean firstRun = true;
  private Timer timerUpdate = new Timer();
  private Timer timerChecker = new Timer();
  private Request apiRequest;

  @FXML
  private void initialize() throws URISyntaxException, IOException {

    lblStatus.setMaxWidth(Double.MAX_VALUE);
    AnchorPane.setLeftAnchor(lblStatus, 0.0);
    AnchorPane.setRightAnchor(lblStatus, 0.0);
    lblStatus.setAlignment(Pos.CENTER);
    lblLastChecked.setAlignment(Pos.CENTER);
    lblStatus.setText("starting the engine...");
    lblLastChecked.setText("");

    startTimerChecker();

    startTimerUpdateTime();

    apiRequest = new Request.Builder()
        .url("https://bittrex.com/api/v1.1/public/getmarkets")
        .build();

    //------- NOT USED AT THE MOMENT SINCE THE API CALL IS PUBLIC -----------
    //------- read the configuration ---------
    //Properties properties = new Properties();
    //Path propFile = Paths.get(this.getClass().getResource("config.ini").toURI());
    //properties.load(Files.newBufferedReader(propFile));
    //System.out.println(">> GOT>> " + properties.getProperty("BITTREX_API_KEY"));
  }

  private void soundAlarm() {
    timerChecker.cancel();

    try {
      FileInputStream fis = new FileInputStream("alertsound.mp3");
      Player playMP3 = new Player(fis);
      playMP3.play();
    } catch (Exception exc) {
      exc.printStackTrace();
      System.out.println("Failed to play the file.");
    }

    Platform.runLater(() -> stage.toFront());
  }

  private void startTimerUpdateTime() {
    timerUpdate.schedule(new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(() -> {
          Date newDate = new Date();
          lblLastChecked.setText("last checked " + TimeAgo.toDuration(newDate.getTime() - oldDate.getTime()));
        });
      }
    }, 1000, 1000);
  }

  private void startTimerChecker() {
    timerChecker.schedule(new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(() -> {
          Date newDate = new Date();
          oldDate = newDate;
          apiCheck();
        });
      }
    }, 5000, 10000);
  }

  private void apiCheck() {

    client.newCall(apiRequest).enqueue(new Callback() {
      @Override public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()) {

          String jsonData = response.body().string();
          JSONObject jsonObject = null;
          try {
            jsonObject = new JSONObject(jsonData);
          } catch (JSONException e) {
            updateLabel(e.getLocalizedMessage());
            e.printStackTrace();
          }

          if (jsonObject == null) {
            return;
          }

          Markets markets = gson.fromJson(jsonObject.toString(), Markets.class);

          List<Result> newcoinsList = markets.getResult();

          //---- on the first run, do nothing ----
          if (firstRun) {
            firstRun = false;
            oldList = newcoinsList;
            updateLabel("waiting for the next coin (" + newcoinsList.size() + " coins.)");
            return;
          }

          //Test new coin....
          //Result xResult = new Result();
          //xResult.setMarketName("ANDREICOIN");
          //newcoinsList.add(xResult);

          newcoinsList.removeAll(oldList);

          if (newcoinsList.size() > 0) {
            updateLabel("New Coin: " + newcoinsList.get(0).getMarketName());
            soundAlarm();
          }
        } else {
          updateLabel("Ops! failed to talk with the Bittrex api. check firewall ?");
        }
      }

      @Override public void onFailure(Request request, IOException e) {
        updateLabel("error: failed to get data from bittrex. firewall ? api link changed ?");
      }
    });
  }

  //------ helpers -------
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  private void updateLabel(String text) {
    Platform.runLater(() -> lblStatus.setText(text));
  }
}
