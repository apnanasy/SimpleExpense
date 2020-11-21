package com.example.firstkotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import java.text.DecimalFormat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Exception
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    var expenses:Float = 0.0F
    var income:Float = 0.0F
    var mc:MathContext = MathContext(8, RoundingMode.HALF_DOWN)
    var expensesB:BigDecimal = BigDecimal(0.00, mc)
    var incomeB:BigDecimal = BigDecimal(0.00, mc)
    var expenseTrans = mutableListOf<Transaction>()
    val df = DecimalFormat("#.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            var objectInput: ObjectInputStream = ObjectInputStream(applicationContext.openFileInput("trans.e"))
            expenseTrans =  objectInput.readObject() as MutableList<Transaction>
        }
        catch(e: FileNotFoundException) {
            var t: Toast = Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG)
            t.show()
        }

        val adapt = ArrayAdapter(this,android.R.layout.simple_list_item_1, expenseTrans)
        listView.adapter = adapt

        display()

        sIncome?.setOnClickListener() {
            display()
        }

        bCalc?.setOnClickListener()
        {
            if(sIncome.isChecked) {
                expenseTrans.add(Transaction(eTtrans.text.toString().toBigDecimal(mc), true))
                try {
                    var objOut: ObjectOutputStream = ObjectOutputStream(
                        applicationContext.openFileOutput(
                            "trans.e",
                            Context.MODE_PRIVATE
                        )
                    )
                    objOut.writeObject(expenseTrans) //new
                    objOut.close()
                } catch (e: Exception) {
                    var t: Toast =
                        Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG)
                    t.show()
                }
            } else {
                expenseTrans.add(Transaction(eTtrans.text.toString().toBigDecimal(mc), false))
                try {
                    var objOut: ObjectOutputStream = ObjectOutputStream(
                        applicationContext.openFileOutput(
                            "trans.e",
                            Context.MODE_PRIVATE
                        )
                    )
                    objOut.writeObject(expenseTrans)
                    objOut.close()
                } catch (e: Exception) {
                    var t: Toast =
                        Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG)
                    t.show()
                }
            }
            display()
        }
        bReset?.setOnClickListener()
        {
            expenseTrans.clear()
            display()
        }

    }
    fun display() {
        expenses = 0.00F
        expensesB = BigDecimal(0.00, mc)
        income = 0.00F
        incomeB = BigDecimal(0.00, mc)
        eTtrans.setText("")
        if (sIncome.isChecked) {
            bCalc.text = "Add Income"
        } else {
            bCalc.text = "Add expense"
        }
        for (trans in expenseTrans) {
            if(trans.income) {
                incomeB = incomeB.add(trans.total)
            } else {
                expensesB =  expensesB.add(trans.total)
            }
        }

        tVtotal.text = expensesB.toString()
        tVincome.text = incomeB.toString()
    }

}