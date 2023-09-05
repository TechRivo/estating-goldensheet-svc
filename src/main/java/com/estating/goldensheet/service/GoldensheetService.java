package com.estating.goldensheet.service;

import com.estating.goldensheet.dto.SaveRequest;
import com.estating.goldensheet.dto.SaveRequestFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.estating.goldensheet.dto.SaveRequestFactory.SUPPORTED_FIELDS;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;

@Service
public class GoldensheetService {

    public SaveRequest parseGoldensheet(byte[] file) {
        try (var workbook = new XSSFWorkbook(new ByteArrayInputStream(file))) {

            var sheet = workbook.getSheet("GoldenSheet");
            if (sheet == null) {
                throw new IllegalStateException("GoldenSheet sheet not found");
            }

            var headerRow = sheet.getRow(0);
            if (headerRow == null || headerRow.getLastCellNum() < 0) {
                throw new IllegalStateException("Header row not found");
            }

            var fieldCol = findHeaderCol(headerRow, "Field");
            var valueCol = findHeaderCol(headerRow, "Value");

            if (fieldCol < 0 || valueCol < 0) {
                throw new IllegalStateException("Field and/or Value columns not found");
            }

            var fields = IntStream.range(1, sheet.getLastRowNum())
                    .mapToObj(sheet::getRow)
                    .filter(Objects::nonNull)
                    .filter(row -> isValidField(row, fieldCol))
                    .collect(Collectors.toMap(
                            row -> row.getCell(fieldCol).toString(),
                            row -> row.getCell(valueCol, RETURN_BLANK_AS_NULL).toString()
                    ));

            return SaveRequestFactory.create(fields);

        } catch (IOException e) {
            throw new IllegalStateException("Could not parse supplied file");
        }
    }

    private int findHeaderCol(XSSFRow row, String header) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            var cell = row.getCell(i);
            if (StringUtils.equalsIgnoreCase(cell.toString(), header)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isValidField(XSSFRow row, int fieldCol) {
        var field = row.getCell(fieldCol, RETURN_BLANK_AS_NULL);
        if (field == null) {
            return false;
        }

        return SUPPORTED_FIELDS.keySet().stream()
                .anyMatch(it -> StringUtils.equalsIgnoreCase(field.toString(), it));
    }

}
