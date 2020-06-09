var btn1 = document.getElementById("btn1");
var btn2 = document.getElementById("btn2");
var arr = [0, 1, 2, 5, 8, 7, 6, 3];//按这个下标依次旋转
var lis = document.getElementsByTagName("li");
var num = 0;  //定义初识下标
btn1.onclick = function () {

    btn1.disabled = true;
    time = setInterval(function () {
        lis[arr[num]].className = "";
        num++;
        if (num > 7) {
            num = 0;
        }
        lis[arr[num]].className = "light";
        console.log(lis[arr[num]].className);
    }, 30);

};
btn2.onclick = function () {
    // var loc = location.href;
    // var n1 = loc.length;//地址的总长度
    // var n2 = loc.indexOf("=");//取得=号的位置
    // var username = decodeURI(loc.substr(n2 + 1, n1 - n2));//取=号后面的内容
    // document.getElementById("username").value = username;
    // console.log(username);
    //  var obj = document.getElementById("uls").getElementsByTagName("li");
    setTimeout(function () {
        clearInterval(time);
    }, 0);
    btn1.disabled = false;
    // var prize= obj[arr[num]].innerHTML;
    // alert(prize);

};
