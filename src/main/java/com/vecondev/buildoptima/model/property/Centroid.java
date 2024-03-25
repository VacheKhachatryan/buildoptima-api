package com.vecondev.buildoptima.model.property;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
public class Centroid implements Serializable {

  @Serial private static final long serialVersionUID = -59621346509348655L;
  private double lat;
  private double lng;
}
