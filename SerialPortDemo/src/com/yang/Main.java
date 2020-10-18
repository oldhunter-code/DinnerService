package com.yang;


import com.yang.bean.Board;
import com.yang.serialport.manager.SerialPortManager;

import com.yang.serialport.utils.ShowUtils;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import javax.swing.*;
import java.awt.*;

import java.util.*;
import java.util.List;


public class Main {
    //面板组件
    private Board[] boards = new Board[50];
    private JButton[] buttons = new JButton[50];
    private JFrame f;
    private JTextArea jTextArea; //待服务餐桌显示组件
    private Set<Board> set = new TreeSet<>(); // 待服务餐桌集合


    // 串口列表
    private List<String> mCommList = null;
    // 串口对象
    private SerialPort mSerialport;
    private String portName = "COM2"; //串口名
    private int baudrate = 115200; //波特率


    /**
     * 打开串口
     */
    private void openSerialPort() {


        try {
            mSerialport = SerialPortManager.openPort(portName, baudrate);
            if (mSerialport == null) {
                ShowUtils.warningMessage("没有找到指定串口");
            }
        } catch (PortInUseException e) {
            ShowUtils.warningMessage("串口已被占用！");
        }
        // 添加串口监听
        SerialPortManager.addListener(mSerialport, new SerialPortManager.DataAvailableListener() {

            @Override
            public void dataAvailable() {
                byte[] data = null;
                try {
                    if (mSerialport == null) {
                        ShowUtils.errorMessage("串口对象为空，监听失败！");
                    } else {
                        // 读取串口数据
                        data = SerialPortManager.readFromPort(mSerialport);
                        //获取得到的餐桌号和服务状态
                        int id = data[1];
                        int isCall = data[2];
                        System.out.println(data[1] + "  " + data[2]);
                        //服务状态和原先不一致才改变显示
                        if (boards[id-1].getIsCall()!=data[2]){
                        boards[id-1].setIsCall(isCall);
                        boards[id-1].setTime(new Date());
                        show(boards,buttons,jTextArea,set);
                        }

                    }
                } catch (Exception e) {
                    ShowUtils.errorMessage(e.toString());
                    // 发生读取错误时显示错误信息后退出系统
                    // System.exit(0);
                }
            }
        });
    }

    // 添加串口监听


    /**
     * 初始化数据
     */
    private void initData() {
        mCommList = SerialPortManager.findPorts();
        // 检查是否有可用串口，有则加入选项中
        if (mCommList == null || mCommList.size() < 1) {
            ShowUtils.warningMessage("没有搜索到有效串口！");
        }

    }

    /**
     * 初始化窗口
     */
    private void initView() {
        for (int i = 0; i < boards.length; i++) {
            boards[i] = new Board(i, 0, new Date());
        }
        f = new JFrame("餐厅呼叫系统");
        f.setSize(1300, 900);//设置容器尺寸
        f.setLocation(200, 200);//设置容器位置
        f.setLayout(null);//设置布局。
        JButton b = new JButton("刷新");
        b.setBounds(50, 50, 280, 30);//设置按钮在容器中的位置
        f.add(b);//将按钮加在容器上
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                buttons[i * 10 + j] = new JButton("餐桌:" + (i * 10 + j + 1));
                buttons[i * 10 + j].setBounds(60 + j * 120, 100 + i * 100, 100, 50);
                f.add(buttons[i * 10 + j]);
                buttons[i * 10 + j].setBackground(Color.green);
            }
        }
        jTextArea = new JTextArea("待服务餐桌：");
        jTextArea.setBounds(100, 600, 1000, 200);
        f.add(jTextArea);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//界面关闭后程序结束
        f.setVisible(true);//界面可视化
    }

    public static void main(String[] args) throws Exception {

        Main main = new Main();
        main.initView();
        main.initData();
        show(main.boards, main.buttons, main.jTextArea, main.set);
        main.openSerialPort();


//
//        for (int i = 0; i < boards.length; i++) {
//            boards[i] = new Board(i, 0, new Date());
//        }

        //Frame frame = new Frame(boards, jTextAreas);


//        JFrame f = new JFrame("餐厅呼叫系统");
//        f.setSize(1300, 900);//设置容器尺寸
//        f.setLocation(200, 200);//设置容器位置
//        f.setLayout(null);//设置布局。
//        JButton b = new JButton("刷新");
//        b.setBounds(50, 50, 280, 30);//设置按钮在容器中的位置
//        f.add(b);//将按钮加在容器上
//        JButton[] buttons = new JButton[50];
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 10; j++) {
//                buttons[i * 10 + j] = new JButton("餐桌:" + (i * 10 + j + 1));
//                buttons[i * 10 + j].setBounds(60 + j * 120, 100 + i * 100, 100, 50);
//                f.add(buttons[i * 10 + j]);
//                buttons[i * 10 + j].setBackground(Color.green);
//            }
//        }
//        JTextArea c = new JTextArea("待服务餐桌：");
//        c.setBounds(100, 600, 1000, 200);
//        f.add(c);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//界面关闭后程序结束
//        b.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {//事件响应器，当点击按钮后会执行方法中的代码
//                boards[1].setIsCall(1);
//                boards[0].setIsCall(0);
//                //show(boards, buttons, c,list);
//            }
//        });
//        boards[0].setIsCall(1);
//        show(boards, buttons, c,list);
//
//        f.setVisible(true);//界面可视化。


    }

