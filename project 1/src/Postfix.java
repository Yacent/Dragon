import java.io.*;

/**
 * <b>简单语法指导器：Infix to Postfix</b>
 * <p>Input: an expression of Infix &nbsp;&nbsp;
 * 运算量只能为：0-9 + -</p>
 * <p>Output: an expression of Postfix (with error Msg)</p>
 * <pre>
 * Grammar:
 * expr -&gt; term rest
 * rest -&gt; + term {print('+')} rest
 *      |  - term {print('-')} rest
 *      |  ?
 * term -&gt; 0 {print('0')}
 *      |  1 {print('1')}
 *      |  ...
 *      |  9 {print('9')}
 * </pre>
 *
 * @author Yuxin Lin(林育新)
 */
class Parser {
    /**
     * 表示当前正在被处理的字符
     */
    static int lookahead;

    /**
     * 表示lookahead在输入字符串input中的位置
     */
    int position;

    /**
     * 记录语法分析的错误信息
     */
    StringBuffer errorMsg;

    /**
     * 读取下一个输入字符
     * 忽略空格 space tab \t
     * @throws IOException 当不正常读取字符时，抛出异常
     */
    private void getNextToken() throws IOException {
        //  读取下一个字符
        lookahead = System.in.read();
        //  记录当前lookahead的位置
        position++;

        /**
         * 忽略空格处理
         * 遇到空格，不断读取下一个字符直至为非空格字符
         */
        while (lookahead == ' ') {
            lookahead = System.in.read();
            position++;
        }
    }

    /**
     * 构造函数，由Postfix调用
     * 初始化 lookahead 的值
     * 实例化errorMsg
     * @throws IOException 当出现异常时, 抛出
     */
    public Parser() throws IOException {
        getNextToken();
        errorMsg = new StringBuffer();
    }

    /**
     * <pre>
     * expr -&gt; term + rest
     * term GOTO 143
     * rest GOTO 115
     * </pre>
     * @throws IOException 当出现异常时, 抛出
     */
    void expr() throws IOException {
        term();
        rest();
        //  检测是否存在错误信息, 并输出
        if (errorMsg.length() != 0) {
            System.out.println("\n" + errorMsg);
        }
    }

    /**
     * <pre>
     * rest -&gt; + term {print('+')} rest
     *      |  - term {print('-')} rest
     *      |  ?
     * </pre>
     * 实现文法的三个表达式
     * 错误恢复模式：恐慌模式
     *      当遇到一个错误时，不断读取下一位，直至可以处理为止
     *      简单错误回复模式，可实现一次读取，即检查出全部错误
     *
     * 采取 循环递归 替代 尾递归
     * <pre>
     * if (lookahead == '+') {
     *     match('+');
     *     term();
     *     System.out.write('+')
     *     rest(); 尾递归
     * }
     * </pre>
     * @throws IOException 当出现异常时, 抛出
     */
    void rest() throws IOException {
        while (true) {
            if (lookahead == '+') {
                match('+');
                term();
                System.out.write('+');
                continue;
            } else if (lookahead == '-') {
                match('-');
                term();
                System.out.write('-');
                continue;
            } else if (Character.isDigit((char)lookahead)) {
                /**
                 * 错误模式：digit之间缺少运算符 + 或 -
                 * i.e. 95+2
                 */
                errorMsg.append("错误：第" + position + "个字符\n\t" + "语法错误：运算量间缺少运算符\n");
                System.out.print((char) lookahead + " <!运算量间缺少运算符> ");
                getNextToken();
                continue;
            } else if (Character.isAlphabetic(lookahead)) {
                /**
                 * 错误模式：输入字符错误，非[0-9\+\-]
                 * i.e. 9 + a - 2
                 */
                errorMsg.append("错误：第" + position + "个字符\n\t" + "词法错误：非法的输入\n");
                System.out.print(" <!非法的运算量> ");
                getNextToken();
                continue;
            } else {
                // do nothing
            }
            break;
        }
       // if (lookahead == '+') {
       //     match('+');
       //     term();
       //     System.out.write('+');
       //     rest();
       // } else if (lookahead == '-') {
       //     match('-');
       //     term();
       //     System.out.write('-');
       //     rest();
       // } else if (Character.isDigit((char)lookahead)) {
       //     errorMsg.append("错误：第" + position + "个字符\n\t" + "语法错误：运算量间缺少运算符\n");
       //     System.out.print((char) lookahead + " <!运算量间缺少运算符> ");
       //     getNextToken();
       //     rest();
       // } else if (Character.isAlphabetic(lookahead)) {
       //     errorMsg.append("错误：第" + position + "个字符\n\t" + "词法错误：非法的输入\n");
       //     System.out.print(" <!非法的运算量> ");
       //     getNextToken();
       //     rest();
       // } else {
       //     // do nothing
       // }
    }

    /**
     * <pre>
     * term -&gt; 0 {print('0')}
     *      |  1 {print('1')}
     *      |  ...
     *      |  9 {print('9')}
     * </pre>
     * term 只能为digit，其余都为错误
     * 错误恢复模式：恐慌模式
     * @throws IOException 当出现异常时, 抛出
     */
    void term() throws IOException {
        while (true) {
            if (Character.isDigit((char)lookahead)) {
                System.out.write((char)lookahead);
                match(lookahead);
                break;
            } else {
                if (position == 1) {
                    //  错误类型：不以数字开头 i.e. a + 8
                    errorMsg.append("错误：第" + position + "个字符\n\t" + "语法错误：表达式首项应为数字\n");
                    System.out.print(" <!非法的运算量> ");
                } else if (lookahead == '+' || lookahead == '-' || lookahead == '\n') {
                    //  错误类型：运算符缺失运算分量 i.e. 9+
                    errorMsg.append("错误：第" + position + "个字符\n\t" + "语法错误：缺少右运算分量\n");
                    System.out.print(" <!运算符缺少右运算量> ");
                } else {
                    //  输入了非法字符 i.e. [^0-9\+\-]
                    errorMsg.append("错误：第" + position + "个字符\n\t" + "词法错误：非法的输入\n");
                    System.out.print(" <!非法的运算量> ");
                }
            }

            //  当lookahead为\n时，即出现了错误，且表达式已扫描结束，应终止，后无token可读，若不终止，程序出错
            if (lookahead == '\n') {
                break;
            }

            getNextToken();
            break;
        }
    }

    /**
     * 用以匹配当前的符号是否为正确
     * @param t 传入当前读取的词
     * @throws IOException 当出现异常时, 抛出
     */
    void match(int t) throws IOException {
        if (lookahead == t)  {
            getNextToken();
        } else {
            throw new Error("syntax error");
        }
    }
}

public class Postfix {
	public static void main(String[] args) throws IOException {
       // FileWriter writer = null;
       // try {
       //     writer = new FileWriter("../RTRDuration.csv", true);
       //     writer = new FileWriter("../TRDuration.csv", true);
       // } catch (IOException e) {
       //     e.printStackTrace();
       // }
       // long startTime = System.currentTimeMillis();
	   System.out.println("Input an infix expression and output its postfix notation:");
	   new Parser().expr();
	   System.out.println("\nEnd of program.");
	   // long endTime = System.currentTimeMillis();
       // long totalTime = endTime - startTime;
       // writer.write(totalTime + "\n");
       // writer.close();
	}
}
