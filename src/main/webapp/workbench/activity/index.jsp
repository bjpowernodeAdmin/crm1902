<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>


<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

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


		//为创建按钮来绑定事件，目的就是为了打开添加操作的模态窗口
		$("#addBtn").click(function () {

			/*
				打开/关闭 模态窗口的方式

					通过id找到需要打开的模态窗口的jquery对象，调用modal方法来操作模态窗口，
													为该方法传递参数 show：打开模态窗口  hide：关闭模态窗口

			 */

			//$("#createActivityModal").modal("show");

			//先过后台取得用户信息列表，将用户信息（姓名）铺在所有者的下拉框中之后，打开模态窗口

			$.ajax({

				url : "workbench/activity/getUserList.do",
				type : "get",
				dataType : "json",
				success : function(data){


					/*

						data
							[{用户1},{用户2},{用户3}...]

					 */

					var html = "<option></option>";
					//每一个n，代表每一个用户的json对象
					$.each(data,function (i,n) {

						html += "<option value='"+n.id+"'>"+n.name+"</option>";

					})


					$("#create-owner").html(html);

					//取得当前登录的用户的id
					//el表达式是能够使用在js中的，但是你的el表达式必须要套用在字符串中
					var id = "${user.id}";

					//将id值赋予select
					$("#create-owner").val(id);

					//所有者下拉框处理完毕之后，打开模态窗口
					$("#createActivityModal").modal("show");

				}

			})

		})

		//为保存按钮绑定事件，执行市场活动的添加操作
		$("#saveBtn").click(function () {

			$.ajax({

				url : "workbench/activity/save.do",
				data : {

					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())

				},
				type : "post",
				dataType : "json",
				success : function(data){

					/*
					* 	data
					* 		{"success" : true/false}
					*
					* */

					if(data.success){

						//添加市场活动成功之后，将添加操作的模态窗口中已经填写的数据清空掉
						//定位到添加操作模态窗口的表单，将表单清空
						//jquery对象提供了submit方法用来提交表单，但是没有提供reset方法重置表单
						/*

							虽然jquery没有提供重置方法，但是原生dom对象提供了重置的方法

							将jquery对象转换为dom对象
								在实际项目开发中，将jquery对象当做成dom对象的数组来操作
								jquery对象[下标]

							将dom对象转换为jquery对象
								$(dom)


						 */
						$("#activityAddForm")[0].reset();

						//添加成功后，刷新列表
						//参数：$("#activityPage").bs_pagination('getOption', 'rowsPerPage') 用户设置好的每页展现的记录数
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						//添加市场活动成功之后，关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");

					}else{

						alert("添加市场活动失败");

					}


				}

			})


		})


		//当页面加载完毕后，刷新市场活动信息列表
		pageList(1,2);

		//为查询按钮绑定事件，执行条件查询操作
		$("#searchBtn").click(function () {

			//点击查询按钮，将搜索框中的内容保存到隐藏域当中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,2);

		})

		//为全选的复选框绑定事件，执行全选操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked);

		})

		//为内容相关的普通的复选框绑定事件，操作全选的复选框
		//这种做法不行，因为$("input[name=xz]")选中的元素是由js动态拼接生成的
		//由js动态拼接生成的元素，不能以传统的绑定事件的方式来执行程序
		/*$("input[name=xz]").click(function () {

			alert(123);

		})*/

		//动态拼接的元素需要使用on的形式来绑定事件
		//$(需要绑定的元素的有效的外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)
		$("#activityBody").on("click",$("input[name=xz]"),function(){

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)

		})

		//为删除按钮绑定事件，执行删除市场活动操作
		$("#deleteBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){

				alert("请选择需要删除的记录");

			//肯定选了，而且可以是多条记录
			}else{

				if(confirm("确定删除所选记录吗？")){

					//alert("执行删除操作");

					//遍历$xz，将里面所有的元素取得，将每一个元素的value值拿到，就相当于拿到了需要删除记录的id
					var param = "";
					for(var i=0;i<$xz.length;i++){

						param += "id="+$($xz[i]).val();

						//如果不是最后一条记录
						if(i<$xz.length-1){

							param += "&";

						}

					}

					$.ajax({

						url : "workbench/activity/delete.do",
						data : param,
						type : "post",
						dataType : "json",
						success : function(data){

							/*

                                data
                                    {"success":true/false}

                             */
							if(data.success){

								//将全选的复选框的√灭掉
								$("#qx").prop("checked",false);

								//删除成功后，刷新市场活动列表
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


							}else{

								alert("删除市场活动失败");

							}


						}

					})

				}


			}

		})

		//为修改按钮绑定事件，打开修改操作的模态窗口
		$("#editBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){

				alert("请选择需要修改的记录");

			}else if($xz.length>1){

				alert("只能选择一条记录执行修改操作");


			//肯定选了，而且肯定只选了一条，确定$xz中只有一个dom元素
			}else{

				var id = $xz.val();

				$.ajax({

					url : "workbench/activity/getUserListAndActivity.do",
					data : {

						"id" : id

					},
					type : "get",
					dataType : "json",
					success : function(data){

						/*

							data
								{"uList":[{用户1},{2},{3}],"a":{市场活动}}

						 */

						//处理所有者下拉框
						var html = "<option></option>";

						$.each(data.uList,function(i,n){

							html += "<option value='"+n.id+"'>"+n.name+"</option>";

						})

						$("#edit-owner").html(html);


						//处理完所有者的下拉框之后，处理单条记录，将记录中的数据铺在修改操作模态窗口的文本框中
						$("#edit-id").val(data.a.id);
						$("#edit-owner").val(data.a.owner);
						$("#edit-name").val(data.a.name);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);


						//数据处理完毕后，将修改操作的模态窗口打开
						$("#editActivityModal").modal("show");



					}

				})


			}

		})

		//为更新按钮绑定事件，执行修改操作
		$("#updateBtn").click(function () {

			$.ajax({

				url : "workbench/activity/update.do",
				data : {

					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"name" : $.trim($("#edit-name").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val())

				},
				type : "post",
				dataType : "json",
				success : function(data){

					/*
					* 	data
					* 		{"success" : true/false}
					*
					* */

					if(data.success){

						//刷新市场活动列表
						//修改操作之后，应该维持在当前页进行刷新
						//参数$("#activityPage").bs_pagination('getOption', 'currentPage')：用来维持在当前页
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						//修改市场活动成功之后，关闭修改操作的模态窗口
						$("#editActivityModal").modal("hide");

					}else{

						alert("修改市场活动失败");

					}


				}

			})


		})


	});


	/*

		pageList方法为刷新市场活动列表的方法
			pageNo：分页操作需要的参数  当前页的页码
			pageSize：分页操作需要的参数  每页需要展现的记录数
			这两个参数是所有分页操作中，需要的两个参数，其他所有的与分页相关的数据，都可以通过这两个参数来取得


		pageList方法的入口都有哪些？（都什么情况需要调用pageList方法，刷新市场活动信息列表？）

			1.点击左边的菜单项"市场活动",进入到该页面，该页面加载完毕后，自动执行该pageList方法，刷新列表
			2.点击查询按钮，需要执行该pageList方法刷新列表
			3.点击分页组件，需要执行该pageList方法刷新列表
			4.执行添加，修改，删除完毕后，需要执行该pageList方法刷新列表

	 */
	function pageList(pageNo,pageSize){

		//查询列表之前，将隐藏域中保存的信息取出，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		//alert("刷新市场活动信息列表");

		/*

			查询市场活动列表，都需要为后台传递哪些参数

			分页查询相关的参数
			pageNo
			pageSize

			条件查询相关的参数
			name
			owner
			startDate
			endDate

		 */
		$.ajax({

			url : "workbench/activity/pageList.do",
			data : {

				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val()),
				"pageNo" : pageNo,
				"pageSize" : pageSize

			},
			type : "get",
			dataType : "json",
			success : function(data){

				//alert(data);

				/*

					data
						{"total":100,"dataList":[{市场活动1},{2},{3}]}

				 */

				var html = "";

				//每一个n就代表每一个市场活动对象
				$.each(data.dataList,function(i,n){

					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td> ';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';

				})

				$("#activityBody").html(html);

				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				//处理完信息列表后，展现分页组件
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//当点击分页组件的时候，触发该函数
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});



			}

		})

	}

	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">



								</select>
							</div>
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">



								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--

						data-dismiss="modal"
							关闭模态窗口

					-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	

	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  <!--

				  	一定要将subimt改为button

				  -->
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">

					<!--

						data-toggle="modal"
							点击该按钮，触发一个模态窗口（模态框）

						data-target="#createActivityModal"
							触发的模态窗口，以id的形式进行定位

						按钮的触发，一定不要写死，一旦将按钮写死了，触发的行为就跟着写死了，想加入其它的行为就不可能了
						例如我们将 data-toggle="modal" data-target="#createActivityModal"写死到了button元素中，在我打开模态窗口前，或者打开模态窗口后，想处理一些额外的行为，就添加不了了

						在实际项目开发中，所有的按钮的行为，一定是我们自己来为它绑定事件，来触发行为

					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称123</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>