package com.data_storage;

import java.util.List;
import java.util.stream.Collectors;

public class Patient {
    private List<PatientData> records;

    public List<PatientData> getRecords(long startTime, long endTime) {
        return records.stream()
                      .filter(record -> record.getTimestamp() >= startTime && record.getTimestamp() <= endTime)
                      .collect(Collectors.toList());
    }
}

