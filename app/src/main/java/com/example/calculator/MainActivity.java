package com.example.calculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;


import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;

    Button button_backspace;
    Button button_calculate;
    Button button_comma;
    Button button_division;
    Button button_minus;
    Button button_plus;
    Button button_cos;
    Button button_sin;
    Button button_tan;
    Button button_square;
    Button button_open_parenthesis;
    Button button_closed_parenthesis;
    Button button_cursor_left;
    Button button_cursor_right;


    EditText expression;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expression = findViewById(R.id.expression);
        expression.setSelection(0);
        expression.requestFocus();

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button_backspace = (Button) findViewById(R.id.button_backspace);
        button_calculate = (Button) findViewById(R.id.button_calculate);
        button_comma = (Button) findViewById(R.id.button_comma);
        button_division = (Button) findViewById(R.id.button_division);
        button_minus = (Button) findViewById(R.id.button_minus);
        button_plus = (Button) findViewById(R.id.button_plus);
        button_cos = (Button) findViewById(R.id.button_cos);
        button_sin = (Button) findViewById(R.id.button_sin);
        button_tan = (Button) findViewById(R.id.button_tan);
        button_square = (Button) findViewById(R.id.button_square);
        button_open_parenthesis = (Button) findViewById(R.id.button_open_parenthesis);
        button_closed_parenthesis = (Button) findViewById(R.id.button_closed_parenthesis);
        button_cursor_left = (Button) findViewById(R.id.button_cursor_left);
        button_cursor_right = (Button) findViewById(R.id.button_cursor_right);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button_calculate.setOnClickListener(this);
        button_backspace.setOnClickListener(this);
        if(button_comma!=null)
        button_comma.setOnClickListener(this);
        if(button_division!=null)
        button_division.setOnClickListener(this);
        if(button_minus!=null)
        button_minus.setOnClickListener(this);
        if(button_plus!=null)
        button_plus.setOnClickListener(this);
        if(button_cos!=null)
        button_cos.setOnClickListener(this);
        if(button_sin!=null)
        button_sin.setOnClickListener(this);
        if(button_tan!=null)
        button_tan.setOnClickListener(this);
        if(button_square!=null)
        button_square.setOnClickListener(this);
        if(button_open_parenthesis!=null)
        button_open_parenthesis.setOnClickListener(this);
        if(button_closed_parenthesis!=null)
        button_closed_parenthesis.setOnClickListener(this);
        if(button_cursor_left!=null)
        button_cursor_left.setOnClickListener(this);
        if(button_cursor_right!=null)
        button_cursor_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_cursor_left) {
            if (expression.getSelectionStart() - 1 >= 0)
                expression.setSelection(expression.getSelectionStart() - 1);
        } else if(v.getId() == R.id.button_cursor_right){
            if (expression.getSelectionStart() + 1 <= expression.length())
            expression.setSelection(expression.getSelectionStart() + 1);
        } else if (v.getId() == R.id.button_calculate) {
            Double result = eval(expression.getText().toString());
            expression.setText(result.toString());
        } else if (v.getId() == R.id.button_backspace) {
            String text = expression.getText().toString();
            if (text.length() != 0) expression.setText(text.substring(0, text.length() - 1));
        } else {
            expression.getText().insert(expression.getSelectionStart(), ((Button) v).getText().toString());
        }
        if (v.getId() == R.id.button_cos || v.getId() == R.id.button_sin || v.getId() == R.id.button_tan) {
            expression.setSelection(expression.length() - 1);
        }
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}