# FABridge

便于Activity和Fragment间通信的库。

## 引入

在你的项目的build.gradle

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.novoda:bintray-release:0.3.4'
   }
}

```

在你的module的build.gradle

```
apply plugin: 'com.neenbedankt.android-apt'

dependencies {
    apt 'com.zhy.fabridge:fabridge-compiler:1.0.0'
    compile 'com.zhy.fabridge:fabridge-api:1.0.0'
}
```

## 使用

* Fragment

```

class SampleFragment extends Fragment
{
	//定义常量
	public static final String EVENT_LIST_ITEM_CLICK = "event_list_item_click";

	//任何需要回调的地方调用
	 Fabridge.call(getActivity(), EVENT_LIST_ITEM_CLICK, params);	
}
```

这个的params是可变参数，不过因为Fragment是复用的，针对某个事件的参数，预先最好设计好（类似于接口回调方法中定义参数一样）。


* Activity中

```
@FCallbackId(id = ListFragment.EVENT_LIST_ITEM_CLICK)
public void menuItemClick(View view, int pos, String title)
{
    mTextView.setText(title);
}
```
通过`@FCallbackId`指定Fragment声明的常量即可。

当Activity中方法需要参数，通过`Fabridge.call()`方法传入即可，针对本例调用方法如下：

`Fabridge.call(getActivity(), EVENT_LIST_ITEM_CLICK, view, position, mAdapter.getItem(position));`，

具体可以参考sample。


## 例子

下面演示一个点击Fragment中的Item，Activity中回调处理。

* Fragment

```
public class ListFragment extends Fragment
{
 //删除了一些代码
   public static final String EVENT_LIST_ITEM_CLICK = "event_list_item_click";

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
       //删除了一些代码
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Fabridge.call(getActivity(), EVENT_LIST_ITEM_CLICK, view, position, mAdapter.getItem(position));
            }
        });
    }
}

```

* Activity

```
public class MainActivity extends AppCompatActivity
{

    private TextView mTextView;
  
    @FCallbackId(id = ListFragment.EVENT_LIST_ITEM_CLICK)
    public void menuItemClick(View view, int pos, String title)
    {
        mTextView.setText(title);
    }
}
```

如果按照常规写法，则需要定义各种接口，设置接口，回调接口方法等。

而本方案，只需要定义一个常量，你可以把常量理解为原本需要定义的接口中的方法，所以针对每个常量，调用时传入的参数应该是固定的（和使用接口时内部的方法一致）。


## 感谢

因为看到这篇文章[Activity 与 Fragment 通信(99%)完美解决方案](http://android.jobbole.com/82699/)，开始对Fragment与Activity通信方案进行考虑。

##License

```
Copyright 2016 hongyangAndroid

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
