package nl.healthri.fairdatapoint.search.database;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.vocabulary.LDP;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FdpRecordStoreInFile implements FdpRecordStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(FdpRecordStoreInFile.class);
    private final LinkedHashModel model;
    private final FileWriter fileWriter;

    public FdpRecordStoreInFile(Path file) throws IOException {
        fileWriter = new FileWriter(file.toFile());
        model = new LinkedHashModel();
    }

    @Override
    public void store(FdpRecord rec) {
        Model m = rec.model;
        LOGGER.debug(rec.title());
        m.filter(null, null, null).stream()
                .filter(st -> !st.getPredicate().getNamespace().equals(LDP.NAMESPACE))
                .forEach(model::add);
    }

    @Override
    public void done() {
        
        Rio.write(model, fileWriter, RDFFormat.TURTLE);
    }

    @Override
    public void close() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
