package xbittrexfx.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Result {

  @SerializedName("MarketCurrency")
  @Expose
  private String marketCurrency;
  @SerializedName("BaseCurrency")
  @Expose
  private String baseCurrency;
  @SerializedName("MarketCurrencyLong")
  @Expose
  private String marketCurrencyLong;
  @SerializedName("BaseCurrencyLong")
  @Expose
  private String baseCurrencyLong;
  @SerializedName("MinTradeSize")
  @Expose
  private Double minTradeSize;
  @SerializedName("MarketName")
  @Expose
  private String marketName;
  @SerializedName("IsActive")
  @Expose
  private Boolean isActive;
  @SerializedName("Created")
  @Expose
  private String created;
  @SerializedName("Notice")
  @Expose
  private Object notice;
  @SerializedName("IsSponsored")
  @Expose
  private Object isSponsored;
  @SerializedName("LogoUrl")
  @Expose
  private String logoUrl;


}