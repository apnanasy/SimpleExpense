package com.example.firstkotlin

// import kotlinx.serialization.Serializable
import java.io.Serializable
import java.math.BigDecimal


class Transaction (var total: BigDecimal, var income: Boolean) : Serializable{
    override fun toString(): String {
        if(income) {
            return "Income: " + total.toString()
        } else {
            return "Expense: " + total.toString()
        }

    }

}