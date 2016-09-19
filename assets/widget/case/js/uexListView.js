/**
 * @author lkl
 */
define(["CC"],function(CC){
  if (!UNIT_TEST) return;

  var TEST_CASE = {};
  
  TEST_CASE.setItems = function(){
    var params = {
        "listItems": [
            {
                "image": "http://f7.topit.me/7/c1/0f/119943606879e0fc17l.jpg",
                "placeholderImg": "res://1Normal.png",
                "title": "标题",
                "subtitle": "子标题",
                "rightBtnImg": "res://ac_title_btn_hov.png",
                "titleSize": 25,
                "titleColor": "#006000",
                "subtitleSize": 20,
                "subtitleColor": "#000000",
                "selectedBackgroundColor": "#006000",
                "backgroundColor": "#FFFFFF",
                "useCustomLayout": "1",
                "height": 200
            },
            {
                "image": "http://img5.duitang.com/uploads/item/201407/17/20140717215718_KGaJ3.jpeg",
                "placeholderImg": "res://1Normal.png",
                "title": "标题",
                "subtitle": "子标题",
                "rightBtnImg": "res://ac_title_btn_hov.png",
                "titleSize": 25,
                "titleColor": "#006000",
                "subtitleSize": 20,
                "subtitleColor": "#000000",
                "selectedBackgroundColor": "#006000",
                "backgroundColor": "#FFFFFF",
                "height": 200
            },
            {
                "image": "http://img5.duitang.com/uploads/item/201405/23/20140523214438_aUXPJ.thumb.700_0.jpeg",
                "placeholderImg": "res://1Normal.png",
                "title": "标题",
                "subtitle": "子标题",
                "rightBtnImg": "res://ac_title_btn_hov.png",
                "titleSize": 25,
                "titleColor": "#006000",
                "subtitleSize": 20,
                "subtitleColor": "#000000",
                "selectedBackgroundColor": "#006000",
                "backgroundColor": "#FFFFFF",
                "height": 200
            }
        ],
        "rightSwipeOptionItem": {
            "backgroundColor": "#ffffff",
            "optionBtn": [
                {
                    "btnIndex": "1",
                    "text": "分享",
                    "textColor": "#ffffff",
                    "textSize": 20,
                    "bgColor": "#6F00D2"
                },
                {
                    "btnIndex": "2",
                    "text": "删除",
                    "textColor": "#ffffff",
                    "textSize": 20,
                    "bgColor": "#6F00D2"
                }
            ]
        },
        "leftSwipeOptionItem": {
            "backgroundColor": "#ffffff",
            "optionBtn": [
                {
                    "btnIndex": "1",
                    "text": "分享",
                    "textColor": "#ffffff",
                    "textSize": 20,
                    "bgColor": "#6F00D2"
                },
                {
                    "btnIndex": "2",
                    "text": "删除",
                    "textColor": "#ffffff",
                    "textSize": 20,
                    "bgColor": "#6F00D2"
                }
            ]
        }
    };
    uexListView.setItems(JSON.stringify(params));
    UNIT_TEST.assert(true);
    /*uexSearchBarView.onSearch = function(info){
      uexSearchBarView.onSearch = null;
      var keyword = JSON.parse(info).keyword;
      if (!keyword) {
        CC.log("uexSearchBarView.onSearch回调结果解析失败!");
        UNIT_TEST.assert(false);
      };
      CC.confirm("请确认您输入的内容为\n" + keyword,function(ret){
        UNIT_TEST.assert(ret);
      });
    };
    CC.alert("请输入任意字符串进行搜索.");*/
  };

  TEST_CASE.setItemSwipeType = function(){
      uexListView.setItemSwipeType(2);
      CC.log("设置左右都可以滑动");
      UNIT_TEST.assert(true);
  };

  TEST_CASE.setPullRefreshHeader = function(){
        var params ={
           PullRefreshHeaderStyle:{
                "arrowImage":"res://1Normal.png",
                "backGroundColor":"#e2e7ed",
                "textColor":"#576c89",
                "textFontSize":20,
                "pullRefreshNormalText":"下拉可以刷新",
                "pullRefreshPullingText":"松开即可刷新",
                "pullRefreshLoadingText":"加载中...",
                "isShowUpdateDate":1
            }
        };
        uexListView.setPullRefreshHeader(JSON.stringify(params));
        UNIT_TEST.assert(true);
  };

  TEST_CASE.setPullRefreshFooter = function(){
      var params ={
          PullRefreshFooterStyle:{
              "arrowImage":"res://1Normal.png",
              "backGroundColor":"#e2e7ed",
              "textColor":"#576c89",
              "textFontSize":13,
              "pullRefreshNormalText":"上拉加载更多",
              "pullRefreshPullingText":"松开即可加载更多",
              "pullRefreshLoadingText":"加载中...",
              "isShowUpdateDate":1
          }
      };
      uexListView.setPullRefreshFooter(JSON.stringify(params));
      UNIT_TEST.assert(true);
  };

  TEST_CASE.open = function(){
    var wid = window.screen.width;
    var hei = window.screen.height - 300;
    var param = {
      x: 0,
      y: (uexWindow.getHeight() * 0.2) | 0,
      w: uexWindow.getWidth(),
      h: (uexWindow.getHeight() * 0.7) | 0,
    };
    uexListView.open(param);
    UNIT_TEST.assert(true);
    CC.confirm("请确认listView被正确打开.",function(ret){
      UNIT_TEST.assert(ret);
    });
  };

  TEST_CASE.onItemClick = function(){
    uexSearchBarView.onItemClick = function(info){
      uexSearchBarView.onItemClick = null;
      var data = JSON.parse(info);
      var index = data.index;
      var keyword = data.keyword;
      if (!keyword || (!index && index !== 0)) {
        CC.log("uexSearchBarView.onItemClick回调结果解析失败!");
        UNIT_TEST.assert(false);
      };
      CC.confirm("请确认\n您选择的内容所在的行数(从0开始计数)为: " + index + "\n您所选择的内容为: " + keyword,function(ret){
        UNIT_TEST.assert(ret);
      });
    };
    CC.alert("请继续进行若干次搜索操作,然后点击任一个搜索历史记录.");
  };
  TEST_CASE.clearHistory = function(){
    CC.alert("即将清除搜索历史记录.",function(){
      uexSearchBarView.clearHistory();
      CC.confirm("请确认搜索历史记录已被正确清除",function(ret){
        UNIT_TEST.assert(ret);
      });
    });
  };
  TEST_CASE.close = function(){
    uexSearchBarView.close();
    CC.confirm("请确认searchBarView已被正确关闭.",function(ret){
      UNIT_TEST.assert(ret);
    });
  };


  UNIT_TEST.addCase("uexListView", TEST_CASE);
});
