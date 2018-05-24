package cn.geeksworld.skiingshow.Tools;

/**
 * Created by xhs on 2017/11/16.
 */

public class RepeatCode {
    /*

             <me.gujun.android.taggroup.TagGroup
            android:id="@+id/tag_group"
            style="@style/TagGroup"
           />

        View inTitle = findViewById(R.id.inTitle);
        inTitle.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title_name = inTitle.findViewById(R.id.title_name);
        TextView title_right = inTitle.findViewById(R.id.title_right);
        title_right.setVisibility(View.VISIBLE);
        title_right.setText(R.string.edit);
        title_right.setOnClickListener(this);
        title_name.setText(R.string.page4_collect);


        HashMap<String, Object> map = new HashMap<>();
        map.put("u_id", share.getInt(ShareKey.UID, 0));
        map.put("pageNumber", pageNumber);
        HttpUtil.requestPostNetWork(Url.findMyFavoriteVideo, Tool.getParams(getActivity(), map), new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                JSONObject obj = (JSONObject) JSONObject.parse(result);
                int code = obj.getIntValue("code");
                if (1 == code){
                    JSONArray result1 = obj.getJSONArray("result");
                    List<T> list = JSONArray.parseArray(result1.toString(), T.class);
                    String jsonString = JSON.toJSONString(list, SerializerFeature.PrettyFormat,
                            SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);
                    datas.clear();
                    datas.addAll(JSONArray.parseArray(jsonString, T.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void downfailed(String error) {

            }
        });

          switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }

    class OnRefresh implements OnRefreshListener {

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            pageNumber = 1;
            datas_audio.clear();
            loadData_AudioList();
        }
    }

    class OnLoadMore implements OnLoadmoreListener {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            pageNumber++;
            loadData_AudioList();
        }
    }


    * */
}
