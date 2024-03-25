package com.vecondev.buildoptima.model.property;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class Polygon implements Serializable {

  @Serial private static final long serialVersionUID = -8786962878612318996L;
  private Set<Coordinate> coordinates;
  private Set<EdgesLabel> edgesLabels;

  @Data
  private static class Coordinate implements Serializable {
    @Serial private static final long serialVersionUID = -3820613662249072260L;
    private double lat;
    private double lng;
  }

  @Data
  private static class EdgesLabel implements Serializable {
    @Serial private static final long serialVersionUID = 1631488052944067524L;
    private String label;
    private double lat;
    private double lng;
  }
}
