package hlmg.hexagonal;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "hlmg.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    @ArchTest
    void hexagonalArchitectureWithOnion(JavaClasses classes) {
        Architectures.onionArchitecture()
                .withOptionalLayers(true)
                .domainModels("hlmg.hexagonal.domain..")
                .applicationServices("hlmg.hexagonal.application..")
                .adapter("integration", "hlmg.hexagonal.adapter.integration..")
                .adapter("security", "hlmg.hexagonal.adapter.security..")
                .adapter("webapi", "hlmg.hexagonal.adapter.webapi..")
                .check(classes);
    }

}
