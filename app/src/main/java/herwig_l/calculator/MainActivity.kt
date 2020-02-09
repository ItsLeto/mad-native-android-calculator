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
    private var equation: String = ""

    private val DIVISION_SYBMOL = "\u00f7"
    private val MULTIPLY_SYMBOL = "\u00d7"
    private val MINUS_SYMBOL = "\u2212"
    private val PLUS_SYMBOL = "+"

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
                if (equation.isEmpty()) {
                    equation += memory.toString()
                    tvEquation.text = tvEquation.text.toString() + memory.toString()
                } else {

                    val lastSymbol = equation.takeLast(1)
                    Log.d("MemLoad", lastSymbol)

                    if (lastSymbol !== "+"
                        && lastSymbol !== "-"
                        && lastSymbol !== "*"
                        && lastSymbol !== "/"
                    ) {
                        if (memory < 0) {
                            equation += memory.toString()
                            tvEquation.text = tvEquation.text.toString() + memory.toString()
                        } else if (memory >= 0) {
                            equation += "+$memory"
                            tvEquation.text = tvEquation.text.toString() + "+" + memory.toString()
                        }
                    }
                    tvResult.text = calculateResult()?.toString() ?: ""
                }
            }
        }

        btnClear.setOnClickListener {
            equation = ""
            tvEquation.text = ""
            tvResult.text = ""
        }

        btnBack.setOnClickListener {
            equation = equation.dropLast(1)
            tvEquation.text = tvEquation.text.toString().dropLast(1)
            tvResult.text = calculateResult()?.toString() ?: ""
        }
        btnAdd.setOnClickListener { addOperatorToEquation(PLUS_SYMBOL) }
        btnSubtract.setOnClickListener { addOperatorToEquation(MINUS_SYMBOL) }
        btnMultiply.setOnClickListener { addOperatorToEquation(MULTIPLY_SYMBOL) }
        btnDivid.setOnClickListener { addOperatorToEquation(DIVISION_SYBMOL) }
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
        equation += symbol
        tvEquation.text = tvEquation.text.toString() + symbol
        tvResult.text = calculateResult()?.toString() ?: ""
    }

    private fun calculateResult(): Double? {
        var result: Double? = null
        try {
            val equation = ExpressionBuilder(equation.toString()).build()
            result = equation.evaluate()
        } catch (e: Exception) {
            Log.e("[Calculate]", e.toString())
        }
        return result
    }

    private fun isArithmetic(operator: String): Boolean {
        return operator == "+" || operator == "-" || operator == "/" || operator == "*"
    }

    private fun getAsciiOperator(operator: String): String? {
        return when (operator) {
            PLUS_SYMBOL -> "+"
            MINUS_SYMBOL -> "-"
            MULTIPLY_SYMBOL -> "*"
            DIVISION_SYBMOL -> "/"

            else -> null
        }
    }

    private fun addOperatorToEquation(operator: String) {
        Log.d("[addOperatorToEquation]", operator)
        Log.d("[Equation]", equation)
//        val equation = equation.toString()

        if (equation.isEmpty()) {
            // only symbol allowed to stand at the beginning of the equation is a '-'
            if (operator == MINUS_SYMBOL) {
                equation += getAsciiOperator(operator)
                tvEquation.text = tvEquation.text.toString() + operator
            }
            return
        }

        val lastChar = equation.takeLast(1)
        val secondLastChar = equation.takeLast(2).take(1)
        Log.d("Last", lastChar)
        Log.d("SecondLast", secondLastChar)


        if (isArithmetic(lastChar)) {
            if (operator == MINUS_SYMBOL) {
                if (lastChar == "+") {
                    equation = equation.dropLast(1) + getAsciiOperator(operator)
                    tvEquation.text = tvEquation.text.toString().dropLast(1) + operator
                } else if (lastChar == "*" || lastChar == "/") {
                    tvEquation.text = tvEquation.text.toString() + operator
                    equation += getAsciiOperator(operator)
                } else {
                    return // chaining '-' should not be possible
                }
            } else if (operator == PLUS_SYMBOL) {
                if (isArithmetic(secondLastChar)) {
                    tvEquation.text = tvEquation.text.toString().dropLast(2) + operator
                    equation = equation.dropLast(2) + getAsciiOperator(operator)
                } else {
                    tvEquation.text = tvEquation.text.toString().dropLast(1) + operator
                    equation = equation.dropLast(1) + getAsciiOperator(operator)
                }
            } else if (operator == MULTIPLY_SYMBOL) {
                if (isArithmetic(secondLastChar)) {
                    tvEquation.text = tvEquation.text.toString().dropLast(2) + operator
                    equation = equation.dropLast(2) + getAsciiOperator(operator)
                } else {
                    tvEquation.text = tvEquation.text.toString().dropLast(1) + operator
                    equation = equation.dropLast(1) + getAsciiOperator(operator)
                }
            } else if (operator == DIVISION_SYBMOL) {
                if (isArithmetic(secondLastChar)) {
                    tvEquation.text = tvEquation.text.toString().dropLast(2) + operator
                    equation = equation.dropLast(2) + getAsciiOperator(operator)
                } else {
                    tvEquation.text = tvEquation.text.toString().dropLast(1) + operator
                    equation = equation.dropLast(1) + getAsciiOperator(operator)
                }
            }
        } else {
            Log.d("[Noo]", secondLastChar)
            equation += getAsciiOperator(operator)
            tvEquation.text = tvEquation.text.toString() + operator
        }
    }
}
