package com.smartenglishbackend.service;

import java.time.LocalDate;
import java.util.List;

public interface ICalcData {
    boolean accept(String method);
    List<Float> calc(LocalDate startTime, String duration);
}
