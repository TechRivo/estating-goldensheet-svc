package com.estating.goldensheet.domain;

import com.estating.goldensheet.util.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum TeaserStatus {

    TRANSACTION_EXAMPLE,
    STRICTLY_PRELIMINARY,
    PRICING_SUPPLEMENT_AVALILABLE,
    CLOSED_TRANSACTION,
    SEARCH_MANDATE;

    private static final Map<String, TeaserStatus> NORMALIZED = Arrays.stream(TeaserStatus.values())
            .collect(Collectors.toMap(
                    it -> EnumUtils.normalize(it.name()),
                    it -> it
            ));

    public static TeaserStatus fromString(String str) {
        var normalized = EnumUtils.normalize(str);

        return NORMALIZED.entrySet()
                .stream()
                .filter(it -> StringUtils.equalsIgnoreCase(it.getKey(), normalized))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
