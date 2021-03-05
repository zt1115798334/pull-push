package com.example.pullpush.utils;

import com.example.pullpush.custom.SysConst;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUtils {
    // 控制线程数，最优选择是处理器线程数*3，本机处理器是4线程
    private final static int THREAD_COUNT = SysConst.AVAILABLE_PROCESSORS * 3;
    // 线程共享数据，保存所有的type文件
    private final ArrayList<File> fileList = Lists.newArrayList();
    // 当前文件或者目录
    private final File file;
    // 所需的文件类型
    private final String type;

    public FileUtils(String f, String type) {
        super();
        this.file = new File(f);
        this.type = type;
    }


    public ArrayList<File> getFileList() {
        // 外部接口，传递遍历结果
        return fileList;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    class FileThread implements Runnable {
        private File file;
        private String type;

        @Override
        public void run() {
            try {
                quickFind();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 非递归深度遍历算法
        void quickFind() throws IOException {
            // 使用栈，进行深度遍历
            Stack<File> stk = new Stack<File>();
            stk.push(this.file);
            File f;
            while (!stk.empty()) {
                f = stk.pop();
                if (f.isDirectory()) {
                    File[] fs = f.listFiles();
                    if (fs != null)
                        for (File value : fs) {
                            stk.push(value);
                        }
                } else {
                    if (f.getName().endsWith(type)) {
                        // 记录所需文件的信息
                        fileList.add(f);
                    }
                }
            }
        }
    }

    // 深度遍历算法加调用线程池
    public void File() {
        File fl = this.file;
        ArrayList<File> fList = new ArrayList<>();
        ArrayList<File> fListTmp = new ArrayList<>();
        ArrayList<File> tmp = null, next = null;
        fList.add(fl);
        // 广度遍历层数控制
        int loop = 0;
        while (loop++ < 3) {// 最优循环层数是3层，多次实验得出
            tmp = tmp == fList ? fListTmp : fList;
            next = next == fListTmp ? fList : fListTmp;
            for (File value : tmp) {
                fl = value;
                if (fl != null) {
                    if (fl.isDirectory()) {
                        File[] fls = fl.listFiles();
                        if (fls != null) {
                            next.addAll(Arrays.asList(fls));
                        }
                    } else {
                        if (fl.getName().endsWith(type)) {
                            fileList.add(fl);
                        }
                    }
                }
            }
            tmp.clear();
        }

        // 创建线程池，一共THREAD_COUNT个线程可以使用
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

        for (File file : next) {
            pool.submit(new FileUtils.FileThread(file, type));
        }
        pool.shutdown();
        // 必须等到所有线程结束才可以让主线程退出，不然就一直阻塞
        while (!pool.isTerminated()) ;
    }

}
