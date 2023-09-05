package com.estating.goldensheet.dto;

import com.estating.goldensheet.domain.SubscriptionStatus;
import com.estating.goldensheet.domain.TeaserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveRequest {

    private String internalName;

    private SubscriptionStatus status;

    private TeaserStatus teaserStatus;

    private String soldPercentage;

    private String openValue;

    private String relincPrice;

}
