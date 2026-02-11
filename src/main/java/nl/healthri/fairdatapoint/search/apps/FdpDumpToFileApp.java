package nl.healthri.fairdatapoint.search.apps;

import nl.healthri.fairdatapoint.search.FairDataPointHarvester;
import nl.healthri.fairdatapoint.search.database.FdpRecordStore;
import nl.healthri.fairdatapoint.search.database.FdpRecordStoreInFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.file.Path;

public class FdpDumpToFileApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(FdpDumpToFileApp.class);

    public static void run(URI url, Path file) {
        LOGGER.info("Start saving FDP: {} ", url);

        try (FdpRecordStore database = new FdpRecordStoreInFile(file);
             FairDataPointHarvester harvester = new FairDataPointHarvester()) {

            harvester.harvest(url, database);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
