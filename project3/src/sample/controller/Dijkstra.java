package sample.controller;

public class Dijkstra {
    public int[] shortPath;
    public String[] path;
    static int M=10000;
    public Dijkstra(){

    }

    public void findShortPath(int[][] distances,int start,int end) {

        int[] shortPath = Dijsktra(distances,start,end);
        this.shortPath = shortPath;
        System.out.println("从"+start+"出发到"+end+"的最短距离为："+shortPath[end]);

    }
    //得到一个点到其它任意点的最短路径标号
    public int[] Dijsktra(int[][] weight, int start, int end){
        int n = weight.length;        //顶点个数
        int[] shortPath = new int[n];    //start到其他各点的最短路径
        String[] path=new String[n]; //start到其他各点的最短路径的字符串表示
        for(int i=0;i<n;i++)
            path[i]=new String(start+"-->"+i);
        int[] visited = new int[n];   //是否已找到最短路径
        shortPath[start] = 0;
        visited[start] = 1;//表示已经找到
        //要加入n-1个顶点
        for(int count = 1;count <= n - 1;count++)
        {
           //选出一个距离初始顶点start最近的未标记顶点（贪心算法）
            int k = -1;
            int j = Integer.MAX_VALUE;
            for(int i = 0;i < n;i++) {
                if (visited[i] == 0 && weight[start][i] < j) {
                    j = weight[start][i];
                    k = i;
                }
            }
            shortPath[k] = j;
            visited[k] = 1;
            //以k为中间点，修正从start到未访问各点的距离
            for(int i = 0;i < n;i++)
            {
                if(visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]){
                    weight[start][i] = weight[start][k] + weight[k][i];
                    path[i]=path[k]+"-->"+i;
                }
            }
        }
        this.path = path;
        System.out.println("=====================================");
        System.out.println("从"+start+"出发到"+end+"的最短路径为："+path[end]);
        return shortPath;
    }
}