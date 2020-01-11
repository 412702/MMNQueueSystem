package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.SystemEvent.*;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem.MMNQueuingSystem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @ClassName:
 * @Description: TODO(事件调度法窗体)
 * @author 张春立
 * @date 2019年12月
 */

public class QSSESAFrame extends JFrame implements ActionListener {
//    private Object Exception;

    public static void main(String[] args) {
        QSSESAFrame qsframe = new QSSESAFrame();
        qsframe.setVisible(true);
    }
    //事件调度法参数
    private long plat_num;   //服务台数目
    private double arrive_rate; //到底率
    private double serveice_rate;   //服务率
    private long seed1;     //种子一
    private long seed2;     //种子二
    private long queue_length_limit; //最大队长限制
    private long finish_time;    //结束时间限制
    private int sleep_time;     //休眠等待时间（或者叫步长时间）

    //相应结果取值
    private long plat_num_used;  //使用中的服务台数
    private long num_in_queue;   //排队中的顾客数
    private long rejected_customer; //因队列长度不足而离开的顾客数
          //相应结果取值 -> 事件状态
//    private String[] event_status = {"事件开始", "系统初始化", "事件扫描","处理事件表","推进事件","处理当前事件", "判断模拟是否结束","模拟结束"};  //其实这里应该是用map更好，弄上一个key值
    private Map<String, String> event_status; ;



    //panel
    private JPanel panelLeft;
    private JPanel panelCenter;
    private JPanel panelRight;

    //panelLeft component
    private JLabel label_pl;    //服务台数目
    private JTextArea textArea_pl;
    private JLabel label_ar;    //到达率
    private JTextArea textArea_ar;
    private JLabel label_sr;    //服务率
    private JTextArea textArea_sr;
    private JLabel label_s1;    //种子1
    private JTextArea textArea_s1;
    private JLabel label_s2;    //种子2
    private JTextArea textArea_s2;
    private JLabel label_qll;    //最大队长
    private JTextArea textArea_qll;
    private JLabel label_ft;    //完成时间
    private JTextArea textArea_ft;
    private JLabel label_st;    //流程图模拟每步间隔
    private JTextArea textArea_st;
    private JButton button_begin;   //开始按钮
    private JButton button_clear;   //清空按钮

    //panelCenter component
    private JLabel label_start;     //开始
    private JLabel label_sys_init;  //系统初始化
    private JLabel label_time_scan; //时间扫描
    private JLabel label_proc_event_table;//处理事件表
    private JLabel label_simu_push_time;//模拟时钟推进当前时间
    private JLabel label_proc_curr_event;//处理当前事件
    private JLabel label_if_end;//判断模拟是否结束
    private JLabel label_end;//结束


    //panelRight component
    private JLabel label_sys_time; //获得系统时间
    private JLabel label_sys_time_value;
    private JLabel label_pnu;   //plat_num_used, 使用中的服务台数目
    private JLabel label_pnu_value;
    private JLabel label_niq;   //num_in_queue,队列中用户数目
    private JLabel label_niq_value;
    private JLabel label_leave_num;    //因等待离开系统的人数
    private JLabel label_leave_num_value;
    private JLabel label_es;    //event_status,事件状态
    private JLabel label_es_value;

    private JButton button_excel;   //选择生成excel表的按钮
    private Boolean button_excel_enable = false;

