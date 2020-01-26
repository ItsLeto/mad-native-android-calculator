package herwig_l.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private var numParentheses: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnEquals.setOnClickListener {
            try {
                val equation = ExpressionBuilder(tvEquation.text.toString()).build()
                val result = equation.evaluate()
                tvResult.text = result.toString()
            } catch (e: Exception) {
                tvResult.text = "ERROR"
            }
        }

        btnClear.setOnClickListener               { tvEquation.text = ""; tvResult.text = "" }
        btnBack.setOnClickListener                { tvEquation.text = tvEquation.text.dropLast(1) }
        btnAdd.setOnClickListener                 { addSymbolToEquation("+", true) }
        btnSubtract.setOnClickListener            { addSymbolToEquation("-", true) }
        btnMultiply.setOnClickListener            { addSymbolToEquation("*", true) }
        btnDivid.setOnClickListener               { addSymbolToEquation("/", true) }
        btnOpenParenthesis.setOnClickListener     { addSymbolToEquation("(") }
        btnClosingParenthesis.setOnClickListener  { addSymbolToEquation(")") }
        btnPoint.setOnClickListener               { addSymbolToEquation(".") }
        btnNum0.setOnClickListener                { addSymbolToEquation("0") }
        btnNum1.setOnClickListener                { addSymbolToEquation("1") }
        btnNum2.setOnClickListener                { addSymbolToEquation("2") }
        btnNum3.setOnClickListener                { addSymbolToEquation("3") }
        btnNum4.setOnClickListener                { addSymbolToEquation("4") }
        btnNum5.setOnClickListener                { addSymbolToEquation("5") }
        btnNum6.setOnClickListener                { addSymbolToEquation("6") }
        btnNum7.setOnClickListener                { addSymbolToEquation("7") }
        btnNum8.setOnClickListener                { addSymbolToEquation("8") }
        btnNum9.setOnClickListener                { addSymbolToEquation("9") }

    }

    private fun addSymbolToEquation(symbol: String, isAction: Boolean = false) {
        if (isAction) {
            btnAdd.isEnabled = false
            btnSubtract.isEnabled = false
            btnMultiply.isEnabled = false
            btnDivid.isEnabled = false
        } else {
            btnAdd.isEnabled = true
            btnSubtract.isEnabled = true
            btnMultiply.isEnabled = true
            btnDivid.isEnabled = true
        }

        if (symbol === "(") {
            btnClosingParenthesis.isEnabled = true
            numParentheses += 1
        } else if( symbol === ")") {
            numParentheses -= 1
            if (numParentheses === 0)
                btnClosingParenthesis.isEnabled = false
        }

        tvEquation.text = tvEquation.text.toString() + symbol
    }
}
