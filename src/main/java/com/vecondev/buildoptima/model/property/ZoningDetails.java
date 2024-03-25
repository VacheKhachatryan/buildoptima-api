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
public class ZoningDetails implements Serializable {

  @Serial private static final long serialVersionUID = -3310546386502606480L;

  private GeneralInformation generalInformation;
  private SpecialConditions specialConditions;
  private HillsideInformation hillsideInformation;
  private HistoricDesignations historicDesignations;
  private NearestTransit nearestTransit;
  private FireRelated fireRelated;
  private Cases cases;

  @Data
  private static class GeneralInformation implements Serializable {
    @Serial private static final long serialVersionUID = 8958826365148119858L;
    private String lotArea;
    private String usableLotArea;
    private Set<String> zoneCodes;
    private String heightDistrict;
    private Set<String> specificPlanAreaNames;
    private Set<String> supplementalUseDistricts;
    private String generalPlanLandUse;
    private String coastalZone;
    private String environmentallySensitiveArea;
  }

  @Data
  private static class SpecialConditions implements Serializable {
    @Serial private static final long serialVersionUID = 4089599440218158429L;
    private String buildingLine;
    private String tcondition;
    private String dlimitation;
    private String qcondition;
  }

  @Data
  private static class HillsideInformation implements Serializable {
    @Serial private static final long serialVersionUID = 6806836836489236183L;
    private String hillsideGradingArea;
    private String hillsideOrdinanceArea;
    private String hillsideConstructionRegulations;
    private String hillsideMountainousArea;
  }

  @Data
  private static class HistoricDesignations implements Serializable {
    @Serial private static final long serialVersionUID = -5689042837483809602L;
    private String historicPreservationZoneName;
    private String historicMonumentName;
  }

  @Data
  private static class NearestTransit implements Serializable {
    @Serial private static final long serialVersionUID = 2433072553948720857L;
    private Double distanceToNearestBusStopInMi;
    private String nearestBusStopCoordinate;
    private Double distanceToNearestMetroStationInMi;
    private String nearestMetroStationCoordinate;
  }

  @Data
  private static class FireRelated implements Serializable {
    @Serial private static final long serialVersionUID = 4334702767777200835L;
    private String veryHighFireHazardSeverityZone;
    private String fireBrushClearanceZone;
    private String fireHydrant;
  }

  @Data
  private static class Cases implements Serializable {
    @Serial private static final long serialVersionUID = -4110315330388275847L;
    private Set<String> cityPlanningCases;
    private Set<String> environmentalCases;
    private Set<String> ordinances;
    private Set<String> ziReports;
    private Set<String> zoningAdminCases;
    private Set<String> otherCases;
  }
}
