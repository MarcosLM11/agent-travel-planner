package com.marcos.myagenttravelplannerapp.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.marcos.myagenttravelplannerapp",
        importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    private static final String DOMAIN = "..domain..";
    private static final String MEMORY = "..memory..";
    // Use fully-qualified path to avoid matching com.embabel.agent.spi (Embabel framework)
    private static final String AGENT = "com.marcos.myagenttravelplannerapp.agent..";

    @ArchTest
    static final ArchRule domainShouldNotDependOnAgent =
            noClasses().that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage(AGENT)
                    .because("Domain must not depend on agent orchestration layer");

    @ArchTest
    static final ArchRule domainShouldNotDependOnMemory =
            noClasses().that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage(MEMORY)
                    .because("Domain must not depend on memory/infrastructure layer");

    @ArchTest
    static final ArchRule memoryShouldNotDependOnAgent =
            noClasses().that().resideInAPackage(MEMORY)
                    .should().dependOnClassesThat().resideInAPackage(AGENT)
                    .because("Memory/infrastructure must not depend on agent orchestration layer");

    @ArchTest
    static final ArchRule domainShouldNotUseSpringAnnotations =
            noClasses().that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "org.springframework.stereotype..",
                            "org.springframework.web..")
                    .because("Domain records must be framework-agnostic POJOs");

    @ArchTest
    static final ArchRule domainClassesShouldBeRecords =
            classes().that().resideInAPackage(DOMAIN)
                    .and().areNotEnums()
                    .should().beRecords()
                    .because("Domain types must be immutable records");
}