package com.vecondev.buildoptima.model.property;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Details implements Serializable {

  @Serial private static final long serialVersionUID = 4938316858899458002L;

  private Land land;
  private Set<Building> buildings;
  private Improvements improvements;

  @Data
  private static class Land implements Serializable {
    @Serial private static final long serialVersionUID = -6801937910816015654L;
    private String lotUse;
    private String buildingType;
    private Double lotArea;
    private Set<String> zoningCodes;
    private String pue;
    private String horseLot;
  }

  @Data
  private static class Building implements Serializable {
    @Serial private static final long serialVersionUID = 8843121198109734863L;
    private String buildType;
    private Integer yearBuilt;
    private Double floorArea;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer lastRemodeled;
  }

  @Data
  private static class Improvements implements Serializable {
    @Serial private static final long serialVersionUID = -6445270391821087226L;
    private SewerAvailability sewerAvailability;
    private String swimmingPool;
    private String solarPanel;
    private Set<String> otherImprovements;

    @Data
    private static class SewerAvailability implements Serializable {
      @Serial private static final long serialVersionUID = -1687248015671964172L;
      private Boolean isSewerAvailable;
      private String sewerWyeMapFilename;
      private String sewerConnectedInfo;
    }
  }
}
