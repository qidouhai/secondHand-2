/*function getByClass(aParent,sClass)
{
	var aEle=aParent.getElementsByTagName('*');
	var aResult=[];
	for(var i=0;i<aEle.length;i++)
	{
		if(aEle[i].className==sClass)
		{
			aResult.push(aEle[i]);	
		}
	}
	return aResult;
}

function getStyle(obj,name)
{
	if(obj.currentStyle)
	{
		return obj.currentStyle[name];	
	}
	else
	{
		return getComputedStyle(obj,false)[name];	
	}
}
function startMove(obj,json,fnEnd)
{
	
	clearInterval(obj.timer);
	obj.timer=setInterval(function(){
		var bStop=true;
		for(var attr in json)
		{
			var cur=0;
			if(attr=='opacity')
			{
				cur=Math.round((getStyle(obj,attr))*100);	
			}
			else
			{
				cur=parseInt(getStyle(obj,attr))	
			}
			var speed=(json[attr]-cur)/7;
			speed=speed>0?Math.ceil(speed):Math.floor(speed);
			if(cur!=json[attr])
			{
				bStop=false;	
			}
			if(attr=='opacity')
			{
				obj.style.filter='alpha(opacity:'+(cur+speed)+')';
				obj.style.opacity=(cur+speed)/100;	
			}
			else
			{
				obj.style[attr]=cur+speed+'px';	
			}
		}
		if(bStop)
		{
			clearInterval(obj.timer);
			if(fnEnd)fnEnd();	
		}
	},30)
}*/
window.onload=function()
{
	var aSlide=document.getElementById('single_lb');
	var aBig=getByClass(aSlide,'big')[0];
	var aBigLi=aBig.getElementsByTagName('li');
	var aSmall=getByClass(aSlide,'small')[0];
	var aSmallLi=aSmall.getElementsByTagName('li');
	var aSmallImg=aSmall.getElementsByTagName('img')
	var aPrev=getByClass(aSlide,'prev')[0];
	var aNext=getByClass(aSlide,'next')[0];
	var now=0;
	aBig.style.width=aBigLi[0].offsetWidth*aBigLi.length+'px';
	aSmall.style.width=(aSmallLi[0].offsetWidth+10)*aSmallLi.length+'px';
	
	aNext.onmouseover=aPrev.onmouseover=aBig.onmouseover=function()
	{
		startMove(aPrev,'opacity',70);
		startMove(aNext,'opacity',70);
	}
	aNext.onmouseout=aPrev.onmouseout =aBig.onmouseout=function()
	{
		startMove(aPrev,'opacity',0);
		startMove(aNext,'opacity',0);
	}
	aSlide.onmouseover=function()
	{
		clearInterval(timer);	
	}
	aSlide.onmouseout=function()
	{
		timer=setInterval(aNext.onclick,3000)	
	}
	for(var i=0;i<aSmallLi.length;i++)
	{
		
		aSmallLi[i].index=i;
		
		aSmallLi[i].onclick=function()
		{
			if(this.index==now)return;
			now=this.index;
			
			tab();
		}
	}
	aPrev.onclick=function()
	{
		now--;
		if(now==-1)
		{
			now=aSmallLi.length-1;	
		}
		tab();
	}
	aNext.onclick=function()
	{
		now++;
		if(now>aSmallLi.length-1)
		{
			now=0;	
		}
		tab();
	}
	function tab()
	{
		startMove(aBig,'left',-now*aBigLi[0].offsetWidth)
		if(now==0)
		{
			startMove(aSmall,'left',0)	
		}
		else if(now==aSmallLi.length-1)
		{
			startMove(aSmall,'left',-(now-3)*(aSmallLi[0].offsetWidth+5))	
		}
		else if(now==aSmallLi.length-2)
		{
			startMove(aSmall,'left',-(now-2)*(aSmallLi[0].offsetWidth+5))	
		}
		
		else
		{
			startMove(aSmall,'left',-(now-1)*(aSmallLi[0].offsetWidth+5));
		}
		for(var i=0;i<aSmallImg.length;i++)
		{
			aSmallImg[i].index=i;
			startMove(aSmallImg[i],'opacity',50);
			aSmallImg[i].onmouseover=function()
			{
				startMove(this,'opacity',100)	
			}
			aSmallImg[i].onmouseout=function()
			{
				if(now!=this.index)
				{
					startMove(this,'opacity',50)
				}
			}	
		}
		startMove(aSmallImg[now],'opacity',100);		
	}
	var timer=setInterval(aNext.onclick,3000);
	
}







