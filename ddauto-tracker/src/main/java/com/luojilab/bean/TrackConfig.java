package com.luojilab.bean;

import java.util.List;

/**
 * user liushuo
 * date 2017/4/8
 */

public class TrackConfig {
    /**
     * ctr_id : ColumnVC_ArticleIcon 控件唯一id
     * event_id : article_into 事件
     * params : [{"p":"article_id"}] 参数
     */

    private String ctr_id;
    private String event_id;
    private List<ParamsBean> params;

    public String getCtr_id() {
        return ctr_id;
    }

    public void setCtr_id(String ctr_id) {
        this.ctr_id = ctr_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public List<ParamsBean> getParams() {
        return params;
    }

    public void setParams(List<ParamsBean> params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * p : article_id
         */

        private String p;

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }
    }
}
