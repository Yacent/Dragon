/**
 * Created by linyx25 on 2015/11/1..
 */
import java.util.*;
import java.io.*;

class NFA {

    /* α����ʵ�ֹ�������
     *
     * �����-closure(T)
     *      ��T������״̬ѹ��stack��
     *      ����-closure(T)��ʼ��ΪT
     *      while(stack�ǿ�) {
     *          ��ջ��Ԫ��t����ջ��
     *          for (ÿ����������������u: ��t������һ�����Ϊ�ŵ�ת������״̬u) {
     *              if (u ���ڦ�-closure(T)��) {
     *                  ��u���뵽��-closure(T)��
     *                  ��uѹ��ջ��
     *              }
     *          }
     *      }
     *
     * ģ��NFA������
     *      S = ��-closure(s0)
     *      c = nextChar()
     *      while (c != eof) {
     *          S = ��-closure(move(S, c))
     *          c = nextChar()
     *      }
     *      if (S �� F != ?) return yes
     *      else return no
     *
     * ����һ������newStates�е���״̬s
     *      addState(s) {
     *          ��sѹ��ջnewStates��
     *          alreadyOn[s] = TRUE
     *          for (t on move[s, ��])
     *              if (!alreadyOn[t])
     *                  addState[t]
     *      }
     *
     *  S = ��-closure(move(S, c))�ľ���ʵ�֣�������״̬ȫ��ѹ�뵽oldStates����
     *      for (oldStates�ϵ�ÿ��s) {
     *          for (move[s, c]�е�ÿ��t) {
     *              if (!alreadyOn[t]) {
     *                  addState[t]
     *              }
     *          }
     *          ��s����oldStatesջ
     *      }
     *
     *      for (newStates�е�ÿ��s) {
     *          ��s����newStatesջ
     *          ��sѹ�뵽oldStatesջ
     *          alreadyOn[s] = FALSE
     *      }
     */

    private boolean[] alreadyOn;    // ָ���ĸ�״̬�Ѿ���newStates�У�����������Ϣ��ջ�д����Ϣ��ͬ
    private Stack<Integer> oldStates = new Stack<>();   // ��ǰ״̬����
    private Stack<Integer> newStates = new Stack<>();   // ��һ��״̬����

    /* @param move[][][] -> (state, letter, to_state)
     * @param accept_state[] -> store the accept_state of the NFA
     * @param word -> the input the string waited to recognize
     *
     * RETURN -> if accepted then true else false
     */
    boolean recognizeString(int move[][][], int accept_state[], String word) {

        // ��ʼ��alreadyOn���飬һ��ʼȫΪFalse����û����״̬��ջ��
        alreadyOn = new boolean[move.length];
        for (int i = 0; i < move.length; i++) {
            alreadyOn[i] = false;
        }

        // ��ʼ������ֻ�Цŵĵ�״̬������newStatesջ��0Ϊ��ʼ״̬
        addState(0, move);

        /*
         * ģ���㷨��Ҫ���̣����ϱ���word������S = ��-closure(move(S,c))�������¾�״̬���ϵĸ���
         * ���㵽����һ״̬��ֱ���������ж�����״̬�Ƿ��ڽ���״̬��
         * �� -> �ɱ����ַ���
         * �� -> �򷵻�false
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

        // ������̬���ϵ��У��Ƿ���ڽ���״̬
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
     * ���ϵ���addState������ó� newStates�����µ�״̬����
     */
    private  void addState(int i , int move[][][]) {
        if (!alreadyOn[i]) {
            newStates.push(i);
            alreadyOn[i] = true;
        }
        /*
         * ѭ���ж�ֻͨ�����ܵ����״̬��ÿһ��ѭ�����жϸ�״̬��alreadyOn����ֵ�Ƿ�Ϊ��
         * �� -> �޴���
         * �� -> �ݹ����
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
