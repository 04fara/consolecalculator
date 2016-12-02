import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {

    private static ArrayList<Integer> operands = new ArrayList<>();
    private static ArrayList<String> operators = new ArrayList<>();
    private static ArrayList<Integer> bracketsIndices = new ArrayList<>();
    private static String rawnum = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String equation, inp, currentChar, prevChar = "";
        while (true) {
            System.out.println("Enter your equation: ");
            inp = sc.nextLine().replaceAll(" ", "");
            if ("=+-*/".contains(inp)) equation = inp.substring(0, inp.length() - 1);
            else equation = inp;
            int i = 0;
            while (i != equation.length()) {
                currentChar = Character.toString(equation.charAt(i));
                if (i > 0) prevChar = Character.toString(equation.charAt(i - 1));
                switch (currentChar) {
                    case "0":
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                    case "9":
                        rawnum = rawnum.concat(currentChar);
                        break;
                    case "+":
                    case "-":
                    case "/":
                    case "*":
                        operators.add(currentChar);
                        saveOperand();
                        break;
                    case "(":
                        if (i > 0 && "0123456789".contains(prevChar)) {
                            operators.add("*");
                            saveOperand();
                        }
                        bracketsIndices.add(operators.size());
                        break;
                    case ")":
                        saveOperand();
                        makeOperations(bracketsIndices.get(bracketsIndices.size() - 1), operators.size(), true);
                        break;
                }
                i++;
            }
            saveOperand();
            makeOperations(0, operators.size(), false);
            System.out.println("Answer:");
            System.out.print(operands.get(0));
            operators.clear();
            operands.clear();
            System.out.println();
        }
    }

    private static int[] getEq(int i) {
        int[] res = new int[]{operands.get(i), operands.get(i + 1)};
        operands.remove(i);
        operands.remove(i);
        operators.remove(i);
        return res;
    }

    private static void add(int i) {
        int[] eq = getEq(i);
        operands.add(i, eq[0] + eq[1]);
    }

    private static void sub(int i) {
        int[] eq = getEq(i);
        operands.add(i, eq[0] - eq[1]);
    }

    private static void mul(int i) {
        int[] eq = getEq(i);
        operands.add(i, eq[0] * eq[1]);
    }

    private static void div(int i) {
        int[] eq = getEq(i);
        operands.add(i, eq[0] / eq[1]);
    }

    private static boolean hasMulDiv(int i, int j) {
        for (int k = i; k < j; k++) if ("*/".contains(operators.get(k))) return true;
        return false;
    }

    private static void makeOperations(int i, int j, boolean brackets) {
        while (true) {
            if (!hasMulDiv(i, j) || operators.isEmpty()) break;
            int i1 = i;
            while (i1 < j && !operators.isEmpty()) {
                if (operators.get(i1).equals("*")) {
                    mul(i1);
                    j--;
                } else if (operators.get(i1).equals("/")) {
                    div(i1);
                    j--;
                }
                i1++;
            }
        }
        if (brackets) {
            String operator;
            int num = bracketsIndices.get(bracketsIndices.size() - 1);
            while (i < j) {
                operator = operators.get(num);
                switch (operator) {
                    case "+":
                        add(num);
                        break;
                    case "-":
                        sub(num);
                        break;
                }
                i++;
            }
            bracketsIndices.remove(bracketsIndices.size() - 1);
        } else while (!operators.isEmpty()) {
            if (operators.get(0).equals("+")) add(0);
            else if (operators.get(0).equals("-")) sub(0);
        }
    }

    private static void saveOperand() {
        if (!rawnum.isEmpty()) {
            operands.add(Integer.parseInt(rawnum));
            rawnum = "";
        }
    }

}