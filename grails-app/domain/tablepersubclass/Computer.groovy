package tablepersubclass

class Computer {

    static belongsTo = [programmer: Programmer]

    static constraints = {
    }
}
