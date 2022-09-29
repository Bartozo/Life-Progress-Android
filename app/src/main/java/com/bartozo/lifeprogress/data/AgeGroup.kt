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

    fun getColor(): Color {
        return when (this) {
            BABY -> Color(0xFF069DDE)
            CHILD -> Color(0xFF5FB945)
            ADOLESCENT -> Color(0xFFF9B828)
            YOUNGADULT -> Color(0xFFF7801A)
            ADULT -> Color(0xFFDF3B3C)
            MIDDLEAGE -> Color(0xFF943B95)
            OLDAGE -> Color(0xFF955F3B)
        }
    }
}