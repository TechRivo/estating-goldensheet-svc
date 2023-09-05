package com.estating.goldensheet.domain;

import com.estating.goldensheet.util.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum SubscriptionStatus {

    PRIMARY_MARKET,
    CLOSED_TRANSACTION,
    PRE_SUBSCRIPTION,
    SECONDARY_MARKET;

    private static final Map<String, SubscriptionStatus> NORMALIZED = Arrays.stream(SubscriptionStatus.values())
            .collect(Collectors.toMap(
                    it -> EnumUtils.normalize(it.name()),
                    it -> it
            ));

    public static SubscriptionStatus fromString(String str) {
        var normalized = EnumUtils.normalize(str);

        return NORMALIZED.entrySet()
                .stream()
                .filter(it -> StringUtils.equalsIgnoreCase(it.getKey(), normalized))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

}
