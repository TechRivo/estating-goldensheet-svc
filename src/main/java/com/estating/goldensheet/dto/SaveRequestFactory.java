package com.estating.goldensheet.dto;

import com.estating.goldensheet.domain.SubscriptionStatus;
import com.estating.goldensheet.domain.TeaserStatus;
import com.estating.goldensheet.dto.SaveRequest.SaveRequestBuilder;

import java.util.Map;
import java.util.function.BiConsumer;

public class SaveRequestFactory {

    public static Map<String, BiConsumer<String, SaveRequestBuilder>> SUPPORTED_FIELDS = Map.of(
            "Internal Name", (value, builder) -> builder.internalName(value),
            "Investment Status", (value, builder) -> builder.status(SubscriptionStatus.fromString(value)),
            "Transaction Status", (value, builder) -> builder.teaserStatus(TeaserStatus.fromString(value)),
            "Open/Sold percentage", (value, builder) -> builder.soldPercentage(value),
            "Open/sold value", (value, builder) -> builder.openValue(value),
            "RELINC Price", (value, builder) -> builder.relincPrice(value)
    );

    public static SaveRequest create(Map<String, String> fields) {
        var builder = SaveRequest.builder();

        SUPPORTED_FIELDS.entrySet().stream()
                .filter(entry -> fields.get(entry.getKey()) != null)
                .forEach(entry -> {
                    var value = fields.get(entry.getKey());
                    entry.getValue().accept(value, builder);
                });

        return builder.build();
    }

}
