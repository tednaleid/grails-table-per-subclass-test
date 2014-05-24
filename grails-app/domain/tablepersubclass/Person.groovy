package tablepersubclass

class Person {
    String firstName
    String lastName

    static hasMany = [houses: House]

    static mapping = {
        tablePerHierarchy false
    }

    static constraints = {
    }
}
