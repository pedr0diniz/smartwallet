package br.com.zinid.smartwallet

import com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

internal class PackageDependencyTest {

    private val importedClassesRoot = ClassFileImporter().importPackages("br.com.zinid.smartwallet")

    @Test
    fun `package domain should not depend on classes from application package`() {
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..")
            .check(importedClassesRoot)
    }

    @Test
    fun `input ports should only be implemented by use cases`() {
        classes().that().implement(simpleNameEndingWith("InputPort"))
            .should().haveSimpleNameEndingWith("UseCase")
            .check(importedClassesRoot)
    }

    @Test
    fun `input ports should only be accessed by controllers`() {
        classes().that().haveSimpleNameEndingWith("InputPort")
            .should().onlyBeAccessed().byClassesThat().haveSimpleNameEndingWith("Controller")
            .check(importedClassesRoot)
    }

    @Test
    fun `output ports should only be implemented by adapters`() {
        classes().that().implement(simpleNameEndingWith("OutputPort"))
            .should().haveSimpleNameEndingWith("Adapter")
            .check(importedClassesRoot)
    }

    @Test
    fun `output ports should only be accessed by input ports or other output port implementations`() {
        classes().that().haveSimpleNameEndingWith("OutputPort")
            .should().onlyBeAccessed().byClassesThat().haveNameMatching(".*(UseCase|Adapter)")
            .check(importedClassesRoot)
    }

    @Test
    fun `jpa repositories should only be accessed by adapters`() {
        classes().that().haveSimpleNameEndingWith("Repository")
            .and().resideInAPackage("..adapter..")
            .should().onlyBeAccessed().byClassesThat().haveSimpleNameEndingWith("Adapter")
            .check(importedClassesRoot)
    }
}
