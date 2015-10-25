/**
 * Created by linyx25 on 2015/10/3.
 */
import java.util.Stack;
import java.lang.String;

public class InfixToPostfix {
    /* @param expression  -> the string of infix expression
     * RETURN -> the string of suffix expression
     *
     * �жϲ���:
     * 1. �����ж��Ƿ�Ϊ ')'�����ǣ���ջ��Ԫ�س���pop��ջ��Ԫ��Ϊ'('Ϊֹ�������ǣ���2
     * 2. �Ƿ�Ϊ '+' �� '-'�����ǣ�ѭ���ж�popջ��Ԫ���Ƿ�Ϊ '*' '/',ȡ����popջ��Ԫ��,�����ǣ���3
     * 3. �ж��Ƿ�Ϊ '(' '*' '/'�����ǣ�ֱ����ջ
     * 4. �������������ǣ�ֱ�����
     *
     * ps;
     * 1. java stack peek -> return object����Ҫ����ת��Ϊchar���Ͳ��ܽ����ж�
     * 2. ����1�����һ�㣬����stack��ʱ��������Ϊ <T> -> <Character>����
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


    /* Main�������� convertExpr����
     * ���Է������������ȶ���õ� str ��Ϊ����
     *
     * ����1:
     * expression: (a+b)*c-(a+b)/e
     * standard output: ab+c*ab+e/-
     *
     * ����2:
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


