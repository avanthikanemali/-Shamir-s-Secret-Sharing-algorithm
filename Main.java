import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class X {
    public static void main(String[] args) {
        try {
            // 1: Read the file
            JSONParser a = new JSONParser();
            JSONObject b = (JSONObject) a.parse(new FileReader("test_case.json"));

            // 2: Grab the keys
            JSONObject c = (JSONObject) b.get("keys");
            int d = Integer.parseInt(c.get("n").toString());
            int e = Integer.parseInt(c.get("k").toString());

            // 3: Decode Y values
            List<Y> f = new ArrayList<>();
            for (Object g : b.keySet()) {
                if (!g.equals("keys")) {
                    JSONObject h = (JSONObject) b.get(g);
                    int i = Integer.parseInt(g.toString());
                    int j = Integer.parseInt(h.get("base").toString());
                    String k = h.get("value").toString();
                    int l = m(k, j);
                    f.add(new Y(i, l));
                }
            }

            // 4: Check number of points
            if (f.size() < e) {
                throw new IllegalArgumentException("Not enough points to compute.");
            }

            // 5: Find the secret
            int n = o(f);
            System.out.println("Secret: " + n);

        } catch (Exception p) {
            p.printStackTrace();
        }
    }

    // Decode
    public static int m(String q, int r) {
        return Integer.parseInt(q, r);
    }

    // Solve using matrix method
    public static int o(List<Y> s) {
        int t = s.size();
        double[][] u = new double[t][t];
        double[] v = new double[t];

        for (int w = 0; w < t; w++) {
            int x = s.get(w).x;
            int y = s.get(w).y;
            for (int z = 0; z < t; z++) {
                u[w][z] = Math.pow(x, z);
            }
            v[w] = y;
        }

        double[] a = p(u, v);
        return (int) Math.round(a[0]);
    }

    // Gaussian elimination to solve
    public static double[] p(double[][] u, double[] v) {
        int t = v.length;
        for (int w = 0; w < t; w++) {
            int x = w;
            for (int y = w + 1; y < t; y++) {
                if (Math.abs(u[y][w]) > Math.abs(u[x][w])) {
                    x = y;
                }
            }

            double[] z = u[w];
            u[w] = u[x];
            u[x] = z;
            double temp = v[w];
            v[w] = v[x];
            v[x] = temp;

            for (int y = w + 1; y < t; y++) {
                double alpha = u[y][w] / u[w][w];
                v[y] -= alpha * v[w];
                for (int z1 = w; z1 < t; z1++) {
                    u[y][z1] -= alpha * u[w][z1];
                }
            }
        }

        double[] a = new double[t];
        for (int y = t - 1; y >= 0; y--) {
            double sum = 0;
            for (int z = y + 1; z < t; z++) {
                sum += u[y][z] * a[z];
            }
            a[y] = (v[y] - sum) / u[y][y];
        }
        return a;
    }

    // Helper class
    static class Y {
        int x;
        int y;

        Y(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
