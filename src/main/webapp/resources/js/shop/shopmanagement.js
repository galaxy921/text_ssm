$(function () {

    var shopId=getQueryString("shopId");
    var shopInfoUrl='/shopadmin/getshopmanagementinfo?shopId='+shopId;
    alert(shopId);
    $.getJSON(shopInfoUrl,function (data) {
        if(data.redirect){

            window.location.href=data.url;
        }else{
            if(data.shopId!=underfined&&data.shopId!=null){
                shopId=data.shopId;
            }
            $('#shopInfo').attr('href','/shopadmin/shopoperation?shopId='+shopId);
        }

    });

});