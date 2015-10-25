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
     * 流程：
     * 1. 构造一个递归函数recursive
     * 2. 取得输入字符串的首位 word.charAt(0)，并计算出其相对应的值 (int)word.charAt(0) - 96，以表示输入为a b c ……
     * 3. 对递归函数传入NFA的初始状态 move[0][*][*]
     * 4. 传入递归函数初始状态deep为0，即状态0，deep为当前"指针"所指向字符串的字符位置
     * 5. 根据 currentNum = (((int)word.charAt(deep) - 96)) % 27 可以获取当前深度(即指针所在字符串位置)的输入字符
     * 6. 根据5所得的currentNum而调用move函数，去判断输入该字符，得出可能迁移到的状态 nextStates
     * 7. 根据6所得的nextStates，分别遍历数组中可能到达的状态，分别使用递归，继续向下搜寻，直至遍历完整个字符串
     * 8. 将7递归完后的结尾状态，加入到result当中，作为一条可能路径的结尾
     * 9. 根据8所得的result容器，遍历其中，寻找是否有与accept_state匹配的状态
     *      -> 有：return true
     *      -> 无：return false
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