    //模拟对象
    private MMNQueuingSystem mmnqs;
    //把模拟放入新线程
    Thread thread;
    //
    /*
    * initial UI frame
    * */
    public QSSESAFrame(){
        this.init();
    }
    public void init(){
        System.out.println(this.getClass().getResource("ProcessIcon/start.png"));
        this.InitGlobalFont(new Font("alias",Font.PLAIN,20));
        //layout
        GridLayout gridLayout_frame = new GridLayout(1,3);
        FlowLayout flowLayout_panelLeft = new FlowLayout(FlowLayout.LEFT, 10, 20);//设置流动方式以及水平垂直间隙
        FlowLayout flowLayout_panelRight = new FlowLayout(FlowLayout.LEFT, 10, 40);//设置流动方式以及水平垂直间隙
        /*ArrayList*/
        ArrayList<JTextArea> textAreasList = new ArrayList<JTextArea>();
        textAreasList.add(textArea_pl);
        textAreasList.add(textArea_ar);
        textAreasList.add(textArea_sr);
        textAreasList.add(textArea_s1);
        textAreasList.add(textArea_s2);
        textAreasList.add(textArea_qll);
        textAreasList.add(textArea_ft);
        textAreasList.add(textArea_st);
        /*./ ArrayList*/
        /*panelLeft part*/
        panelLeft = new JPanel();
//        panelLeft.setBackground(Color.cyan);
        panelLeft.setLayout(flowLayout_panelLeft);
        panelLeft.setBorder(BorderFactory.createTitledBorder("输入模拟参数"));

        label_pl = new JLabel("服务台数目：");    //服务台数目
        textArea_pl = new JTextArea();
        label_ar = new JLabel("到达率：");    //到达率
        textArea_ar = new JTextArea();
        label_sr = new JLabel("服务率：");    //服务率
        textArea_sr = new JTextArea();
        label_s1 = new JLabel("到达率的随机数种子：");    //种子1，到达率的随机数种子
        textArea_s1 = new JTextArea();textArea_s1.setText("非必填");
        label_s2 = new JLabel("服务率随机数种子：");    //种子2，服务率的随机数种子
        textArea_s2 = new JTextArea();textArea_s2.setText("非必填");
        label_qll = new JLabel("最大排队长度：");    //最大队长
        textArea_qll = new JTextArea();
        label_ft = new JLabel("系统结束时间：");    //完成时间
        textArea_ft = new JTextArea();
        label_st = new JLabel("流程图每步时间(ms)：");    //流程图模拟每步间隔时长
        label_st.setToolTipText("流程图模拟每步间隔时长");
        textArea_st  = new JTextArea();
        button_begin = new JButton("开始模拟");   //开始按钮
        button_clear = new JButton("清空参数");   //清空按钮

        //给两个按钮添加监听器
        button_begin.addActionListener(this);
        button_clear.addActionListener(this);

        //设置大小
            //label
        label_pl.setPreferredSize(new Dimension(200, 20));
        label_ar.setPreferredSize(new Dimension(200, 20));
        label_sr.setPreferredSize(new Dimension(200, 20));
        label_s1.setPreferredSize(new Dimension(200, 20));
        label_s2.setPreferredSize(new Dimension(200, 20));
        label_qll.setPreferredSize(new Dimension(200, 20));
        label_ft.setPreferredSize(new Dimension(200, 20));
        label_st.setPreferredSize(new Dimension(200, 20));
//        label_st.setOpaque(true);
//        label_st.setBackground(Color.RED);
            //textArea
        textArea_pl.setPreferredSize(new Dimension(120,30));
        textArea_ar.setPreferredSize(new Dimension(120,30));
        textArea_sr.setPreferredSize(new Dimension(120,30));
        textArea_s1.setPreferredSize(new Dimension(120,30));
        textArea_s2.setPreferredSize(new Dimension(120,30));
        textArea_qll.setPreferredSize(new Dimension(120,30));
        textArea_ft.setPreferredSize(new Dimension(120,30));
        textArea_st.setPreferredSize(new Dimension(120,30));
            //button
        button_clear.setPreferredSize(new Dimension(160,30));
        button_begin.setPreferredSize(new Dimension(160,30));
        //全部塞进去
            //label && textArea
        panelLeft.add(label_pl);
        panelLeft.add(textArea_pl);
        panelLeft.add(label_ar);
        panelLeft.add(textArea_ar);
        panelLeft.add(label_sr);
        panelLeft.add(textArea_sr);
        panelLeft.add(label_s1);
        panelLeft.add(textArea_s1);
        panelLeft.add(label_s2);
        panelLeft.add(textArea_s2);
        panelLeft.add(label_qll);
        panelLeft.add(textArea_qll);
        panelLeft.add(label_ft);
        panelLeft.add(textArea_ft);
        panelLeft.add(label_st);
        panelLeft.add(textArea_st);
        //加一个空的给加间距
        JLabel tmp_label = new JLabel("");
        tmp_label.setPreferredSize(new Dimension(400, 20));
        panelLeft.add(tmp_label);
            //button
        panelLeft.add(button_clear);
        panelLeft.add(button_begin);

        /*panelCenter part*/
        panelCenter = new JPanel();
//        panelCenter.setBackground(Color.green);
        panelCenter.setLayout(null);
//        panelCenter.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        panelCenter.setBorder(BorderFactory.createTitledBorder("模拟流程示意"));

        //初始化控件
//        this.initPanelCenter();
        label_start = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/start.png")),0.3));
        label_sys_init = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/systemInit.png")),0.3));
        label_time_scan = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/timeScan.png")),0.3));
        label_proc_event_table = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/processEventTable.png")),0.3));
        label_simu_push_time = new JLabel(this.change(new ImageIcon((this.getClass().getResource("ProcessIcon/simulateClockPushEvent.png"))),0.3));
        label_proc_curr_event = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/processCurrentEvent.png")),0.3));
        label_if_end = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/ifEnd.png")),0.3));
        label_end = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/end.png")),0.3));

        //设置控件大小
        label_start.setBounds(80,40,260,40);
        label_start.setOpaque(true);//设置位透明背景，只有一设置为透明背景才能显示出来设置的背景颜色
//        label_sys_init.setPreferredSize(new Dimension(260,40));
        label_sys_init.setBounds(80,100,260,40);
        label_sys_init.setOpaque(true);
//        label_time_scan.setPreferredSize(new Dimension(260,40));
        label_time_scan.setBounds(80,160,260,40);
        label_time_scan.setOpaque(true);
//        label_proc_event_table.setPreferredSize(new Dimension(260,40));
        label_proc_event_table.setBounds(80,220,260,40);
        label_proc_event_table.setOpaque(true);
//        label_simu_push_time.setPreferredSize(new Dimension(260,40));
        label_simu_push_time.setBounds(80,280,260,40);
        label_simu_push_time.setOpaque(true);
//        label_proc_curr_event.setPreferredSize(new Dimension(260,40));
        label_proc_curr_event.setBounds(80,340,260,40);
        label_proc_curr_event.setOpaque(true);
//        label_if_end.setPreferredSize(new Dimension(290,60));
        label_if_end.setBounds(80,400,260,50);
        label_if_end.setOpaque(true);
//        label_end.setPreferredSize(new Dimension(260,40));
        label_end.setBounds(80,480,260,40);
        label_end.setOpaque(true);


        //流程图放进panelCenter
        panelCenter.add(label_start);
        panelCenter.add(label_sys_init);
        panelCenter.add(label_time_scan);
        panelCenter.add(label_proc_event_table);
        panelCenter.add(label_simu_push_time);
        panelCenter.add(label_proc_curr_event);
        panelCenter.add(label_if_end);
        panelCenter.add(label_end);
        //再为流程图补上箭头
        this.insertRowToFlowChart();

        /*panelRight part*/
        panelRight = new JPanel();
//        panelRight.setBackground(Color.pink);
        panelRight.setLayout(flowLayout_panelRight);
        panelRight.setBorder(BorderFactory.createTitledBorder("模拟过程状态"));

        //初始化控件
        label_sys_time = new JLabel("模拟系统时间：");
        label_sys_time_value = new JLabel();//("0.436");
        label_pnu = new JLabel("使用中的服务台数目：");
        label_pnu_value =new JLabel();//("4");
        label_niq = new JLabel("队列中的等待人数：");
        label_niq_value = new JLabel();//("99");
        label_es = new JLabel("当前系统状态：");
        label_es_value = new JLabel();//("判断模拟是否结束");    //初始应为空值
        label_leave_num = new JLabel("因超出系统容量离开的顾客：");
        label_leave_num_value = new JLabel();//("100");

        //把状态值在初始化的时候都给上
        this.event_status = this.initEventStatusMap();
        button_excel = new JButton("导出事件表到Excel");

        button_excel.addActionListener(this);
        //设置控件大小
        label_sys_time.setPreferredSize(new Dimension(160,20));
        label_sys_time_value.setPreferredSize(new Dimension(160,20));
        label_pnu.setPreferredSize(new Dimension(200,20));
        label_pnu_value.setPreferredSize(new Dimension(60,20));
        label_niq.setPreferredSize(new Dimension(200,20));
        label_niq_value.setPreferredSize(new Dimension(60,20));

        label_es.setPreferredSize(new Dimension(180,20));
        label_es_value.setPreferredSize(new Dimension(180,20));

        label_leave_num.setPreferredSize(new Dimension(280,20));
        label_leave_num_value.setPreferredSize(new Dimension(60,20));

        button_excel.setPreferredSize(new Dimension(160,30));
        //导出excel初始状态应为不可用
        button_excel.setEnabled(false);
        //把放value的label显示独特的样式
        this.InitValueLabel();
        //把控件放到对应的panel里面
        panelRight.add(label_sys_time);
        panelRight.add(label_sys_time_value);
        panelRight.add(label_pnu);
        panelRight.add(label_pnu_value);
        panelRight.add(label_niq);
        panelRight.add(label_niq_value);
        panelRight.add(label_es);
        panelRight.add(label_es_value);
        panelRight.add(label_leave_num);
        panelRight.add(label_leave_num_value);

        //加两个空的label来给加间距，给下面的按钮定个位
        JLabel tmp_label_0 = new JLabel("");
        JLabel tmp_label_1 = new JLabel("");
        tmp_label_0.setPreferredSize(new Dimension(360, 80));
        tmp_label_1.setPreferredSize(new Dimension(190, 30));
        panelRight.add(tmp_label_0);
        panelRight.add(tmp_label_1);

        panelRight.add(button_excel);
        /*tab 键切换文本框,其实没有luan用*/
        FocusTraversalPolicy Policy = new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                int index = textAreasList.indexOf(aComponent);
                return textAreasList.get((index+1)%textAreasList.size());
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                int index = textAreasList.indexOf(aComponent);
                return textAreasList.get((index-1+textAreasList.size())%textAreasList.size());
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return textAreasList.get(0);
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return textAreasList.get(textAreasList.size() -1);
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return textAreasList.get(0);
            }
        };
        //frame setting
        /*panelLeft.setFocusTraversalPolicyProvider(true);
        panelLeft.setFocusTraversalPolicy(Policy);*/
        this.setLayout(gridLayout_frame);
        this.add(panelLeft);
        this.add(panelCenter);
        this.add(panelRight);
