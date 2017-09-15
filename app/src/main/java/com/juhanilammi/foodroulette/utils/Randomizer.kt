package com.juhanilammi.foodroulette.utils

import com.juhanilammi.foodroulette.data.models.SimpleFacebookLocation
import java.util.*

class Randomizer {
    companion object {
        fun randomize(list: List<SimpleFacebookLocation>): SimpleFacebookLocation{

                return list.get(Random().nextInt(list.size))

        }
    }
}