package com.vecondev.buildoptima.model.property;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hazards implements Serializable {

  @Serial private static final long serialVersionUID = -5942760421214222815L;

  private SeismicHazards seismicHazards;
  private NaturalHazards naturalHazards;
  private OtherHazards otherHazards;

  @Data
  private static class SeismicHazards implements Serializable {
    @Serial private static final long serialVersionUID = -5601227814868936933L;
    private String nearActiveFaultName;
    private Integer nearActiveFaultDistance;
    private String alquistPrioloFaultZone;
    private String landslide;
    private String liquefaction;
  }

  @Data
  private static class NaturalHazards implements Serializable {
    @Serial private static final long serialVersionUID = -7919659737655560551L;
    private String veryHighFireHazardSeverityZone;
    private String highWindArea;
    private String floodZone;
    private String coastalZone;
    private String methaneProducingLandfill;
  }

  @Data
  private static class OtherHazards implements Serializable {
    @Serial private static final long serialVersionUID = -9210213736934824274L;
    private String airportInfluenceArea;
    private String aviationNoise;
    private String freewayHazard;
    private String railNoise;
    private String highVoltageLineWithinRange;
    private Integer highVoltageLineProximityInFt;
    private String highVoltageLineType;
    private Integer highVoltageLineValueInKiloVoltage;
  }
}
