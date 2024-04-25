package io.github.givimad.whisperjni;

/**
 * @author: Zekun Fu
 * @date: 2024/4/21 8:12
 * @Description:
 */
public class Test111 {
    public long findKthSmallest(int[] coins, int k) {
        int n = coins.length;
        long l = 0, r = Long.MAX_VALUE;
        while (l < r) {
            long mid = (l + r) >> 1;
            if (check(coins, k, mid)) r = mid;
            else l = mid + 1;
        }
        return l;
    }
    private boolean check(int[] coins, int k, long x) {
        long sum = 0;
        int n = coins.length;
        for (int i = 1; i < (1 << n); i++) {
            long cnt = 0;
            long lcm = 1;
            for (int j = 0; j < n; j++) {
                if ((i >> j & 1) == 1) {
                    lcm = getLcm(lcm, coins[j]);
                    cnt++;
                }
            }
            sum += cnt % 2 == 0 ? -x / lcm : x / lcm;
        }
//        System.out.println(sum);
        return sum >= k;
    }
    private long getLcm(long a, long b) {
        return a * b / gcd(a, b);
    }
    private long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) {
        int[] coins = {3,6,9};
        int k = 3;
        long ans =
        new Test111().findKthSmallest(coins, k);
        System.out.println(ans);
    }
}
