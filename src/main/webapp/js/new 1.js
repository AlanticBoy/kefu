 window.onload=function(){
	   
		var container=document.getElementById('container');       /*通过id获取对应元素进行后续的操作*/
		var list=document.getElementById('list');
		var buttons=document.getElementById('buttons').getElementsByTagName('span');
		var pre=document.getElementById('prev');
		var next=document.getElementById('next');
		var index=1;
		var animated=false;
		var timer;
		function showButton(){
			for(var i=0;i<buttons.length;i++){
				if(buttons[i].className=='on'){
				        buttons[i].className='';
					    break;
		        }
		   }
		      buttons[index-1].className="on";   /*设置对应图片页面的下标类名，在css中设置对应的背景色*/
		}

	  function animate(offset){
	      animated=true;
		  var newleft=parseInt(list.style.left)+offset;
		  var time=300;//位移总时间
		  var interval=10;//位移间隔时间
		  var speed=offset/(time/interval);//每一次的位移量
	     function go(){
			   if((speed<0&&parseInt(list.style.left)>newleft)||(speed>0&&parseInt(list.style.left)<newleft)){
					 list.style.left=parseInt(list.style.left)+speed+'px';
					 setTimeout(go,interval);
				  }
			 else{
				 animated=false;
				 list.style.left=newleft+'px';
			  if(newleft>-980){                    /*如果开始为第一张，点击上一张，则切换为最后以后一张*/
				   list.style.left=-2960+'px'; 
			  }
			if(newleft<-2960){
			 list.style.left=-980+'px';          /*如果是最后一张图片，则从头开始*/
					 }
				  }
			   }
			   go();
		   }
		   function play(){
			 timer=setInterval(function(){   /*设置定时器并赋给一个变量，以便于后续操作*/
					  next.onclick();
				},3000);
		   }
		   function stop(){
				clearInterval(timer);       /*清除定时器*/
			}
		   next.onclick=function(){
		   if(index==3){
			   index=1;
			}
		   else{
				 index+=1;
			}
		   showButton();
		   if(animated==false){
				animate(-980);
			  }
		   }
		  pre.onclick=function(){
				 if(index==1){
				  index=3;
				  }
			   else{
				   index-=1;
				}
				showButton();
			   if(animated==false){
					animate(980);
				  }
		   }
	 
		  for(var i=0;i<buttons.length;i++){
			buttons[i].onclick=function(){
			if(this.className=='on'){
			   return;
			}
		   var myIndex=parseInt(this.getAttribute('index'));
		   var offset=-980*(myIndex-index);
				index=myIndex;
				showButton();
					 if(animated==false){
							animate(offset);
						 }
				}
		   }
			 container.onmouseover=stop;       /*鼠标移入容器停止自动切换*/
			 container.onmouseout=play;        /*鼠标移出容器开始自动切换*/
			 play();
   }