//        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1200, 580);
        this.setLocation(500,400);
        this.setTitle("事件调度法");
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e){}

            @Override
            public void windowClosing(WindowEvent e) {
//                Q.setVisible(false);
                QSSESAFrame.this.setVisible(false);
                if( thread!=null){
                    QSSESAFrame.this.thread.stop();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if( QSSESAFrame.this.thread!=null){
//                    QSSESAFrame.this.thread.interrupt();
                    QSSESAFrame.this.thread.stop();
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //开始按钮
        if (e.getSource() == button_begin){
            simulationStart(mmnqs);
        }
        //清空按钮
        if (e.getSource() == button_clear){
            clearAllParamter();
        }
        //生成Excel表
        if(e.getSource() == button_excel){
            this.button_excel.setEnabled(false);//防止保存时间过长多次点击
            JFileChooser fc = new JFileChooser();
            fc.setDialogType(JFileChooser.SAVE_DIALOG);
            fc.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());//初始化目录，初始位置在桌面
            fc.setFileFilter(new FileNameExtensionFilter("Excel", "xlsx"));//添加文本文件过滤器,你可以添加更多

            fc.setVisible(true);
            fc.showDialog(this,"另存为");
            File fo = fc.getSelectedFile();
            if (fo == null){
                button_excel.setEnabled(true);
                return;
            }
            File tmpFile = fo.getParentFile();
            String file_name = fo.getName();
            file_name += ".xlsx";
            String finalFile_name = file_name;
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try{
                        super.run();
                        try {

                            if (mmnqs.wirteRecordsInEXCEL(new FileOutputStream(new File(tmpFile, finalFile_name)))){
                                EventQueue.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        JOptionPane.showMessageDialog(null, "Excel保存成功","提示",JOptionPane.PLAIN_MESSAGE);
                                    }
                                });
                            }else {
                                EventQueue.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        JOptionPane.showMessageDialog(null, "Excel保存失败","提示",JOptionPane.PLAIN_MESSAGE);
                                    }
                                });
                            }
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }catch (Exception ex){
                        //
                    }finally {
                        button_excel.setEnabled(true);
                    }
                }
            };
            thread.start();
            System.out.println("button_excel");
        }
    }
    //
    private void simulationStart(MMNQueuingSystem mmnqs){
        button_excel.setEnabled(false);
        if(!this.verifyInputParamter()){return;}
        this.simulationStartDisabled();
        //开始模拟
        this.mmnqs = new MMNQueuingSystem(this.plat_num, this.arrive_rate, this.serveice_rate, this.seed1, this.seed2, this.queue_length_limit,this.finish_time,this.sleep_time);
        mmnqs=this.mmnqs;
        /*添加监听器，执行回调*/
        /*正常过程应是->  开始 - 初始化 - 时间扫描 - 处理事件表 - 模拟时钟推进当前时间 - 处理当前事件 - 判断模拟是否接受 - 结束*/
        /*这里由于模拟过程事件表本身就是经处理过有序，时间扫描过程未能出现，想要在流程图上显示就要动手脚了*/
        MMNQueuingSystem finalMmnqs = mmnqs;
        mmnqs.addInitEventListener(new InitEventListener() {
            @Override
            public void onInit(InitEvent e) {
                //此处回调初始化过程时的结果
                simuInitSystem(finalMmnqs);
                System.out.println("初始化：");
            }
        });
        mmnqs.addGetNextEventEventListener(new GetNextEventEventListener() {
            @Override
            public void onGetNextEvent(GetNetEventEvent e) {
                try{
                    simuTimeScan(finalMmnqs);//时间扫描被包含在此处回调中，设置一个延迟只为了能够使流程图清晰
                    System.out.println("时间扫描");
                    Thread.sleep(sleep_time);
                }catch (InterruptedException exception){
                    exception.printStackTrace();
                }

                //回调处理事件表
                simuProcEventTable(finalMmnqs);
                System.out.println("处理事件表");

            }
        });
        mmnqs.addSystemTimePushEventListener(new SystemTimePushEventListener() {
            @Override
            public void onSystemTimePush(SystemTimePushEvent e) {
                /*//此处回调模拟时钟推进当前时间
                simuPushTime(finalMmnqs);
                System.out.println("模拟时钟推进当前时间");//如果两个事件触发时间相同，则连续两次事件第二次不再触发该回调*/
            }
        });
        mmnqs.addEventStartEVentListener(new EventStartEVentListener() {
            @Override
            public void onEventStart(EventStartEvent e) {
                try{
                    //此处只是为了流程图显示需要，如果需要实际过程，注释掉try中内容，并取消上一条注释
                    simuPushTime(finalMmnqs);//模拟时钟推进也被包含在此处回调中，设置一个延迟只为了能够使流程图清晰
                    System.out.println("模拟时钟推进当前时间");
                    Thread.sleep(sleep_time);
                }catch (InterruptedException exception){
                    exception.printStackTrace();
                }
                //此处回调一次事件开始结果和状态，不是模拟开始
                simuProcCurrEvent(finalMmnqs);
                System.out.println("一次事件开始");
            }
        });
        mmnqs.addEventFinishEventListener(new EventFinishEventListener() {
            @Override
            public void onEventFinish(EventFinishEvent e) {
                //某一事件处理结束,事件处理结束判断是否有下一个事件待处理
                simuIfEnd(finalMmnqs);
                System.out.println("一次事件结束,判断模拟是否结束");
            }
        });
        mmnqs.addFinishEventListener(new FinishEventListener() {
            @Override
            public void onFinish(FinishEvent e) {
                //模拟结束回调
                simuEnd(finalMmnqs);
                System.out.println("模拟结束");
                button_excel.setEnabled(true);  //模拟结束设置为可导出excel表格
                simulationEndRecovery();
            }
        });
//        开启一个线程进行模拟
        this.thread = new Thread(){
            public void run(){
                try{
                    /*模拟开始之前的开始状态*/
                    simuStart(finalMmnqs);
                    Thread.sleep(sleep_time);
                    finalMmnqs.Start();
                    sleep(1);
                }catch (InvocationTargetException | IllegalAccessException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        this.thread.start();


    }
    /*
    * 初始化事件状态
    * */
    private Map<String, String> initEventStatusMap(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("Start","事件开始");
        map.put("SystemInit","系统初始化");    //流程里面有
        map.put("TimeScan","时间扫描");
        map.put("DealEventList","处理事件表");
        map.put("PropulsionEvent","推进事件");
        map.put("DealEvent","处理当前事件");
        map.put("JudgeOver","判断模拟是否结束");
        map.put("End","模拟结束");
        return map;
    }
    /*
     * 统一所有子窗口设置字体样式
     * */
    private void InitGlobalFont(Font font){

        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
    /*
     * 统一所有子窗口设置字体样式
     * */
    private void InitGlobalFont(){

        FontUIResource fontRes = new FontUIResource(new Font("微软雅黑",Font.PLAIN,14));
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
    /*
    * 初始化取值处字体
    * */
    private void InitValueLabel(){
        label_sys_time_value.setFont(new Font("微软雅黑",Font.BOLD,20));
        label_pnu_value.setFont(new Font("微软雅黑",Font.BOLD,20));
        label_niq_value.setFont(new Font("微软雅黑",Font.BOLD,20));
        label_es_value.setFont(new Font("微软雅黑",Font.BOLD,20));
        label_leave_num_value.setFont(new Font("微软雅黑",Font.BOLD,20));
    }
    /*
    * 模拟开始后，所有按钮不可使用，文本框也不可更改
    * */
    private void simulationStartDisabled(){
        this.textArea_pl.setEnabled(false);
        this.textArea_ar.setEnabled(false);
        this.textArea_sr.setEnabled(false);
        this.textArea_s1.setEnabled(false);
        this.textArea_s2.setEnabled(false);
        this.textArea_qll.setEnabled(false);
        this.textArea_ft.setEnabled(false);
        this.textArea_st.setEnabled(false);
        this.button_clear.setEnabled(false);
        this.button_begin.setEnabled(false);
    }
    /*
     * 模拟结束后，所有按钮，文本框也恢复正常状态
     * */
    private void simulationEndRecovery(){
        this.textArea_pl.setEnabled(true);
        this.textArea_ar.setEnabled(true);
        this.textArea_sr.setEnabled(true);
        this.textArea_s1.setEnabled(true);
        this.textArea_s2.setEnabled(true);
        this.textArea_qll.setEnabled(true);
        this.textArea_ft.setEnabled(true);
        this.textArea_st.setEnabled(true);
        this.button_clear.setEnabled(true);
        this.button_begin.setEnabled(true);
    }
    /*
    * 开始之前状态与参数的初始化
    * */
    private boolean verifyInputParamter(){
        //首先获取到参数列表，并验证
        try {
            this.plat_num = Long.parseLong(this.trim(this.textArea_pl.getText()));   //服务台数目
            if(this.plat_num <= 0){throw new Exception();}
        }catch (Exception e){//可能 抛出的异常是 NumberFormatException, 不过此处不需要 识别过多内容，直接Exception即可
            JOptionPane.showMessageDialog(null, "输入的服务台数目无效，必须为正整数","提示",JOptionPane.PLAIN_MESSAGE);
            return false;
        }

        try{
            this.arrive_rate = Double.parseDouble(this.trim(this.textArea_ar.getText()));      //到达率, Double
            if(this.arrive_rate <= 0){throw new Exception();}
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "输入的到达率无效，必须为正实数","提示",JOptionPane.PLAIN_MESSAGE);
            return false;
        }

        try {
            this.serveice_rate = Double.parseDouble(this.trim(this.textArea_sr.getText()));    //服务率, Double
            if(this.arrive_rate <= 0){throw new Exception();}
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "输入的服务率无效，必须为正实数","提示",JOptionPane.PLAIN_MESSAGE);
            return false;
        }

        try{
            this.queue_length_limit = Long.parseLong(this.trim(this.textArea_qll.getText())); //最大队长限制
            if(this.queue_length_limit <= 0){throw new Exception();}
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "输入的最大队列长度无效，必须为正整数","提示",JOptionPane.PLAIN_MESSAGE);
            return false;
        }

        try{

            this.finish_time = Long.parseLong(this.trim(this.textArea_ft.getText()));    //结束时间限制
            if(this.finish_time <= 0){throw new Exception();}
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "输入的系统结束时间无效，必须为正整数","提示",JOptionPane.PLAIN_MESSAGE);
            return false;
        }

        try{
            this.sleep_time = Integer.parseInt(this.trim(this.textArea_st.getText()));     //休眠等待时间（或者叫步长时间）
            if(this.sleep_time < 0){throw new Exception();}
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "输入的系统结束时间无效，必须为正整数","提示",JOptionPane.PLAIN_MESSAGE);
            return false;
        }

        try{
            if(this.trim(this.textArea_s1.getText()).isEmpty() || this.trim(this.textArea_s1.getText()).equals("非必填")){
                this.seed1 = new Random().nextLong();
                this.textArea_s1.setText(String.valueOf(this.seed1));
            }else {
                this.seed1 = Long.parseLong(this.trim(this.textArea_s1.getText()));     //种子一, Long, 到达率的随机数种子
            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "输入的到达率的随机数种子无效，必须为整数","提示",JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        try{
            if(this.trim(this.textArea_s2.getText()).isEmpty() || this.trim(this.textArea_s2.getText()).equals("非必填")){
                this.seed2 = new Random().nextLong();
                this.textArea_s2.setText(String.valueOf(this.seed2));
            }else {
                this.seed2 = Long.parseLong(this.trim(this.textArea_s2.getText()));     //种子二, Long, 服务率的随机数种子
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "输入的服务率的随机数种子无效，必须为整数","提示",JOptionPane.PLAIN_MESSAGE);
            return false;
        }
//        System.out.println("this.plat_num"+this.plat_num);
//        System.out.println("this.arrive_rate："+this.arrive_rate);
//        System.out.println("this.serveice_rate"+this.serveice_rate);
//        System.out.println("this.seed1"+this.seed1);
//        System.out.println("this.seed2"+this.seed2);
//        System.out.println("this.num_in_queue"+this.queue_length_limit);
//        System.out.println("this.finish_time"+this.finish_time);
//        System.out.println("this.sleep_time"+this.sleep_time);

        return true;
    }
    /*
    * 图片缩放
    * */
    public ImageIcon change(ImageIcon image,double i){//  i 为放缩的倍数
        int width=(int) (image.getIconWidth()*i);
        int height=(int) (image.getIconHeight()*i);
        Image img=image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);//第三个值可以去查api是图片转化的方式
        ImageIcon image2=new ImageIcon(img);
        return image2;
    }
    /*
    * 流程图箭头插入
    * */
    private void insertRowToFlowChart(){
        JLabel lable_tmp;
        lable_tmp = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/row.png")),0.2));
        lable_tmp.setBounds(185,75,40,25);
        lable_tmp.setOpaque(true);
        this.panelCenter.add(lable_tmp);

        lable_tmp = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/row.png")),0.2));
        lable_tmp.setBounds(185,135,40,25);
        lable_tmp.setOpaque(true);
        this.panelCenter.add(lable_tmp);

        lable_tmp = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/row.png")),0.2));
        lable_tmp.setBounds(185,195,40,25);
        lable_tmp.setOpaque(true);
        this.panelCenter.add(lable_tmp);

        lable_tmp = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/row.png")),0.2));
        lable_tmp.setBounds(185,255,40,25);
        lable_tmp.setOpaque(true);
        this.panelCenter.add(lable_tmp);

        lable_tmp = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/row.png")),0.2));
        lable_tmp.setBounds(185,315,40,25);
        lable_tmp.setOpaque(true);
        this.panelCenter.add(lable_tmp);

        lable_tmp = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/row.png")),0.2));
        lable_tmp.setBounds(185,375,40,25);
        lable_tmp.setOpaque(true);
        this.panelCenter.add(lable_tmp);

        lable_tmp = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/rowYes.png")),0.6));
        lable_tmp.setBounds(190,445,80,30);
        lable_tmp.setOpaque(true);
        this.panelCenter.add(lable_tmp);

        lable_tmp = new JLabel(this.change(new ImageIcon(this.getClass().getResource("ProcessIcon/rowNo.png")),0.4));
        lable_tmp.setBounds(0,95,200,380);
        lable_tmp.setOpaque(true);
        this.panelCenter.add(lable_tmp);
    }

    /*
    * 清空已填参数
    * */
    private void clearAllParamter(){
        textArea_pl.setText("");
        textArea_ar.setText("");
        textArea_sr.setText("");
        textArea_s1.setText("");
        textArea_s2.setText("");
        textArea_qll.setText("");
        textArea_ft.setText("");
        textArea_st.setText("");
    }
    /**
     * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    private String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");
        }
        return result;
    }
    /*
     * @param obj MMN对象实例
     * @desciption 模拟开始
    * */
    private void simuStart(MMNQueuingSystem obj){
        this.setAllBackGroundNull();
        this.label_start.setBackground(Color.YELLOW);
        displayStatue(obj,this.event_status.get("Start"));
    }
    /*
     * @param obj MMN对象实例
     * @desciption 系统初始化
    * */
    private void simuInitSystem(MMNQueuingSystem obj){
        this.setAllBackGroundNull();
        this.label_sys_init.setBackground(Color.YELLOW);
        displayStatue(obj,this.event_status.get("SystemInit"));
    }
    /*
     * @param obj MMN对象实例
     * @desciption 时间扫描
    * */
    private void simuTimeScan(MMNQueuingSystem obj){
        this.setAllBackGroundNull();
        this.label_time_scan.setBackground(Color.YELLOW);
        displayStatue(obj,this.event_status.get("TimeScan"));
    }
    /*
     * @param obj MMN对象实例
     * @desciption 处理事件表
    * */
    private void simuProcEventTable(MMNQueuingSystem obj){
        this.setAllBackGroundNull();
        this.label_proc_event_table.setBackground(Color.YELLOW);
        displayStatue(obj,this.event_status.get("DealEventList"));
    }
    /*
    * @param obj MMN对象实例
    * @desciption 模拟时钟推进
    * */
    private void simuPushTime(MMNQueuingSystem obj){
        this.setAllBackGroundNull();
        this.label_simu_push_time.setBackground(Color.YELLOW);
        displayStatue(obj,this.event_status.get("PropulsionEvent"));
    }
    /*
    * 处理当前事件
    * */
    private void simuProcCurrEvent(MMNQueuingSystem obj){
        this.setAllBackGroundNull();
        this.label_proc_curr_event.setBackground(Color.YELLOW);
        displayStatue(obj,this.event_status.get("DealEvent"));
    }
    /*
     * @param obj MMN对象实例
     * @desciption 判断模拟是否结束
    * */
    private void simuIfEnd(MMNQueuingSystem obj){
        this.setAllBackGroundNull();
        this.label_if_end.setBackground(Color.YELLOW);
        displayStatue(obj,this.event_status.get("JudgeOver"));
    }
    /*
    * @param obj MMN对象实例
    * @desciption 模拟结束
    * */
    private void simuEnd(MMNQueuingSystem obj){
        this.setAllBackGroundNull();
        this.label_end.setBackground(Color.YELLOW);
        this.textArea_s1.setText("非必填");
        this.textArea_s2.setText("非必填");
        displayStatue(obj,this.event_status.get("End"));
    }
    /*
    *@param obj MMN对象实例
    * @descoption 显示当前状态参数
    * */
    private void displayStatue(MMNQueuingSystem obj, String statueValue){
        label_sys_time_value.setText(String.format("%.4f", obj.getSystemTime()));//系统模拟时间
        label_pnu_value.setText(String.valueOf(obj.usedServer));    //使用中的服务台数
        label_niq_value.setText(String.valueOf(obj.queueLength));    //排队中的顾客数，队列中的等待人数
        label_es_value.setText(statueValue);//当前系统状态
        label_leave_num_value.setText(String.valueOf(obj.rejectedCustomer));  //超出队长而离开系统的顾客数目
    }
    /*
    * 将流程图中所有JLabel背景都设置为空
    * */
    private void setAllBackGroundNull(){
        this.label_start.setBackground(null);
        this.label_sys_init.setBackground(null);
        this.label_time_scan.setBackground(null);
        this.label_proc_event_table.setBackground(null);
        this.label_simu_push_time.setBackground(null);
        this.label_proc_curr_event.setBackground(null);
        this.label_if_end.setBackground(null);
        this.label_end.setBackground(null);
    }
}
