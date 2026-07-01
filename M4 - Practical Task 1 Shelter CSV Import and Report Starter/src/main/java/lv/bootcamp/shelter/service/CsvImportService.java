package lv.bootcamp.shelter.service;

import lombok.extern.slf4j.Slf4j;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvImportService {

    public ImportResult importAnimals(Path inputPath) throws IOException {
        log.info("Starting import from {}", inputPath);

        List<Animal> allAnimals = new ArrayList<>();

        // TODO Step 1:
        // 1) Read intake.csv with UTF-8.
        // 2) Skip header row.
        // 3) Skip malformed rows and log warnings.
        // 4) Allow blank age as unknown (null), but reject non-numeric age values.
        // 5) Parse intakeDate using DateTimeFormatter.
        // 6) Map each row to Animal object.
        int skippedRows = 0;
        try (BufferedReader br = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            String[] headers = br.readLine().split(",");

            String line;
            while ((line = br.readLine()) != null) {
                boolean isMalformed = false;
                String[] parts = line.split(",", -1);
                if (parts.length != 5) {
                    log.warn("Row does not have enough columns: {}", line);
                    skippedRows++;
                    isMalformed = true;
                }
                if (!isMalformed) {
                    for (int i = 0; i < 5; i++) {
                        if (parts[i].trim().isEmpty() && i != 2) {
                            log.warn("Row has empty required values: {}", line);
                            skippedRows++;
                            isMalformed = true;
                            break;
                        }
                    }
                }
                if (isMalformed) {
                    continue;
                }
                Integer age = null;
                LocalDate intakeDate = null;
                try {
                    age = Integer.parseInt(parts[2].trim());
                    intakeDate = LocalDate.parse(parts[4].trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                } catch (NumberFormatException e) {
                    if (!parts[2].trim().isEmpty()) {
                        log.warn("Row has non-numeric age value: {}", line);
                        skippedRows++;
                        continue;
                    }
                } catch (DateTimeParseException e) {
                    log.warn("Row has invalid date format: {}", line);
                    skippedRows++;
                    continue;
                }
                String name = parts[0].trim();
                String species = parts[1].trim();
                boolean vaccinated = Boolean.parseBoolean(parts[3].trim());
                allAnimals.add(new Animal(name, species, age, vaccinated, intakeDate));
            }
        } catch (IOException e) {
            throw  new IOException("Error reading from " + inputPath, e);
        }
        return new ImportResult(allAnimals, skippedRows);
    }
}
