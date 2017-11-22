package xbittrexfx.model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Markets {

  @SerializedName("success")
  @Expose
  private Boolean success;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("result")
  @Expose
  private List<Result> result = null;


}