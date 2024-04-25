package io.github.givimad.whisperjni;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author: Zekun Fu
 * @date: 2024/4/21 10:46
 * @Description:
 */
public class Leetcode394 {

    public int numberOfSpecialChars(String word) {
        HashSet<Character>set = new HashSet<>();
        int bit = 0;
        int removeBit = 0;
        for (char c : word.toCharArray()) {
            if (Character.isLowerCase(c)) {
                if (set.contains(Character.toUpperCase(c))) {
                    bit |= 1 << (c - 'a');
                    // 如果小写之前有大写
                    removeBit |= 1 << (c - 'a');
                }
            } else {
                if (set.contains(Character.toLowerCase(c)))
                    bit |= 1 << (c - 'A');
            }
            set.add(c);
        }
        return Integer.bitCount(bit) - Integer.bitCount(removeBit);
    }
    /**使格子每一列相等，相邻每一行不相等的最小方法
     * 数很小，10个数
     * 可以遍历每一行变成某一个数字的值，修改的个数，预先统计出来
     * 然后就可以使用dp了
     * */
    public int minimumOperations(int[][] grid) {
        int n = grid.length, m = grid[0].length;
        int[][] sum = new int[m][10];
        for (int k = 0; k < 10; k++) {
            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    sum[j][k] += grid[i][j] == k ? 0 : 1;
                }
            }
        }
        int[][] f = new int[m][10];
        int INF = 0x3f3f3f3f;
        for (int i = 0; i < m; i++)
            Arrays.fill(f[i], INF);
        f[0] = sum[0];
        for (int j = 1; j < m; j++) {
            for (int k = 0; k < 10; k++) {              // 本列的数值
                for (int t = 0; t < 10; t++) {          // 前一列的数值
                    if (k == t) continue;
                    // 只要和前面那一列不相同，就可以变成这个数字。由前一列的最小值，加上本列的变化需要的值
                    f[j][k] = Math.min(f[j][k], f[j - 1][t] + sum[j][k]);
                }
            }
        }
        int ans = INF;
        for (int i = 0; i < 10; i++)
            ans = Math.min(ans, f[m - 1][i]);
        return ans;
    }
    
    public static void main(String[] args) {
        Leetcode394 c = new Leetcode394();
//        int cnt = c.numberOfSpecialChars("AbBCab");
//        System.out.println(cnt);
        int[][] grid = {{1, 1, 1}, {0, 0, 0}};
        int cnt = c.minimumOperations(grid);
        System.out.println(cnt);
    }
}
