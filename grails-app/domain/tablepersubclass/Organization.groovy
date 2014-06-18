package tablepersubclass

class Organization {
    String name

    static hasMany = [members: Person]

    static constraints = {
    }
}
