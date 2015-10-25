/**
 * Created by linyx25 on 2015/10/25.
 */
import java.util.*;
import java.io.*;

class DFA {
    /* @param move[][] -> record the next state of the input of word
     * @param accept_state[] -> store the accept_state of the DFA
     * @param word -> the input the string waited to recognize
     *
     * @param acceptNum -> record the present state in the DFA
     * @param c -> a single letter of the word
     * @param letterNum -> as the parameter of the move, to choose which state to go
     *
     * RETURN -> if accepted then true else false
     *
     * ���̣�
     * 1. ����string word���ַ���˳��ÿ��ȡһ���ַ� c����ʶ��
     * 2. ����� (int)c - 97��0 ���� a, 1 ���� b, �Դ�����
     * 3. ���ݵ�ǰ״̬acceptNum�Լ��������letterNum,����move��ά���飬�������һ״̬��������accpetNum
     * 4. ÿ��ѭ���ж�i �Ƿ�Ϊ�ַ������һ���ַ�
     *    -> �ǣ��ж�acceptNum�Ƿ���accept_state��
     *          -> �ǣ�return true
     *    -> �� return false
     */
    boolean recognizeString(int move[][], int accept_state[], String word) {
        int acceptNum = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int letterNum = ((int)c - 97) % 26;
            acceptNum = move[acceptNum][letterNum];
            if (i == word.length() - 1) {
                for (int j = 0; j < accept_state.length; j++) {
                    if (accept_state[j] == acceptNum) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String args[]) throws IOException {
        int n, m;
        BufferedReader in = new BufferedReader(new FileReader("DFA.in"));
        StringTokenizer st = new StringTokenizer(in.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        while (n != 0) {
            int[][] move = new int[n][m];
            for(int i=0; i<n; i++) {
                st = new StringTokenizer(in.readLine());
                for (int j=0; j<m; j++)
                    move[i][j] = Integer.parseInt(st.nextToken());
            }
            String[] temp = in.readLine().split("\\s");
            int[] accept = new int[temp.length];
            for (int i=0; i<accept.length; i++) accept[i] = Integer.parseInt(temp[i]);
            String word = in.readLine();
            while (word.compareTo("#") != 0) {
                DFA dfa = new DFA();
                if (dfa.recognizeString(move, accept, word)) System.out.println("YES"); else System.out.println("NO");
                word = in.readLine();
            }
            st = new StringTokenizer(in.readLine());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
        }
    }
}
