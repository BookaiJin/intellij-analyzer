package com.fr.analyzer.gc.log;

public class GcMessageGenerator {

    private GcMessageGenerator() {
        //do nothing
    }

    public static void record(StringBuilder row, String[] temps) {
        if ("GC".equalsIgnoreCase(temps[2])) {
            row.append("[").append(temps[2]).append(" (").append(temps[5]).append(") [PSYoungGen: ")
                    .append((temps[7])).append("K->").append((temps[8])).append("K(").append((temps[10])).append("K)] ")
                    .append((temps[19])).append("K->").append((temps[20])).append("K(").append((temps[22])).append("K), ")
                    .append(Integer.parseInt(temps[6]) / 1000F).append(" secs] [Times: real=").append(Integer.parseInt(temps[6]) / 1000F).append(" secs] [pid: ").append(temps[3])
                    .append("] [node: ").append(temps[4]).append("]");
        } else if ("Full GC".equalsIgnoreCase(temps[2])) {
            row.append("[").append(temps[2]).append(" (").append(temps[5]).append(") [PSYoungGen: ")
                    .append((temps[7])).append("K->").append((temps[8])).append("K(").append((temps[9])).append("K)] ")
                    .append("[ParOldGen: ")
                    .append((temps[11])).append("K->").append((temps[12])).append("K(").append((temps[14])).append("K)] ")
                    .append((temps[19])).append("K->").append((temps[20])).append("K(").append((temps[22])).append("K), ")
                    .append("[Metaspace: ")
                    .append((temps[15])).append("K->").append((temps[16])).append("K(").append((temps[18])).append("K)], ")
                    .append(Integer.parseInt(temps[6]) / 1000F).append(" secs] [Times: real=").append(Integer.parseInt(temps[6]) / 1000F).append(" secs] [pid: ").append(temps[3])
                    .append("] [node: ").append(temps[4]).append("]");
        }
        row.append(" [balancePromoterScore: ").append(temps[23]).append(", releasePromoterScore: ").append(temps[24]).append(", loadScore: ").append(temps[25]).append("]");
    }
}
