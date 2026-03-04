package nl.healthri.fairdatapoint.search.apps;

import nl.healthri.fairdatapoint.search.FdpUrls;
import picocli.CommandLine;

import java.net.URI;
import java.nio.file.Path;

import static picocli.CommandLine.*;


@Command(name = "FdpTools",
        description = "FdpTools",
        subcommands = {SaveCommand.class, DumpCommand.class, ValidateCommand.class, SearchCommand.class, PrintCommand.class, CommandLine.HelpCommand.class}
)
public class FdpTools {

    @ArgGroup(multiplicity = "1", exclusive = false)
    fdpGroup group;

    @Spec
    Model.CommandSpec spec;

    static void main(String... args) {
        int exitCode = new CommandLine(new FdpTools()).execute(args);
        System.exit(exitCode);
    }

    enum Profile {
        /**
         * List of known validation profiles for the shacl-validationw webservice.
         */
        HEALTH_RI("https://www.itb.ec.europa.eu/shacl/healthri/api/validateMultiple", "v1.0.0"),
        HEALTH_RI_V2("https://www.itb.ec.europa.eu/shacl/healthri/api/validateMultiple", "v2.0.0"),
        DCAT_AP2("https://www.itb.ec.europa.eu/shacl/dcat-ap/api/validateMultiple", "v2.0"),
        DCAT_AP3_BASE("https://www.itb.ec.europa.eu/shacl/dcat-ap/api/validateMultiple", "v3.0Base0"),
        DCAT_AP3_FULL("https://www.itb.ec.europa.eu/shacl/dcat-ap/api/validateMultiple", "v3.Full"),
        HEALTH_DCAT("https://health-data-itb-rdf-validator.acceptance.data.health.europa.eu/shacl/ehds/api/validateMultiple", "healthdcatap");
        public final String url;
        public final String profile;

        Profile(String url, String profile) {
            this.url = url;
            this.profile = profile;
        }
    }

    public static class fdpGroup {
        @Option(names = {"--fdp-url", "-u"},
                defaultValue = "https://fdp-test.healthdata.nl/",
                description = "Fair datapoint url (default: ${DEFAULT-VALUE})")
        public URI url;

        @Option(names = "--fdp",
                description = "Valid values: ${COMPLETION-CANDIDATES}")
        public FdpUrls urlEnum;

        public URI uri() {
            return urlEnum != null ? urlEnum.uri : url;
        }
    }
}

@Command(name = "save", description = "Save the content of the FDP to disk")
class SaveCommand implements Runnable {

    @Option(names = {"--folder", "-f"},
            description = "folder location where the turtle files will be saved.",
            required = true)
    public Path folder;

    @ParentCommand
    FdpTools parent;

    @Override
    public void run() {
        FdpSaveToDiskApp.run(parent.group.uri(), folder);
    }
}

@Command(name = "dump", description = "Dump the content of the FDP to a single file")
class DumpCommand implements Runnable {

    @Option(names = {"--file", "-f"},
            description = "File used to save to contect of the FDP",
            required = true)
    public Path file;

    @ParentCommand
    FdpTools parent;

    @Override
    public void run() {
        FdpDumpToFileApp.run(parent.group.uri(), file);
    }
}


@Command(name = "print", description = "Print the content of the FDP to screen")
class PrintCommand implements Runnable {

    @ParentCommand
    FdpTools parent;

    @Override
    public void run() {
        FdpPrintApp.run(parent.group.uri());
    }
}

@Command(name = "validate", description = "Validate all document from the FDP to a profile")
class ValidateCommand implements Runnable {

    @Option(names = {"-p", "--profile"},
            required = true,
            description = "Valid values: ${COMPLETION-CANDIDATES}")
    public FdpTools.Profile profile;

    @ParentCommand
    FdpTools parent;

    @Override
    public void run() {
        FdpValidateRdfApp.run(parent.group.uri(), profile.url, profile.profile);
    }
}

@Command(name = "search", description = "search in description using embeddings")
class SearchCommand implements Runnable {

    @Option(names = {"-m", "--model"},
            required = true,
            defaultValue = "C:\\Users\\PatrickDekker(Health\\OneDrive - Health-RI\\Bureaublad\\UniSenEmb\\",
            description = "Universal Sentence Encoder Model (default: ${DEFAULT-VALUE})")
    public Path model;

    @ParentCommand
    FdpTools parent;

    @Override
    public void run() {
        FdpSearchApp.run(parent.group.uri(), model);
    }
}
