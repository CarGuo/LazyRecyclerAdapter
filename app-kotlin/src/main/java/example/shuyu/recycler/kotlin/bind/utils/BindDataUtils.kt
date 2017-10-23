package example.shuyu.recycler.kotlin.bind.utils


import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.bind.model.BindClickModel
import example.shuyu.recycler.kotlin.bind.model.BindImageModel
import example.shuyu.recycler.kotlin.bind.model.BindTextModel


/**
 * Created by guoshuyu on 2017/1/7.
 */

object BindDataUtils {

    val homeList: ArrayList<String>
        get() {
            val list = ArrayList<String>()

            list.add("列表 不上下拉")
            list.add("列表带上下拉")
            list.add("瀑布流 刷新上拉")
            list.add("空页面")
            list.add("Grid上拉无刷新")
            list.add("横向列表带左右拉")
            list.add("横向Grid带左右拉")
            list.add("横向瀑布流带左右拉")
            list.add("拖拽")
            return list
        }

    val normalData: ArrayList<Any>
        get() {

            val list = ArrayList<Any>()


            var textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢1"
            list.add(textModel)


            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢2"
            list.add(textModel)

            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢3"
            list.add(textModel)


            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢4"
            list.add(textModel)


            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢5"
            list.add(textModel)

            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢6"
            list.add(textModel)

            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢7"
            list.add(textModel)

            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢8"
            list.add(textModel)


            return list
        }


    val refreshData: ArrayList<Any>
        get() {

            val list = ArrayList<Any>()

            var imageModel = BindImageModel()
            imageModel.resId = R.drawable.a1
            list.add(imageModel)

            var textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢1"
            list.add(textModel)

            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a2
            list.add(imageModel)

            var clickModel = BindClickModel()
            clickModel.btnText = "我是老按键，按啊按啊按啊····1"
            list.add(clickModel)


            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢2"
            list.add(textModel)

            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a1
            list.add(imageModel)

            clickModel = BindClickModel()
            clickModel.btnText = "我是老按键，按啊按啊按啊····2"
            list.add(clickModel)

            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a2
            list.add(imageModel)

            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢3"
            list.add(textModel)

            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a1
            list.add(imageModel)


            clickModel = BindClickModel()
            clickModel.btnText = "我是老按键，按啊按啊按啊····3"
            list.add(clickModel)


            textModel = BindTextModel()
            textModel.text = "你这个老司机，说好的文本呢4"
            list.add(textModel)

            clickModel = BindClickModel()
            clickModel.btnText = "我是老按键，按啊按啊按啊····4"
            list.add(clickModel)

            return list
        }

    val refreshImageData: ArrayList<Any>
        get() {

            val list = ArrayList<Any>()

            var imageModel = BindImageModel()
            imageModel.resId = R.drawable.a1
            list.add(imageModel)

            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a2
            list.add(imageModel)


            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a1
            list.add(imageModel)
            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a2
            list.add(imageModel)

            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a1
            list.add(imageModel)


            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a1
            list.add(imageModel)


            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a2
            list.add(imageModel)

            imageModel = BindImageModel()
            imageModel.resId = R.drawable.a1
            list.add(imageModel)

            return list
        }

    fun getLoadMoreData(datas: List<Any>): ArrayList<Any> {
        val list = ArrayList<Any>()
        for (i in datas.indices) {
            val `object` = datas[i]
            val newModel: Any
            if (`object` is BindClickModel) {
                newModel = BindClickModel()
                newModel.btnText = "Load More 我就老按键哈哈哈哈！！！！！ " + (datas.indexOf(`object`) + 1)
                list.add(newModel)
            } else if (`object` is BindTextModel) {
                newModel = BindTextModel()
                newModel.text = "Load More 我就老文本哈哈哈哈！！！！！ " + (datas.indexOf(`object`) + 1)
                list.add(newModel)
            } else if (`object` is BindImageModel) {
                newModel = BindImageModel()
                newModel.resId = `object`.resId
                list.add(newModel)
            }
        }
        return list
    }
}
