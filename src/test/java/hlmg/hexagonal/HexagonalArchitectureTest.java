package hlmg.hexagonal;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaFieldAccess;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.dependencies.Slice;
import hlmg.hexagonal.domain.shared.AbstractEntity;
import jakarta.persistence.Entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "hlmg.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    private static final Set<String> OBJECT_METHOD_NAMES = Set.of("toString", "equals", "hashCode");
    private static final Pattern GETTER_PATTERN = Pattern.compile("^get[A-Z].*");
    private static final Pattern BOOLEAN_QUERY_PATTERN = Pattern.compile("^(is|has|can|matches)[A-Z].*");
    private static final Pattern DOMAIN_ASSERTION_PATTERN = Pattern.compile("^(ensure|validate|check)[A-Z].*");

    @ArchTest
    void hexagonalArchitectureWithOnion(JavaClasses classes) {
        onionArchitecture()
                .withOptionalLayers(true)
                .domainModels("hlmg.hexagonal.domain..")
                .applicationServices("hlmg.hexagonal.application..")
                .adapter("integration", "hlmg.hexagonal.adapter.integration..")
                .adapter("security", "hlmg.hexagonal.adapter.security..")
                .adapter("webapi", "hlmg.hexagonal.adapter.webapi..")
                .check(classes);
    }

    @ArchTest
    void aggregateFreeOfCycles(JavaClasses classes) {
        slices().matching("hlmg.hexagonal.domain.(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }

    @ArchTest
    void applicationServiceFreeOfCycles(JavaClasses classes) {
        slices().matching("hlmg.hexagonal.application.(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }

    @ArchTest
    void aggregateDependencies(JavaClasses classes) {
        slices()
                .matching("hlmg.hexagonal.domain.(*)..")
                .should(onlyCallGettersOrRecordMethodsOfOtherSlices())
                .check(classes);
    }

    @ArchTest
    void adapterCanOnlyReadEntities(JavaClasses classes) {
        classes()
                .that().resideInAPackage("hlmg.hexagonal.adapter..")
                .should(onlyCallGettersOrQueryMethodsOfEntities())
                .check(classes);
    }

    private ArchCondition<Slice> onlyCallGettersOrRecordMethodsOfOtherSlices() {
        return new ArchCondition<>("only call getters, query methods, or record/enum methods of other aggregates") {
            private final Set<JavaClass> classesInAnySlice = new HashSet<>();

            @Override
            public void init(Collection<Slice> allSlices) {
                allSlices.forEach(classesInAnySlice::addAll);
            }

            @Override
            public void check(Slice slice, ConditionEvents events) {
                Predicate<JavaClass> isExternalAggregate = target ->
                        !slice.contains(target)
                                && classesInAnySlice.contains(target)
                                && !target.isRecord()
                                && !target.isEnum();

                for (JavaClass javaClass : slice) {
                    checkReadOnlyDependencies(javaClass, isExternalAggregate, events);
                }
            }
        };
    }

    private ArchCondition<JavaClass> onlyCallGettersOrQueryMethodsOfEntities() {
        return new ArchCondition<>("only call getters or query methods of entities") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                checkReadOnlyDependencies(javaClass, HexagonalArchitectureTest::isEntity, events);
            }
        };
    }

    private static void checkReadOnlyDependencies(JavaClass sourceClass, Predicate<JavaClass> targetFilter, ConditionEvents events) {
        for (JavaMethodCall call : sourceClass.getMethodCallsFromSelf()) {
            JavaClass targetOwner = call.getTargetOwner();
            if (targetFilter.test(targetOwner) && !isAllowedQueryMethod(call)) {
                String message = String.format("Class '%s' calls non-query method '%s' of target '%s'",
                        sourceClass.getSimpleName(), call.getTarget().getFullName(), targetOwner.getSimpleName());
                events.add(SimpleConditionEvent.violated(call, message));
            }
        }

        for (JavaFieldAccess access : sourceClass.getFieldAccessesFromSelf()) {
            JavaClass targetOwner = access.getTargetOwner();
            if (targetFilter.test(targetOwner) && access.getAccessType() == JavaFieldAccess.AccessType.SET) {
                String message = String.format("Class '%s' directly modifies field '%s' of target '%s'",
                        sourceClass.getSimpleName(), access.getName(), targetOwner.getSimpleName());
                events.add(SimpleConditionEvent.violated(access, message));
            }
        }
    }

    private static boolean isEntity(JavaClass javaClass) {
        return javaClass.isAssignableTo(AbstractEntity.class) || javaClass.isAnnotatedWith(Entity.class);
    }

    private static boolean isAllowedQueryMethod(JavaMethodCall call) {
        String methodName = call.getTarget().getName();
        JavaClass returnType = call.getTarget().getRawReturnType();

        if (isObjectMethod(methodName)) return true;
        if (isGetter(methodName, returnType)) return true;
        if (isBooleanQuery(methodName, returnType)) return true;
        return isDomainAssertion(methodName);
    }

    private static boolean isObjectMethod(String methodName) {
        return OBJECT_METHOD_NAMES.contains(methodName);
    }

    private static boolean isGetter(String methodName, JavaClass returnType) {
        return GETTER_PATTERN.matcher(methodName).matches() && !isVoid(returnType);
    }

    private static boolean isBooleanQuery(String methodName, JavaClass returnType) {
        return BOOLEAN_QUERY_PATTERN.matcher(methodName).matches() && isBooleanType(returnType);
    }

    private static boolean isDomainAssertion(String methodName) {
        return DOMAIN_ASSERTION_PATTERN.matcher(methodName).matches();
    }

    private static boolean isVoid(JavaClass returnType) {
        return returnType.getName().equals("void");
    }

    private static boolean isBooleanType(JavaClass returnType) {
        return returnType.getName().equals("boolean") || returnType.getName().equals("java.lang.Boolean");
    }

}
