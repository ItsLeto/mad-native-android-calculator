package herwig_l.calculator

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

//    private var numParentheses: Int = 0

    private var memory: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnEquals.setOnClickListener {
            val result = calculateResult()
            if (result !== null) {
                tvEquation.text = result.toString()
                tvResult.text = ""
            }
        }
        btnMemAdd.setOnClickListener {
            val result = calculateResult()
            if (result !== null) {
                tvMemoryStatus.visibility = View.VISIBLE
                memory += result
                tvMemoryStatus.text = "M   val: " + memory.toString()
            }
        }
        btnMemSubract.setOnClickListener {
            val result = calculateResult()
            if (result !== null) {
                tvMemoryStatus.visibility = View.VISIBLE
                memory -= result
                tvMemoryStatus.text = "M   val: " + memory.toString()
            }
        }
        btnMemClear.setOnClickListener {
            tvMemoryStatus.visibility = View.INVISIBLE
            memory = 0.0
        }
        btnMemLoad.setOnClickListener {
            if (tvMemoryStatus.visibility == View.VISIBLE) {
                if (tvEquation.text.toString().isEmpty()) {
                    tvEquation.text = tvEquation.text.toString() + memory.toString()
                } else {

                    val lastSymbol = tvEquation.text.toString().takeLast(1)
                    Log.d("MemLoad", lastSymbol)

                    if (lastSymbol !== "+" && lastSymbol !== "-" && lastSymbol !== "*" && lastSymbol !== "/") {
                        if (memory < 0) {
                            tvEquation.text = tvEquation.text.toString() + memory.toString()
                        } else if (memory >= 0) {
                            tvEquation.text = tvEquation.text.toString() + "+" + memory.toString()
                        }
                    }
                    tvResult.text = calculateResult()?.toString() ?: ""
                }
            }
        }

        btnClear.setOnClickListener {
            tvEquation.text = ""
            tvResult.text = ""
        }

        btnBack.setOnClickListener {
            tvEquation.text = tvEquation.text.dropLast(1)
            tvResult.text = calculateResult()?.toString() ?: ""
        }
        btnAdd.setOnClickListener { addOperatorToEquation("+") }
        btnSubtract.setOnClickListener { addOperatorToEquation("-") }
        btnMultiply.setOnClickListener { addOperatorToEquation("*") }
        btnDivid.setOnClickListener { addOperatorToEquation("/") }
//        btnOpenParenthesis.setOnClickListener     { addSymbolToEquation("(") }
//        btnClosingParenthesis.setOnClickListener  { addSymbolToEquation(")") }
        btnPoint.setOnClickListener { addSymbolToEquation(".") }
        btnNum0.setOnClickListener { addSymbolToEquation("0") }
        btnNum1.setOnClickListener { addSymbolToEquation("1") }
        btnNum2.setOnClickListener { addSymbolToEquation("2") }
        btnNum3.setOnClickListener { addSymbolToEquation("3") }
        btnNum4.setOnClickListener { addSymbolToEquation("4") }
        btnNum5.setOnClickListener { addSymbolToEquation("5") }
        btnNum6.setOnClickListener { addSymbolToEquation("6") }
        btnNum7.setOnClickListener { addSymbolToEquation("7") }
        btnNum8.setOnClickListener { addSymbolToEquation("8") }
        btnNum9.setOnClickListener { addSymbolToEquation("9") }

    }

    private fun addSymbolToEquation(symbol: String, isAction: Boolean = false) {
        tvEquation.text = tvEquation.text.toString() + symbol
        tvResult.text = calculateResult()?.toString() ?: ""
    }

    private fun calculateResult(): Double? {
        var result: Double? = null
        try {
            val equation = ExpressionBuilder(tvEquation.text.toString()).build()
            result = equation.evaluate()
//            tvResult.text = result.toString()
        } catch (e: Exception) {
//            tvResult.text = "ERROR"
        }
        return result
    }

    private fun isArithmetic(operator: String): Boolean {
        return operator == "+" || operator == "-" || operator == "/" || operator == "*"
    }

    private fun addOperatorToEquation(operator: String) {
        Log.d("[addOperatorToEquation]", operator)
        val equation = tvEquation.text.toString()

        if (equation.isEmpty()) {
            // only symbol allowed to stand at the beginning of the equation is a '-'
            if (operator == "-") {
                tvEquation.text = equation + operator
            }
            return
        }

        val lastChar = equation.takeLast(1)
        val secondLastChar = equation.takeLast(2).take(1)

        if (isArithmetic(lastChar)) {
            if (operator == "-") {
                if (lastChar == "+") {
                    tvEquation.text = equation.dropLast(1) + operator
                } else if (lastChar == "*" || lastChar == "/") {
                    tvEquation.text = equation + operator
                } else {
                    return // chaining '-' should not be possible
                }
            } else if (operator == "+") {
                if (isArithmetic(secondLastChar)) {
                    tvEquation.text = equation.dropLast(2) + operator
                } else {
                    tvEquation.text = equation.dropLast(1) + operator
                }
            } else if (operator == "*") {
                if (isArithmetic(secondLastChar)) {
                    tvEquation.text = equation.dropLast(2) + operator
                } else {
                    tvEquation.text = equation.dropLast(1) + operator
                }
            } else if (operator == "/") {
                if (isArithmetic(secondLastChar)) {
                    tvEquation.text = equation.dropLast(2) + operator
                } else {
                    tvEquation.text = equation.dropLast(1) + operator
                }
            }
        } else {
            Log.d("[Noo]", secondLastChar)
            tvEquation.text = equation + operator
        }
    }
}
