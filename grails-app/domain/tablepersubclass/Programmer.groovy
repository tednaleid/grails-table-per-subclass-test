package tablepersubclass

class Programmer extends Person {

    BigDecimal yearlySalary

    static hasMany = [computers: Computer]

    static constraints = {
    }
}
