package com.bartozo.lifeprogress.data

import androidx.compose.ui.graphics.Color

enum class AgeGroup(val age: Int = 0) {
    BABY(0),
    CHILD(3),
    ADOLESCENT(9),
    YOUNGADULT(18),
    ADULT(25),
    MIDDLEAGE(40),
    OLDAGE(60);

    companion object {
        fun getGroupForAge(age: Int): AgeGroup {
            return when(age) {
                in 0 until CHILD.age -> BABY
                in CHILD.age until ADOLESCENT.age -> CHILD
                in ADOLESCENT.age until YOUNGADULT.age -> ADOLESCENT
                in YOUNGADULT.age until ADULT.age -> YOUNGADULT
                in ADULT.age until MIDDLEAGE.age -> ADULT
                in MIDDLEAGE.age until OLDAGE.age -> MIDDLEAGE
                else -> OLDAGE
            }
        }
    }

    fun getAgeInRange(): IntRange {
        return when (this) {
            BABY -> BABY.age until CHILD.age
            CHILD -> CHILD.age until ADOLESCENT.age
            ADOLESCENT -> ADOLESCENT.age until YOUNGADULT.age
            YOUNGADULT -> YOUNGADULT.age until ADULT.age
            ADULT -> ADULT.age until MIDDLEAGE.age
            MIDDLEAGE -> MIDDLEAGE.age until OLDAGE.age
            OLDAGE -> OLDAGE.age until 150
        }
    }

    // TODO update colors
    fun getColor(): Color {
        return when (this) {
            BABY -> Color.Blue
            CHILD -> Color.Green
            ADOLESCENT -> Color.Yellow
            YOUNGADULT -> Color(255, 165, 0) // orange
            ADULT -> Color.Red
            MIDDLEAGE -> Color(160, 32, 240) // purple
            OLDAGE -> Color(150, 75, 0) // brown
        }
    }
}