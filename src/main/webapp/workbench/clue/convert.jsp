<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		//为市场活动源的 "放大镜"图标，绑定事件，打开搜索市场活动的模态窗口
		$("#toSearchActivityBtn").click(function () {

			$("#searchActivityModal").modal("show");

		})

		//为搜索市场活动的文本框绑定敲键盘事件，如果触发的是回车键，则根据关键字搜索市场活动列表
		$("#aname").keydown(function (event) {

			if(event.keyCode==13){

				//查询并展现市场活动列表
				$.ajax({

					url : "workbench/clue/getActivityListByName.do",
					data : {

						"aname" : $.trim($("#aname").val())

					},
					type : "get",
					dataType : "json",
					success : function(data){

						/*

							data
								[{市场活动1},{2},{3}]

						 */

						var html = "";

						$.each(data,function (i,n) {

							html += '<tr>';
							html += '<td><input type="radio" name="xz" value="'+n.id+'"/></td>';
							html += '<td id="'+n.id+'">'+n.name+'</td>';
							html += '<td>'+n.startDate+'</td>';
							html += '<td>'+n.endDate+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '</tr>';

						})

						$("#activityBody").html(html);


					}

				})


				//操作结束后，不要忘记及时终止操作，防止回车键对模态窗口默认的刷新行为
				return false;

			}

		})

		//为提交按钮绑定事件，提交选中的市场活动信息
		$("#submitActivityBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){

				alert("请选择需要提交的市场活动信息");

			}else{

				//取得单选框的value值（id），为交易表单的隐藏域中的id赋值
				var id = $xz.val();
				$("#activityId").val(id);

				//取得选中的td标签对，取得里面的内容（选中的市场活动的名称）
				var activityName = $("#"+id).html();
				//将取出的内容，为交易表单中的市场活动名称赋值
				$("#activityName").val(activityName);

				//关闭模态窗口
				$("#searchActivityModal").modal("hide");

			}


		})


		//为转换按钮绑定事件，执行线索的转换操作
		$("#convertBtn").click(function () {

				/*

					请求路径:workbench/clue/convert.do

					请求参数:线索id
							如果需要创建一笔交易，还需要为后台提供交易表单中的参数信息（交易金额，交易名称，预计成交日期，阶段，市场活动源）


					路径和参数确定完毕后，选择以传统请求还是ajax请求，来进行操作？

						ajax：重点在局部刷新，提高用户的视觉体验
						传统请求：页面需要全局刷新

						转换操作后，该记录就已经删除了，所以没有任何做局部刷新的必要，
						所以我们使用传统请求方式到后台，后台在处理完请求后，重定向到列表页就可以了

				 */

				/*

					对于创建交易的参数的具体分析：

						首先我们需要确定的是，无论是否创建交易，我们发出的请求路径肯定是
															  workbench/clue/convert.do
															 （无论是否创建交易，我们的主业务都是线索转换，所以这个请求路径是统一不变的）
						如果需要创建交易：
							参数：线索id,交易金额，交易名称，预计成交日期，阶段，市场活动源
						如果不需要创建交易：
							参数：线索id

				 */

				/*

					如何判断是否需要创建交易？
						判断 "为客户创建交易"的复选框有没有挑√
						如果挑√了，表示在线索转换的过程中，需要伴随着创建一笔交易
						如果没有挑√，则只执行线索转换的业务，不需要创建交易


				 */


				//判断有没有挑√
				//如果挑√了，需要创建交易
				if($("#isCreateTransaction").prop("checked")){

					//除了传递线索id之外，还需要传递表单中的参数信息
					//如果按照以下形式传递参数，太麻烦
					//window.location.href = "workbench/clue/convert.do?clueId=xxx&name=xxx&money=xx.......";

					//以提交表单的形式来发出请求，表单中的参数，以name属性传递的
					$("#tranForm").submit();

				//如果没有挑√，不需要创建交易
				}else{

					//传递线索id即可
					window.location.href = "workbench/clue/convert.do?clueId=${param.id}";

				}




		})

	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activityBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitActivityBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >

		<form action="workbench/clue/convert.do" id="tranForm" method="post">


          <input type="hidden" name="flag" value="a"/>

          <input type="hidden" name="clueId" value="${param.id}"/>

		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
		    	<c:forEach items="${stageList}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="toSearchActivityBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
			<input type="hidden" id="activityId" name="activityId"/>
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" id="convertBtn" type="button" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>