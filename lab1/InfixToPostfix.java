/**
 * Created by linyx25 on 2015/10/3.
 */
import java.util.Stack;
import java.lang.String;

public class InfixToPostfix {
    /* @param expression  -> the string of infix expression
     * RETURN -> the string of suffix expression
     *
     * 判断步骤:
     * 1. 首先判断是否为 ')'，若是，将栈中元素持续pop至栈顶元素为'('为止，若不是，跳2
     * 2. 是否为 '+' 或 '-'，若是，循环判断pop栈顶元素是否为 '*' '/',取出并pop栈顶元素,若不是，跳3
     * 3. 判断是否为 '(' '*' '/'，若是，直接入栈
     * 4. 以上条件都不是，直接输出
     *
     * ps;
     * 1. java stack peek -> return object，需要将其转换为char类型才能进行判断
     * 2. 对于1提出的一点，声明stack的时候定义类型为 <T> -> <Character>即可
     */
    private static String convertExpr(String expression) {
        String str = expression;
        String strPFX = "";
        Stack<Character>stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == ')') {
                while(true) {
                    char tmp = stack.peek();
                    stack.pop();
                    if (tmp != '(') {
                        strPFX += tmp;
                    } else {
                        break;
                    }
                }
            } else if (ch == '+' || ch == '-') {
                char tmp = stack.peek();
                if (tmp == '*' || tmp == '/') {
                    while (tmp == '*' || tmp == '/') {
                        strPFX += tmp;
                        stack.pop();
                        if (stack.empty()) {
                            break;
                        } else {
                            tmp = stack.peek();
                        }
                    }
                }
                stack.push(ch);
            } else if (ch == '(' || ch == '*' || ch == '/') {
                stack.push(ch);
            } else {
                strPFX += ch;
            }
        }
        while (!stack.empty()) {
            char tmp = stack.peek();
            strPFX += tmp;
            stack.pop();
        }
        return strPFX;
    }


    /* Main函数调用 convertExpr函数
     * 测试方法：传入事先定义好的 str 作为参数
     *
     * 测试1:
     * expression: (a+b)*c-(a+b)/e
     * standard output: ab+c*ab+e/-
     *
     * 测试2:
     * expression: (2+1)/(2-1)*(2+2)
     * standard output: 21+21-22+*\/
     */
    public static void main(String[] args) {
        String str = "(a+b)*c-(a+b)/e";
        String result = convertExpr(str);
        System.out.println("The input is : " + str);
        System.out.println("The result is : " + result + "\n");

        String str2 = "(2+1)/(2-1)*(2+2)";
        String result2 = convertExpr(str2);
        System.out.println("The input is : " + str);
        System.out.println("The result is : " + result2 + "\n");
    }
}


