package tablepersubclass

import spock.lang.Specification

class TablePerSubclassSpec extends Specification {

    Person person
    Programmer programmer
    Teacher teacher
    Student student

    void setup() {
        person = Person.build(firstName: "Alpha")
        programmer = Programmer.build(firstName: "Bravo")
        teacher = Teacher.build(firstName: "Charlie")
        student = Student.build(firstName: "Delta", teacher: teacher)
    }

    void "All person classes can be built"() {
        expect:
        assert Person.count() == 4
        assert Person.list(sort: "firstName") == [person, programmer, teacher, student]
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
}