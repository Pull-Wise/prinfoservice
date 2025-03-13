package com.pullwise.prinfoservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrAnalysisResponse {

    String fileName;
    String analysisPoints;

}
