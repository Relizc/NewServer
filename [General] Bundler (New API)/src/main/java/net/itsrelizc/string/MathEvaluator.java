package net.itsrelizc.string;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MathEvaluator {
    public static long evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return -1;
        }

        try {
            expression = preprocess(expression);
            Expression exp = new ExpressionBuilder(expression).build();
            return (long) exp.evaluate();
        } catch (Exception e) {
            return -1;
        }
    }

    private static String preprocess(String expression) {
        expression = expression.toLowerCase().replaceAll("[ \\t\\r\\n\\f]+", ""); // Remove whitespace
        expression = expression.replaceAll("([0-9]+)k", "$1*1000"); // Replace k with *1000
        expression = expression.replaceAll("([0-9]+)m", "$1*1000000"); // Replace m with *1000000
        expression = expression.replaceAll("([0-9]+)%", "($1/100.0)"); // Convert percentages
        return expression;
    }

    public static void main(String[] args) {
        System.out.println(evaluate("7 * 3 + 5"));  // 26.0
        System.out.println(evaluate("2k * 5"));    // 10000.0
        System.out.println("%,f".formatted(evaluate("2m * 5")));    // 10000000.0
        System.out.println(evaluate("50% * 200")); // 100.0
        System.out.println(evaluate("abc + 5"));   // null
    }
}
