package tablepersubclass

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(Person)
@Build([Organization, Person, Programmer, Teacher, Student])
class TablePerSubclassUnitSpec extends Specification {

    Person person
    Programmer programmer
    Teacher teacher
    Student student

    Organization organization

    void setup() {
        organization = Organization.build(name: "The Order")
        person = Person.build(firstName: "Alpha", organization: organization)
        programmer = Programmer.build(firstName: "Bravo", organization: organization)
        teacher = Teacher.build(firstName: "Charlie")
        student = Student.build(firstName: "Delta", teacher: teacher)
    }

    void "All person classes can be built"() {
        expect:
        assert Person.count() == 4
        List<Person> people = Person.list(sort: "firstName")
        assert people == [person, programmer, teacher, student]
        assert Programmer.count() == 1
        assert Teacher.count() == 1
        assert Student.count() == 1
    }

    void "Person finder returns subclasses not Person object"() {
        expect:
        assert Person.findByFirstName("Alpha").class == Person
        assert Person.findByFirstName("Bravo").class == Programmer
        assert Person.findByFirstName("Charlie").class == Teacher
        assert Person.findByFirstName("Delta").class == Student
    }

    void "Person and Person subclasses belong to Organization"() {
        expect:
        assert person.organization == organization
        assert programmer.organization == organization
        assert person.organization == programmer.organization

        assert Programmer.findAllByOrganization(organization).contains(programmer)
        assert Person.findAllByOrganization(organization).contains(person)
    }
}
