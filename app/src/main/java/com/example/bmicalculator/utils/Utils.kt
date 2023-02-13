package com.example.bmicalculator.utils

import com.example.bmicalculator.R
import kotlin.math.pow

fun bmiCalculate(weight: Int, height: Double) = weight / ((height / 100).pow(2))

fun getString(bmi: Double): String {
    if (bmi < 18.5) return R.string.underweight.toString()
    if (bmi in 18.5..24.9) return R.string.normal_weight.toString()
    if (bmi in 25.0..29.9) return R.string.overweight.toString()
    if (bmi in 30.0..34.9) return R.string.obese_i.toString()
    if (bmi in 35.0..39.9) return R.string.obese_ii.toString()
    if (bmi >= 40) return R.string.obese_iii.toString()
    return R.string.unknown.toString()
}