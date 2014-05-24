package tablepersubclass

class Teacher extends Person {

    // both programmer and teacher share this value
    BigDecimal yearlySalary

    static hasOne = [whiteBoard: WhiteBoard]

    static hasMany = [students: Student]

    static constraints = {
    }
}
