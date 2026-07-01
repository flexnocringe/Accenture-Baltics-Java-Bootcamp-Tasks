package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class ReportExportService {

    public void writeReport(Path outputPath, ShelterReportData reportData) throws IOException {
        // TODO Step 4:
        // 1) Write upload-report.txt in required format.
        // 2) Include generated date, imported/skipped totals.
        // 3) Include unique species and per-species breakdown.
        // 4) Include oldest animal per species.
        // 5) Include animalsNeedingVetInput as name(species), name2(species2).
        // 6) Use UTF-8 and try-with-resources.

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write("=== Shelter Intake Report ===\n");
            writer.write("Report generated on: " + LocalDate.now() + "\n\n");
            writer.write("Total animals imported: " + reportData.importResult().allAnimals().size() + "\n");
            writer.write("Total animals skipped: " + reportData.importResult().skippedRows() + "\n\n");
            writer.write("--- Unique species ---\n");
            writer.write(String.join(", ", reportData.uniqueSpecies()));
            writer.write("\n\n--- Per species breakdown ---\n");
            for (String species : reportData.uniqueSpecies()) {
                writer.write(String.format("%s:\t%d total, %d vaccinated, %d unvaccinated%n", species, reportData.animalsBySpecies().get(species).size(), reportData.vaccinatedPerSpecies().get(species), reportData.unvaccinatedPerSpecies().get(species)));
            }
            writer.write("\n\n--- Oldest per species ---\n");
            for (String species : reportData.uniqueSpecies()) {
                writer.write(String.format("%s:\t%s%n", species, reportData.oldestAnimalPerSpecies().get(species)));
            }
            writer.write("\n\n--- Needs vet input to determine age ---\n");
            writer.write(String.join(", ", reportData.animalsNeedingVetInput()));

        } catch (IOException e) {
            throw new IOException("Error writing report to " + outputPath, e);
        }
    }
}
