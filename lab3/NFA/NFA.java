/**
 * Created by linyx25 on 2015/10/25.
 */
import java.util.*;
import java.io.*;

class NFA {
    /* @param move[][][] -> (state, letter, to_state)
     * @param accept_state[] -> store the accept_state of the NFA
     * @param word -> the input the string waited to recognize
     *
     * @param result -> using for store the state that recursive in the func called recursive and the deep equal to word.length
     * @param letterNum -> as the parameter of the move, to choose which state to go
     *
     * @func recursive() ->to recursive the state that the machine choose to run
     *
     * RETURN -> if accepted then true else false
     *
     * ���̣�
     * 1. ����һ���ݹ麯��recursive
     * 2. ȡ�������ַ�������λ word.charAt(0)��������������Ӧ��ֵ (int)word.charAt(0) - 96���Ա�ʾ����Ϊa b c ����
     * 3. �Եݹ麯������NFA�ĳ�ʼ״̬ move[0][*][*]
     * 4. ����ݹ麯����ʼ״̬deepΪ0����״̬0��deepΪ��ǰ"ָ��"��ָ���ַ������ַ�λ��
     * 5. ���� currentNum = (((int)word.charAt(deep) - 96)) % 27 ���Ի�ȡ��ǰ���(��ָ�������ַ���λ��)�������ַ�
     * 6. ����5���õ�currentNum������move������ȥ�ж�������ַ����ó�����Ǩ�Ƶ���״̬ nextStates
     * 7. ����6���õ�nextStates���ֱ���������п��ܵ����״̬���ֱ�ʹ�õݹ飬����������Ѱ��ֱ�������������ַ���
     * 8. ��7�ݹ����Ľ�β״̬�����뵽result���У���Ϊһ������·���Ľ�β
     * 9. ����8���õ�result�������������У�Ѱ���Ƿ�����accept_stateƥ���״̬
     *      -> �У�return true
     *      -> �ޣ�return false
     */
    boolean recognizeString(int move[][][], int accept_state[], String word) {
        Vector<Integer> result = new Vector<>();
        int letterNum = ((int)word.charAt(0) - 96) % 27;

        recursive(result, move[0][letterNum][0], move[0][letterNum], 0, word, move);
        for (int i = 0; i < result.size(); i++) {
            for (int j = 0; j < accept_state.length; j++) {
                if (result.get(i) == accept_state[j]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void recursive(Vector<Integer> result, int nextState, int[] nextStates, int deep, String word, int move[][][]) {
        if (deep == word.length()) {
            result.add(nextState);
            return;
        }
        int currentNum = (((int)word.charAt(deep) - 96)) % 27;
        nextStates = move[nextState][currentNum];
        for (int j = 0; j < nextStates.length; j++) {
            nextState = nextStates[j];
            recursive(result, nextState, nextStates, deep + 1, word, move);
        }
    }

    public static void main(String args[]) throws IOException {
        int n, m;
        BufferedReader in = new BufferedReader(new FileReader("NFA.in"));
        StringTokenizer st = new StringTokenizer(in.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        while (n != 0) {
            int[][][] move = new int[n][m][];
            for(int i=0; i<n; i++) {
                String line = in.readLine();
                int k = 0;
                for (int j=0; j<m; j++) {
                    while (line.charAt(k) != '{') k++;
                    k++;
                    String states = "";
                    while (line.charAt(k) != '}') {
                        states = states + line.charAt(k);
                        k++;
                    }
                    states = states.trim();
                    if (states.length() > 0) {
                        String[] stateArray = states.split(",");
                        move[i][j] = new int[stateArray.length];
                        for (int l=0; l<move[i][j].length; l++) move[i][j][l] = Integer.parseInt(stateArray[l].trim());
                    }
                    else move[i][j] = new int[0];
                }
            }
            String[] temp = in.readLine().split("\\s");
            int[] accept = new int[temp.length];
            for (int i=0; i<accept.length; i++) accept[i] = Integer.parseInt(temp[i]);
            String word = in.readLine();
            while (word.compareTo("#") != 0) {
                NFA nfa = new NFA();
                if (nfa.recognizeString(move, accept, word)) System.out.println("YES"); else System.out.println("NO");
                word = in.readLine();
            }
            st = new StringTokenizer(in.readLine());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
        }
    }
}