# MMNQueueSystem
Event Scheduling simulation to realize MMN queuing model

##### if you are Chinese, Skip the English part because the Chinese and English content are exactly the same

##### if you are not Chinese, maybe you can tanslate the Chinese to the language you want

##### The repository only contain source code. Build and run the project yourself

##### Code comments are written in Chinese, I hope people who are interested can understand.

### Explain two concepts.

#### What is Event Scheduling? (from wiki) [DoortoWiki](https://en.wikipedia.org/wiki/Event_scheduling)

Event scheduling is the activity of finding a suitable time for an event such as meeting, conference, trip, etc. It is an important part of event planning that is usually carried out at its beginning stage.

In general, event scheduling must take into account what impact particular dates of the event could have on the success of the event. When organizing a scientific conference, for example, organizers might take into account the knowledge in which periods classes are held at universities, since it is expected that many potential participants are university professors. They should also try to check that no other similar conferences are held at the same time, because overlapping would make a problem for those participants who are interested in attending all conferences.

When it is well known who is expected to attend the event (e.g. in the case of a project meeting), organizers usually try to synchronize the time of the event with planned schedules of all participants. This is a difficult task when there are many participants or when the participants are located at distant places. In such cases, the organizers should first define a set of suggested dates and address a query about suitable dates to potential participants. After response is obtained from all participants, the event time suitable for most of participants is selected. This procedure can be alleviated by internet tools.[1]

#### What is MMN Queuing System?

His meaning is from his name. The queuing system is a system that studies how a system queues and receives services and time distribution.The queuing system mainly solves the problem of service and being served, which is the main application area of system simulation. Examples of queuing models are like bank, hospital, airport, and etc..

MMN, Three characters have their own meanings.The first 'M' means customer arrival interval is a random number following a Poisson distribution. The second 'M' means customer service interval is a random number following an exponential distribution.'N' means there is more than one service desk, the specific number of service desks is 'N'.

##### Check the source code for the rest.


## 中文解释
###### 首先如果有想法的话可以考虑直接把上面的英文部分直接翻译，但是真的不建议

##### 这里的所有代码仅仅是源代码，编译运行请自便。基本所有注释都是中文写的，我觉得其实也不一定会有歪果仁能看到

### 两个主要的概念

#### 事件调度法

###### 维基百科给的翻译
事件计划是为会议，会议，旅行等事件找到合适时间的活动。它是事件计划的重要组成部分，通常在其开始阶段就进行。

通常，事件调度必须考虑事件的特定日期可能对事件的成功产生什么影响。例如，在组织一次科学会议时，组织者可能会考虑到大学上课时间的知识，因为预计很多潜在的参与者都是大学教授。他们还应尝试检查是否没有同时举行其他类似的会议，因为重叠会对那些有兴趣参加所有会议的参与者造成麻烦。

当众所周知应该由谁参加活动时（例如，在项目会议中），组织者通常会尝试将活动时间与所有参与者的计划时间表进行同步。当参与者很多或参与者位于遥远的地方时，这是一项艰巨的任务。在这种情况下，组织者应首先定义一组建议日期，然后向潜在参与者提出有关合适日期的查询。从所有参与者获得响应后，选择适合大多数参与者的事件时间。可以通过Internet工具减轻此过程。

###### 事件调度法基本思想
用“事件”的观点来抽象真实系统，即：
定义事件及每一事件发生对系统状态的影响，并按事件发生时间顺序来确定每类事件发生时系统中的各实体之间的逻辑关系及其状态。事件调度法的模拟时钟的推进，是按照下一事件的发生时刻来触发。大多数发生的事件，两个相邻事件发生的时间间隔一般是随机的，因此事件调度法是变步长法。

模拟模型中所有事件均按时间先后顺序存放在事件表中；模型中要设计一个时间控制部件实现模拟时钟的管理与控制。每当处理一类事件时，它总是从事件表中选择最早发生的事件；并将模拟时钟推进到该事件发生的时间；然后调用与该事件相应的事件处理模块；事件处理模块在执行完后都必须返回到时间控制部件。这样，事件的选择与处理不断地进行，模拟时钟按事件时间往前推进，直到模拟终止的条件满足为止。

###### 事件调度法算法表述

1）执行初始化操作：模拟时钟TIME、系统状态及统计量等置初始值：
置初始时间t=t0，结束时间t∞=te
事件表初始化，置系统初始事件
成分状态初始化:S=((sa1,ta1),…,(sam,tam),sam+1,…,san)
2）操作、扫描事件表，将时钟TIME增加到下一个最早发生事件的时间上；
取出具有t(s)=min{ta|a∈CA}事件记录
修改事件表
3）推进模拟时钟 TIME=t(s)
4）While(TIME<=t∞)则执行
Case 根据事件类型i
i=1执行第1类事件处理程序，处理该事件，相应的改变系统状态；
i=2执行第2类事件处理程序，处理该事件，相应的改变系统状态；
。。。。
i=m执行第m类事件处理程序，处理该事件，相应的改变系统状态；
endcase 
取出具有t(s)=min{ta|a∈CA}事件记录（若有若干个，则按解结规则处理）
置模拟时间TIME=t(s)，若模拟期间未到，返回2)，否则，执行下一步；
收集统计数据；
endwhile
5）分析收集的统计数据，产生报告。

###### 事件调度法流程图在代码的GUI界面中，编译运行一下就看到了

#### 排队模型

排队系统解决的主要是服务与被服务的问题，是系统模拟的主要应用领域。排队模型的例子如银行，机场，医院等。。。
那么MMN是什么呢？

MMN其实只是一种对排队系统标注的方法具体标注格式如下：
X/Y/Z/A/B/C
X：到达间隔时间分布（interval-time distribution）
Y：服务时间分布（service-time distribution）
对X、Y，包括：M—泊松到达过程，D—固定模式，Ek—k阶Erlang模式，G—任意型或一般独立型。
Z：并行服务台的数目（the number of parallel servers）;
A：系统容量（system capacity）
B：顾客的容量（size of calling population）
C：服务规则，如FCFS, LCFS
例：M/M/1/∞/ ∞/LCFS ：单服务台、负指数到达、队列能力无限，潜在到达顾客无限，LCFS规则。可简写为：M/M/1。

#### 具体怎么写的，去看源码吧。


###### 这个东西是我跟另外一位小伙伴一起写的，不是我个人单独完成的，特此声明



