/**
 * Created by linyx25 on 2015/11/1..
 */
import java.util.*;
import java.io.*;

class NFA {

    /* 伪代码实现过程如下
     *
     * 计算ε-closure(T)
     *      将T的所有状态压入stack中
     *      将ε-closure(T)初始化为T
     *      while(stack非空) {
     *          将栈顶元素t弹出栈中
     *          for (每个满足如下条件的u: 从t出发有一个标号为ε的转换到达状态u) {
     *              if (u 不在ε-closure(T)中) {
     *                  将u加入到ε-closure(T)中
     *                  将u压入栈中
     *              }
     *          }
     *      }
     *
     * 模拟NFA主程序
     *      S = ε-closure(s0)
     *      c = nextChar()
     *      while (c != eof) {
     *          S = ε-closure(move(S, c))
     *          c = nextChar()
     *      }
     *      if (S ∩ F != ?) return yes
     *      else return no
     *
     * 加入一个不在newStates中的新状态s
     *      addState(s) {
     *          将s压入栈newStates中
     *          alreadyOn[s] = TRUE
     *          for (t on move[s, ε])
     *              if (!alreadyOn[t])
     *                  addState[t]
     *      }
     *
     *  S = ε-closure(move(S, c))的具体实现，即将新状态全部压入到oldStates当中
     *      for (oldStates上的每个s) {
     *          for (move[s, c]中的每个t) {
     *              if (!alreadyOn[t]) {
     *                  addState[t]
     *              }
     *          }
     *          将s弹出oldStates栈
     *      }
     *
     *      for (newStates中的每个s) {
     *          将s弹出newStates栈
     *          将s压入到oldStates栈
     *          alreadyOn[s] = FALSE
     *      }
     */

    private boolean[] alreadyOn;    // 指出哪个状态已经在newStates中，该数组存放信息和栈中存放信息相同
    private Stack<Integer> oldStates = new Stack<>();   // 当前状态集合
    private Stack<Integer> newStates = new Stack<>();   // 下一个状态集合

    /* @param move[][][] -> (state, letter, to_state)
     * @param accept_state[] -> store the accept_state of the NFA
     * @param word -> the input the string waited to recognize
     *
     * RETURN -> if accepted then true else false
     */
    boolean recognizeString(int move[][][], int accept_state[], String word) {

        // 初始化alreadyOn数组，一开始全为False，即没有新状态在栈中
        alreadyOn = new boolean[move.length];
        for (int i = 0; i < move.length; i++) {
            alreadyOn[i] = false;
        }

        // 初始化构造只有ε的的状态，构造newStates栈，0为开始状态
        addState(0, move);

        /*
         * 模拟算法主要过程，不断遍历word，调用S = ε-closure(move(S,c))，进行新旧状态集合的更新
         * 计算到达下一状态，直至结束，判断最终状态是否在接受状态内
         * 是 -> 可表达该字符串
         * 否 -> 则返回false
         */
        for (int i = 0; i < word.length(); i++) {
            while(!oldStates.empty()) {
                int currentState = oldStates.peek();
                for (int j : move[currentState][word.charAt(i) - 96]) {
                    if (!alreadyOn[j]) {
                        addState(j, move);
                    }
                }
                oldStates.pop();
            }
            while(!newStates.empty()) {
                int nextState = newStates.peek();
                oldStates.push(newStates.pop());
                alreadyOn[nextState] = false;
            }
        }

        // 最后的终态集合当中，是否存在结束状态
        while(!oldStates.empty()) {
            int finalState = oldStates.pop();
            for (int i = 0; i < accept_state.length; i++) {
                if (finalState == accept_state[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * 不断调用addState而计算得出 newStates，即新的状态集合
     */
    private  void addState(int i , int move[][][]) {
        if (!alreadyOn[i]) {
            newStates.push(i);
            alreadyOn[i] = true;
        }
        /*
         * 循环判断只通过ε能到达的状态，每一次循环都判断该状态的alreadyOn布尔值是否为真
         * 真 -> 无处理
         * 假 -> 递归调用
         */
        for (int j : move[i][0]) {
            if (!alreadyOn[j]) {
                addState(j, move);
            }
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
