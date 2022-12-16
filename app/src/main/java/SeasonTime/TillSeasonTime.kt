package SeasonTime

import java.util.*

class TillSeasonTime {

    // should i use global calendar or get instance in every single method?
    private val calendar: Calendar = Calendar.getInstance()

    fun getCurrentSeason(): SeasonsEnum = getSeasonByDate(Date())

    fun getNextSeason(season: SeasonsEnum): SeasonsEnum = SeasonsEnum.values()[(season.ordinal + 1) % SeasonsEnum.values().size]

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
    
    private fun getDateDiff(subtrahend: Date, subtractor: Date): Long = subtrahend.time - subtractor.time

    private fun getNearestSeasonDate(season: SeasonsEnum, initialTime: Date): Date {
        val fistMonth: Int = getSeasonFirstMonth(season)
        val currentDateCal = Calendar.getInstance()
        currentDateCal.setTime(initialTime)

        val nextSeasonCal = Calendar.getInstance()
        val nextSeasonMonth = currentDateCal.get(Calendar.MONTH)
        with(nextSeasonCal) {
            set(
                currentDateCal.get(Calendar.YEAR) + if (nextSeasonMonth >= fistMonth) 1  else 0,
                fistMonth,
                getActualMinimum(Calendar.DAY_OF_MONTH),
                getActualMinimum(Calendar.HOUR_OF_DAY),
                getActualMinimum(Calendar.MINUTE),
                getActualMinimum(Calendar.SECOND),
            )
        }

        return Date(nextSeasonCal.timeInMillis)
    }

    private fun getTimeTillNextSeason(season: SeasonsEnum, initialTime: Date): Long {
        val currentDateCal = Calendar.getInstance()
        currentDateCal.setTime(initialTime)

        val nextSeasonDate = getNearestSeasonDate(season, initialTime)

        return getDateDiff(nextSeasonDate, Date(currentDateCal.timeInMillis))
    }

    fun getTimeTillSeasonAsString(season: SeasonsEnum, initialTime: Date? = null): String {
        val delta: Long = getTimeTillNextSeason(season, initialTime?: Date())
        val seconds: Long = delta / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return days.toString() + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60
    }

    private fun getTotalSeasonTimeByDate(date: Date): Long {
        // (date of next season - 3 month).getTime()
        val nextSeason = getNextSeason(getSeasonByDate(date))
        val nextSeasonData = getNearestSeasonDate(nextSeason, date)

        calendar.setTime(nextSeasonData)
        with(calendar) {
            set(Calendar.MONTH, get(Calendar.MONTH) - 3)
            set(Calendar.DAY_OF_MONTH, getActualMinimum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR, getActualMinimum(Calendar.HOUR))
            set(Calendar.MINUTE, getActualMinimum(Calendar.MINUTE))
            set(Calendar.SECOND, getActualMinimum(Calendar.SECOND))
            set(Calendar.MILLISECOND, getActualMinimum(Calendar.MILLISECOND))
        }

        return nextSeasonData.time - Date(calendar.timeInMillis).time
    }

    fun getDateSeasonProgress(initialTime: Date? = null): Double {
        val totalSeasonTime: Long = getTotalSeasonTimeByDate(initialTime?:Date())
        val nextSeason = getNextSeason(getSeasonByDate(initialTime?:Date()))
        val timeTillNextSeason = getTimeTillNextSeason(nextSeason, initialTime?: Date())
        return 1 - timeTillNextSeason.toDouble() / totalSeasonTime
    }
}