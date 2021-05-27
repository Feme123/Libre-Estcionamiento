package parking

import java.util.*

val scanner = Scanner(System.`in`)

fun main() {
    Parking.start()
}

object Parking {
     lateinit var parking: ParkingLot

    fun start() {
        with(scanner) { while (true) {
            println(when (val command = next()) {
                "exit" -> return
                "create" -> {
                    val spots = nextInt()
                    parking = ParkingLot(spots)
                    "Created a parking lot with $spots spots."
                }
                else ->
                    if (::parking.isInitialized) with(parking) { when (command) {
                        "park" -> park(next(), next())
                        "leave" -> leave(nextInt())
                        "status" -> toString()
                        "reg_by_color" -> regByColor(next())
                        "spot_by_color" -> spotByColor(next())
                        "spot_by_reg" -> spotByReg(next())
                        else -> "No such method!"
                    } }
                    else { nextLine(); "Sorry, a parking lot has not been created." }
            })
        } }
    }
}

class ParkingLot(private val size: Int) {
    private val lot = mutableMapOf<Int, Car>()

    fun park(regN: String, color: String): String {
        val freeSpot = (1..size).firstOrNull { it !in lot } ?: 1

        return if (lot.size == size) "Sorry, the parking lot is full." else {
            lot[freeSpot] = Car(regN, color)
            "$color car parked in spot $freeSpot."
        }
    }

    fun leave(spot: Int) = if (lot.containsKey(spot)) {
        lot.remove(spot)
        "Spot $spot is free."
    } else "There is no car in spot $spot."

    fun regByColor(color: String) = lot.values.filter {
        it.color.equals(color, true) }.joinToString(", ") { it.regN }
        .ifEmpty { "No cars with color $color were found." }

    fun spotByColor(color: String) = lot.filter {
        it.value.color.equals(color, true) }.map { it.key }.joinToString(", ")
        .ifEmpty { "No cars with color $color were found." }

    fun spotByReg(regN: String) = lot.filter { it.value.regN == regN }.map { it.key }
        .singleOrNull() ?: "No cars with registration number $regN were found."

    override fun toString(): String {
        val state = mutableListOf<String>()
        lot.forEach { with(it) { state.add("$key $value") } }
        return state.joinToString("\n").ifEmpty { "Parking lot is empty." }
    }
}

data class Car(val regN: String, val color: String) {
    override fun toString() = "$regN $color"
}





