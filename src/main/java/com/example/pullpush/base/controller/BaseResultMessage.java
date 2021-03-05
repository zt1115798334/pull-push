package com.example.pullpush.base.controller;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 14:52
 * description:
 */
public abstract class BaseResultMessage {


    /**
     * 格式输出结果
     *
     * @return ResultMessage
     */
    protected ResultMessage success() {
        return new ResultMessage().correctness();
    }

    /**
     * 格式输出结果
     *
     * @param msg 描述
     * @return ResultMessage
     */
    protected ResultMessage success(String msg) {
        return new ResultMessage().correctness(msg);
    }

    /**
     * 格式输出结果
     *
     * @param data 数据
     * @return ResultMessage
     */
    protected ResultMessage success(Object data) {
        return new ResultMessage().correctness().setData(data);
    }

    /**
     * 格式输出结果
     *
     * @param msg  描述
     * @param data 数据
     * @return ResultMessage
     */
    protected ResultMessage success(String msg, Object data) {
        return new ResultMessage().correctness(msg).setData(data);
    }

    /**
     * 格式输出结果
     *
     * @return ResultMessage
     */
    protected ResultMessage failure() {
        return new ResultMessage().error();
    }

    /**
     * 格式输出结果
     *
     * @param msg 描述
     * @return ResultMessage
     */
    protected ResultMessage failure(String msg) {
        return new ResultMessage().error( msg);
    }


    /**
     * 格式输出结果
     *
     * @param pageNumber 当前页
     * @param pageSize   页大小
     * @param total      总记录数
     * @param rows       当前页数据
     * @return ResultMessage
     */
    protected ResultMessage success(int pageNumber, int pageSize, long total, Object rows) {
        return new ResultMessage().correctnessPage(pageNumber, pageSize, total, rows);
    }

    /**
     * 格式输出结果
     *
     * @return ResultMessage
     */
    protected ResultMessage failurePage() {
        return new ResultMessage().errorPage();
    }
}
