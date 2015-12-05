import java.io.*;

/**
 * <b>���﷨ָ������Infix to Postfix</b>
 * <p>Input: an expression of Infix &nbsp;&nbsp;
 * ������ֻ��Ϊ��0-9 + -</p>
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
 * @author Yuxin Lin(������)
 */
class Parser {
    /**
     * ��ʾ��ǰ���ڱ�������ַ�
     */
    static int lookahead;

    /**
     * ��ʾlookahead�������ַ���input�е�λ��
     */
    int position;

    /**
     * ��¼�﷨�����Ĵ�����Ϣ
     */
    StringBuffer errorMsg;

    /**
     * ��ȡ��һ�������ַ�
     * ���Կո� space tab \t
     * @throws IOException ����������ȡ�ַ�ʱ���׳��쳣
     */
    private void getNextToken() throws IOException {
        //  ��ȡ��һ���ַ�
        lookahead = System.in.read();
        //  ��¼��ǰlookahead��λ��
        position++;

        /**
         * ���Կո���
         * �����ո񣬲��϶�ȡ��һ���ַ�ֱ��Ϊ�ǿո��ַ�
         */
        while (lookahead == ' ') {
            lookahead = System.in.read();
            position++;
        }
    }

    /**
     * ���캯������Postfix����
     * ��ʼ�� lookahead ��ֵ
     * ʵ����errorMsg
     * @throws IOException �������쳣ʱ, �׳�
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
     * @throws IOException �������쳣ʱ, �׳�
     */
    void expr() throws IOException {
        term();
        rest();
        //  ����Ƿ���ڴ�����Ϣ, �����
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
     * ʵ���ķ����������ʽ
     * ����ָ�ģʽ���ֻ�ģʽ
     *      ������һ������ʱ�����϶�ȡ��һλ��ֱ�����Դ���Ϊֹ
     *      �򵥴���ظ�ģʽ����ʵ��һ�ζ�ȡ��������ȫ������
     *
     * ��ȡ ѭ���ݹ� ��� β�ݹ�
     * <pre>
     * if (lookahead == '+') {
     *     match('+');
     *     term();
     *     System.out.write('+')
     *     rest(); β�ݹ�
     * }
     * </pre>
     * @throws IOException �������쳣ʱ, �׳�
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
                 * ����ģʽ��digit֮��ȱ������� + �� -
                 * i.e. 95+2
                 */
                errorMsg.append("���󣺵�" + position + "���ַ�\n\t" + "�﷨������������ȱ�������\n");
                System.out.print((char) lookahead + " <!��������ȱ�������> ");
                getNextToken();
                continue;
            } else if (Character.isAlphabetic(lookahead)) {
                /**
                 * ����ģʽ�������ַ����󣬷�[0-9\+\-]
                 * i.e. 9 + a - 2
                 */
                errorMsg.append("���󣺵�" + position + "���ַ�\n\t" + "�ʷ����󣺷Ƿ�������\n");
                System.out.print(" <!�Ƿ���������> ");
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
       //     errorMsg.append("���󣺵�" + position + "���ַ�\n\t" + "�﷨������������ȱ�������\n");
       //     System.out.print((char) lookahead + " <!��������ȱ�������> ");
       //     getNextToken();
       //     rest();
       // } else if (Character.isAlphabetic(lookahead)) {
       //     errorMsg.append("���󣺵�" + position + "���ַ�\n\t" + "�ʷ����󣺷Ƿ�������\n");
       //     System.out.print(" <!�Ƿ���������> ");
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
     * term ֻ��Ϊdigit�����඼Ϊ����
     * ����ָ�ģʽ���ֻ�ģʽ
     * @throws IOException �������쳣ʱ, �׳�
     */
    void term() throws IOException {
        while (true) {
            if (Character.isDigit((char)lookahead)) {
                System.out.write((char)lookahead);
                match(lookahead);
                break;
            } else {
                if (position == 1) {
                    //  �������ͣ��������ֿ�ͷ i.e. a + 8
                    errorMsg.append("���󣺵�" + position + "���ַ�\n\t" + "�﷨���󣺱��ʽ����ӦΪ����\n");
                    System.out.print(" <!�Ƿ���������> ");
                } else if (lookahead == '+' || lookahead == '-' || lookahead == '\n') {
                    //  �������ͣ������ȱʧ������� i.e. 9+
                    errorMsg.append("���󣺵�" + position + "���ַ�\n\t" + "�﷨����ȱ�����������\n");
                    System.out.print(" <!�����ȱ����������> ");
                } else {
                    //  �����˷Ƿ��ַ� i.e. [^0-9\+\-]
                    errorMsg.append("���󣺵�" + position + "���ַ�\n\t" + "�ʷ����󣺷Ƿ�������\n");
                    System.out.print(" <!�Ƿ���������> ");
                }
            }

            //  ��lookaheadΪ\nʱ���������˴����ұ��ʽ��ɨ�������Ӧ��ֹ������token�ɶ���������ֹ���������
            if (lookahead == '\n') {
                break;
            }

            getNextToken();
            break;
        }
    }

    /**
     * ����ƥ�䵱ǰ�ķ����Ƿ�Ϊ��ȷ
     * @param t ���뵱ǰ��ȡ�Ĵ�
     * @throws IOException �������쳣ʱ, �׳�
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
