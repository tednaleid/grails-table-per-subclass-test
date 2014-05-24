package tablepersubclass

class Student extends Person {

    // both teacher and student share this value
    Integer gradeLevel

    static belongsTo = [teacher: Teacher]

    static hasMany = [friends: Student]

    static constraints = {
    }
}
