package analysis.sql.model;

import java.util.LinkedList;

public class Linked {
    private LinkedList lk;

    public Linked() {
        this.lk = new LinkedList();
    }

    /**
     * 入栈
     *
     * @param obj
     */
    public void addData(Object obj) {
        lk.push(obj);
    }

    /**
     * 出栈
     *
     * @return
     */
    public Object removeData() {
        return lk.poll();
    }

    public LinkedList getLk() {
        return lk;
    }

}
