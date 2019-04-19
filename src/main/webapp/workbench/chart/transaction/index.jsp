<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>title</title>
    <script src="ECharts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script>

        $(function () {

            //在页面加载完毕后，绘制统计图
            getCharts();

        })

        function getCharts() {

            $.ajax({

                url : "workbench/transaction/getCharts.do",
                type : "get",
                dataType : "json",
                success : function(data){

                    /*
                          data
                            {"total":100,"dataList":[{value: 数量, name: '阶段名称'},{value: 数量, name: '阶段名称'},{value: 数量, name: '阶段名称'}]}
                     */
                    // 基于准备好的dom，初始化echarts实例
                    //表示echarts要在我们准备好的盒子中画图了
                    var myChart = echarts.init(document.getElementById('main'));

                    // 指定图表的配置项和数据
                    //option：表示我们要画的图
                    var option = {
                        title: {
                            text: '交易漏斗图',
                            subtext: '统计不同交易阶段数量的漏斗图'
                        },

                        series: [
                            {
                                name:'交易漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: data.total,   //max需要我们从后台传递过来，表示查询的总条数
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data:data.dataList   //data：表示展现的数据，需要我们从后台传递过来
                                    /*[
                                        {value: 360, name: '07成交'},
                                        {value: 40, name: '02需求分析'},
                                        {value: 120, name: '03价值建议'},
                                        {value: 80, name: '06谈判/复审'},
                                        {value: 10, name: '08因竞争丢失'}
                                    ]*/
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    //myChart：初始化后的盒子
                    //setOption：表示要画图的方法
                    //option：我们要画的图
                    //myChart.setOption(option)：在指定的div盒子中，绘制echats图
                    myChart.setOption(option);


                }

            })







        }

    </script>
</head>
<body>

    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 800px;height:400px;"></div>

</body>
</html>