//    public static void show(Board[] boards, JButton[] buttons, JButton jButton) {
//        for (int i = 0; i < boards.length; i++) {
//            if (boards[i].getIsCall() == 0) {
//                buttons[i].setBackground(Color.green);
//            } else {
//                buttons[i].setBackground(Color.red);
//            }
//        }
//    }

    public static void show(Board[] boards, JButton[] jButtons, JTextArea jTextArea,Set<Board> set) throws InterruptedException {

//        boards[10].setIsCall(1);
//        boards[10].setTime(new Date());
//        Thread.sleep(10);
//        boards[8].setIsCall(1);
//        boards[8].setTime(new Date());
//        Thread.sleep(10);
//        boards[7].setIsCall(1);
//        boards[7].setTime(new Date());


        for (int i = 0; i < boards.length; i++) {
            if (boards[i].getIsCall() == 0) {
                jButtons[i].setBackground(Color.green);
                set.remove(boards[i]);
            } else {
                jButtons[i].setBackground(Color.red);
                set.add(boards[i]);
            }
        }

        System.out.println(set);
         jTextArea.setText("待服务餐桌：");
        for (Board board : set) {
            String str = (board.getId() + 1) + " 、";
            jTextArea.append(str);
        }

    }
}


//class Frame extends JFrame implements ActionListener {
//    public Board[] boards;
//    public JTextArea[] jTextAreas;
//    public JTextArea jTextArea;
//    public ArrayList<Board> list;
//
//    public Frame() {
//    }
//
//    ;
//
//    public Frame(Board[] boards, JTextArea[] jTextAreas) {
//        this.jTextAreas = jTextAreas;
//        this.boards = boards;
//        this.setTitle("餐厅呼叫系统");
//        this.setSize(1300, 900);//设置容器尺寸
//        this.setLocation(200, 200);//设置容器位置
//        this.setLayout(null);//设置布局。
//        JButton b = new JButton("刷新");
//        b.addActionListener(this); //刷新按钮加入监听器
//        b.setBounds(50, 50, 280, 30);//设置按钮在容器中的位置
//        this.add(b);//将按钮加在容器上
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 10; j++) {
//                jTextAreas[i * 10 + j] = new JTextArea();
//                jTextAreas[i * 10 + j].append("餐桌:" + (i * 10 + j + 1));
//                jTextAreas[i * 10 + j].setBounds(60 + j * 120, 100 + i * 100, 100, 50);
//                this.add(jTextAreas[i * 10 + j]);
//                jTextAreas[i * 10 + j].setBackground(Color.green);
//            }
//        }
//
//        jTextArea = new JTextArea("待服务餐桌：");
//        jTextArea.setBounds(100, 600, 1000, 200);
//        this.add(jTextArea);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//界面关闭后程序结束
//
//        this.setVisible(true);//界面可视化。
//    }
//
//    public void actionPerformed(ActionEvent e) {
//        if (e.getActionCommand().equals("刷新")) {
//            try {
//                this.show(boards, jTextAreas, jTextArea, list);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//        }
//
//    }
//
//    public void show(Board[] boards, JTextArea[] jTextAreas, JTextArea jTextArea, ArrayList<Board> list) throws InterruptedException {
//        list = new ArrayList<Board>();
//        boards[10].setIsCall(1);
//        boards[10].setTime(new Date());
//        Thread.sleep(10);
//        boards[8].setIsCall(1);
//        boards[8].setTime(new Date());
//        Thread.sleep(10);
//        boards[7].setIsCall(1);
//        boards[7].setTime(new Date());
//
//
//        for (int i = 0; i < boards.length; i++) {
//            if (boards[i].getIsCall() == 0) {
//                jTextAreas[i].setBackground(Color.green);
//            } else {
//                jTextAreas[i].setBackground(Color.red);
//                list.add(boards[i]);
//            }
//        }
//        Collections.sort(list);
//        System.out.println(list);
//        for (Board board : list) {
//            String str = (board.getId() + 1) + " 、";
//            jTextArea.append(str);
//        }
//
//    }
//}