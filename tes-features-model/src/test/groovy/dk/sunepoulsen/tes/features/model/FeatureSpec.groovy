package dk.sunepoulsen.tes.features.model

import dk.sunepoulsen.tes.rest.models.validation.DefaultValidator
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudRead
import dk.sunepoulsen.tes.testdata.generators.TestDataGenerator
import dk.sunepoulsen.tes.testdata.generators.TimeGenerators
import dk.sunepoulsen.tes.validation.tests.ConstraintViolationAssertions
import dk.sunepoulsen.tes.validation.tests.ExpectedConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.groups.Default
import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZonedDateTime

class FeatureSpec extends Specification {

    private DefaultValidator validator
    private TestDataGenerator<ZonedDateTime> datetimeGenerator

    void setup() {
        this.validator = new DefaultValidator()
        this.datetimeGenerator = TimeGenerators.currentZonedDateTimeGenerator()
    }

    void "Validate feature that is valid"() {
        given:
            Feature model = Feature.builder()
                .key('key')
                .name('name')
                .build()

        when:
            this.validator.validate(model)

        then:
            noExceptionThrown()
    }

    @Unroll
    void "Validate feature that is invalid: #_testcase"() {
        given:
            Feature model = Feature.builder()
                .key(_key)
                .name(_name)
                .build()

        when:
            this.validator.validate(model)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, _errors)

        where:
            _testcase      | _key  | _name  | _errors
            'key is null'  | null  | 'name' | [new ExpectedConstraintViolation('key', 'must not be null')]
            'name is null' | 'key' | null   | [new ExpectedConstraintViolation('name', 'must not be null')]
    }

    void "Validate feature with invalid activations"() {
        given:
            Feature model = Feature.builder()
                .key('key')
                .name('name')
                .activations(List.of(FeatureActivation.builder()
                    .id(10L)
                    .enabled(null)
                    .build()
                ))
                .build()

        when:
            this.validator.validate(model, Default, OnCrudRead)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ConstraintViolationAssertions.verifyViolations(exception.constraintViolations, [
                new ExpectedConstraintViolation('activations[0].enabled', 'must not be null')
            ])
    }

}
