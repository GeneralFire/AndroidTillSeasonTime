package SeasonTime

import java.text.SimpleDateFormat
import java.util.*

class TillSeasonTime {

    // should i use global calendar or get instance in every single method?
    private val calendar: Calendar = Calendar.getInstance()

    fun getCurrentSeason(): SeasonsEnum {
        val date = Date()
        return getSeasonByDate(date)
    }

    private fun getSeasonByDate(date: Date): SeasonsEnum {
        calendar.setTime(date)

        return when (val month: Int = calendar.get(Calendar.MONTH)) {
            Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER -> SeasonsEnum.Fall
            Calendar.DECEMBER, Calendar.JANUARY, Calendar.FEBRUARY -> SeasonsEnum.Winter
            Calendar.MARCH, Calendar.APRIL, Calendar.MAY -> SeasonsEnum.Spring
            Calendar.JUNE, Calendar.JULY, Calendar.AUGUST -> SeasonsEnum.Summer
            else -> throw IllegalStateException("Invalid value ${month}. Cannot get season")
        }
    }

    private fun getSeasonFirstMonth(season: SeasonsEnum): Int =
        when (season) {
            SeasonsEnum.Spring -> Calendar.MARCH
            SeasonsEnum.Winter -> Calendar.DECEMBER
            SeasonsEnum.Summer -> Calendar.JUNE
            SeasonsEnum.Fall -> Calendar.SEPTEMBER
        }
    
    private fun getDateDiff(subtrahend: Date, subtractor: Date): Long = subtrahend.getTime() - subtractor.getTime()

    private fun getTimeTillNearestSeason(targetSeason: SeasonsEnum, currentTime: Date): Long {
        val targetMonth: Int = getSeasonFirstMonth(targetSeason)

        val currentDateCal = Calendar.getInstance()
        currentDateCal.setTime(currentTime)
//        println("currentDateCal is ${SimpleDateFormat("HH:mm:ss dd MMMM YYYY").format(currentDateCal.getTime())}")
//        Log.d("TillSeasonTime", "nextSeasonCal is ${SimpleDateFormat("dd:HH:mm:ss MMMM YYYY").format(currentDateCal.getTime())}")

        val nextSeasonCal = Calendar.getInstance()
        nextSeasonCal.set(
            // lol, ternary operator?
            currentDateCal.get(Calendar.YEAR) + if (currentDateCal.get(Calendar.MONTH) > targetMonth) 1  else 0,
            targetMonth,
            nextSeasonCal.getActualMinimum(Calendar.DAY_OF_MONTH),
            nextSeasonCal.getActualMinimum(Calendar.HOUR_OF_DAY),
            nextSeasonCal.getActualMinimum(Calendar.MINUTE),
            nextSeasonCal.getActualMinimum(Calendar.SECOND),
        )
//         println("nextSeasonCal is ${SimpleDateFormat("HH:mm:ss dd MMMM YYYY").format(nextSeasonCal.getTime())}")
//        Log.d("TillSeasonTime", "nextSeasonCal is ${nextSeasonCal.toString()}")
        return getDateDiff(Date(nextSeasonCal.timeInMillis), Date(currentDateCal.timeInMillis))
    }

    fun getFormatedTimeTillSeason(targetSeason: SeasonsEnum, currentTime: Date? = null): String {
        val timeArg = currentTime?: Date()
        val delta: Long = getTimeTillNearestSeason(targetSeason, timeArg)
        val seconds: Long = delta / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return days.toString() + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60
    }
}

//fun main() {
//    println("Hello, World!")
//    val date = Date()
//
//    val a = TillSeasonTime()
//    println(a.getFormatedTimeTillSeason(SeasonsEnum.Spring, date))
//}