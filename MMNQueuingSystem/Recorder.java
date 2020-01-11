package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.Event;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Recorder {
    private List<SystemStateEntity> records;
    private MMNQueuingSystem systemSet;

    public Recorder(MMNQueuingSystem systemSet){
        records=new ArrayList<>();
        this.systemSet=systemSet;
    }

    public List<SystemStateEntity> getRecords() {
        return records;
    }

    /**
     * 添加一个系统状态记录
     * @param systemStateEntity 系统状态
     */
    public void addState(SystemStateEntity systemStateEntity){
        records.add(systemStateEntity);
    }

    /**
     * 添加一个系统状态记录
     */
    public void addState(double systemTime, long queueLength, long usedServer, long rejectedCustomer, Event event){
        records.add(new SystemStateEntity(systemTime, queueLength, usedServer, rejectedCustomer, event));
    }

    public boolean write(OutputStream outputStream){

        XSSFWorkbook xssfWorkbook=new XSSFWorkbook();
        XSSFSheet xssfSheet=xssfWorkbook.createSheet("QueueingSystemRecord");
        XSSFRow titleRow= xssfSheet.createRow(0);
        //TODO 更多统计信息
        //设置字体
        //写表头
        String[] titles={"模拟系统时间","发生的事件","排队中的顾客数","使用中的服务台数","因队列长度不足而离开的顾客数","累计到达顾客数","累计完成服务顾客数","完成排队的顾客数","所有顾客的排队等待时间","平均等待时间","队列中的平均人数","服务台平均利用率"};
        for(int i=0;i<titles.length;++i){
            XSSFCell xssfCell=titleRow.createCell(i, CellType.STRING);
            xssfCell.setCellValue(new String(titles[i].getBytes(), StandardCharsets.UTF_8));
        }
        //写记录
        for(int i=0;i<records.size();++i){
            XSSFRow xssfRow=xssfSheet.createRow(i+1);
            XSSFCell xssfCellSystemTime=xssfRow.createCell(0, CellType.NUMERIC);
            xssfCellSystemTime.setCellValue(records.get(i).systemTime);
            XSSFCell xssfCellEvent=xssfRow.createCell(1, CellType.STRING);
            xssfCellEvent.setCellValue(records.get(i).eventLastHappend.getType());
            XSSFCell xssfCellQueueLength=xssfRow.createCell(2, CellType.NUMERIC);
            xssfCellQueueLength.setCellValue(records.get(i).queueLength);
            XSSFCell xssfCellUsedServer=xssfRow.createCell(3, CellType.NUMERIC);
            xssfCellUsedServer.setCellValue(records.get(i).usedServer);
            XSSFCell xssfCellRejectedCustomer=xssfRow.createCell(4, CellType.NUMERIC);
            xssfCellRejectedCustomer.setCellValue(records.get(i).rejectedCustomer);
            //累计到达顾客数
            xssfRow.createCell(5, CellType.NUMERIC).setCellValue(getAllArriveCustomer(i));
            //累计完成服务顾客数
            xssfRow.createCell(6, CellType.NUMERIC).setCellValue(getAllServicedCustomer(i));
            //完成排队的顾客数
            xssfRow.createCell(7, CellType.NUMERIC).setCellValue(getCustomersFinishedQueue(i));
            //所有顾客的排队等待时间
            xssfRow.createCell(8, CellType.NUMERIC).setCellValue(getTotalWaitingTimeInQueue(i));
            //平均等待时间
            xssfRow.createCell(9, CellType.NUMERIC).setCellValue(CustomersFinishedQueue==0?0:TotalWaitingTimeInQueue/CustomersFinishedQueue);
            //队列中的平均人数
            xssfRow.createCell(10, CellType.NUMERIC).setCellValue(getAverageNumberInQueue(i));
            //服务台平均利用率
            xssfRow.createCell(11, CellType.NUMERIC).setCellValue(getAverageServiceUsedRate(i));
        }
        //写入文件
        try {
            xssfWorkbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //统计累计到达顾客数
    private long AllArriveCustomer=0;
    private long getAllArriveCustomer(int indexOfRecord){
        if(records.get(indexOfRecord).eventLastHappend.getType().equals("CustomerArriveEvent")){
            AllArriveCustomer++;
        }
        return AllArriveCustomer;
    }

    //统计累计完成服务顾客数
    private long AllServicedCustomer=0;
    private long getAllServicedCustomer(int indexOfRecord){
        if(records.get(indexOfRecord).eventLastHappend.getType().equals("FinishServiceEvent")){
            AllServicedCustomer++;
        }
        return AllServicedCustomer;
    }

    //统计完成排队的顾客数
    private long CustomersFinishedQueue=0;
    private long getCustomersFinishedQueue(int indexOfRecord){
        if(indexOfRecord==0){
            if(records.get(indexOfRecord).eventLastHappend.getType().equals("CustomerArriveEvent")){
                CustomersFinishedQueue++;
            }
        }
        else if(records.get(indexOfRecord).eventLastHappend.getType().equals("CustomerArriveEvent")){
            if(records.get(indexOfRecord).usedServer-records.get(indexOfRecord-1).usedServer ==1){
                CustomersFinishedQueue++;
            }
        }
        else if(records.get(indexOfRecord).eventLastHappend.getType().equals("FinishServiceEvent")){
            if(records.get(indexOfRecord-1).queueLength-records.get(indexOfRecord).queueLength == 1){
                CustomersFinishedQueue++;
            }
        }
        return CustomersFinishedQueue;
    }

    //统计所有顾客的排队等待时间
    private double TotalWaitingTimeInQueue=0;
    private double getTotalWaitingTimeInQueue(int indexOfRecord){
        if(indexOfRecord>0){
            double period=records.get(indexOfRecord).systemTime-records.get(indexOfRecord-1).systemTime;
            TotalWaitingTimeInQueue+=period*records.get(indexOfRecord-1).queueLength;
        }
        return TotalWaitingTimeInQueue;
    }

    //统计队列中的平均人数
    private double TotalQueueLengthTime;
    private double getAverageNumberInQueue(int indexOfRecord){
        if(indexOfRecord>0 && records.get(indexOfRecord).systemTime>0){
            double period=records.get(indexOfRecord).systemTime-records.get(indexOfRecord-1).systemTime;
            TotalQueueLengthTime+=records.get(indexOfRecord-1).queueLength*period;
            return TotalQueueLengthTime/records.get(indexOfRecord).systemTime;
        }
        return 0;
    }

    //统计服务台平均利用率
    private double TotalServiceUsedTime=0;
    private double getAverageServiceUsedRate(int indexOfRecord){
        if(indexOfRecord>0 && records.get(indexOfRecord).systemTime>0){
            double period=records.get(indexOfRecord).systemTime-records.get(indexOfRecord-1).systemTime;
            TotalServiceUsedTime+=records.get(indexOfRecord).usedServer*period;
            return TotalServiceUsedTime/records.get(indexOfRecord).systemTime/systemSet.N;
        }
        return 0;
    }
}
