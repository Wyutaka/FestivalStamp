package com.c0de_mattari.nitstamprally.mainAction.controller.beacon

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.util.Log
import com.c0de_mattari.nitstamprally.BuildConfig
import com.c0de_mattari.nitstamprally.mainAction.model.beacon.MyBeaconData
import org.altbeacon.beacon.*
import java.util.*

private const val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
private val mRegion = Region("APIAndBeaconModuleRegion", Identifier.parse("0f0ba8e1-f98a-4f59-af70-91308c6620b5"), null, null)
private var startTime = Date().time

class BeaconController(val parentContext: Context) : BeaconConsumer {

    private val TAG = this.javaClass.simpleName

    private lateinit var mBeaconManager: BeaconManager

    /*クラス生成時に、IActivityLifeCycleを通じて実行される*/
    fun onCreated() {
        mBeaconManager = BeaconManager.getInstanceForApplication(parentContext)
        // iBeaconの受信設定：iBeaconのフォーマットを登録する
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))

    }

    override fun onBeaconServiceConnect() {
        Log.d(TAG,"onBeaconServiceConnect called")
        var callBackCalled = false
        val myBeaconDataList: MutableList<MyBeaconData> = mutableListOf()
        //レンジングのイベント
        mBeaconManager.addRangeNotifier { beaconList, _ ->
            Log.d("DEBAG", startTime.toString())
            Log.d("DEBAG",Date().time.toString())
            if (Date().time - startTime < 3000) {
                callBackCalled = false
                //Android端末によってはbeaconが全然取れないので、（とりあえずの処理として）3倍する
                //TODO:もっと「いい」方法に…
                Log.d(TAG,"Ranging. . . ")
                for (i in 1..3) {
                    myBeaconDataList.addAll(beaconList.asSequence().map { MyBeaconData(it.id2.toInt(), it.rssi) })
                }
            } else {
                //if (callBackCalled == false){
                   // callBackCalled = true
                    mBeaconManager.stopRangingBeaconsInRegion(mRegion)
                    //全ループ終了後に非同期処理を行う
                    Handler().post {
                        onBeaconDataIsUpdated?.invoke(myBeaconDataList)
                        myBeaconDataList.clear()
                    }
                //}
                
            }
        }
    }

    //ビーコン取得後に実行したいコールバックメソッド。BeaconControllerを使うクラス上で書き換える。
    var onBeaconDataIsUpdated: ((beaconListModel: MutableCollection<MyBeaconData>) -> Unit)? = null

    fun rangeBeacon(overWriteFun: (MutableCollection<MyBeaconData>) -> Unit) {
        mBeaconManager.startRangingBeaconsInRegion(mRegion)
        startTime = Date().time
        onBeaconDataIsUpdated = overWriteFun
        Log.d("DEBAG","Rangebeacon called from origin")
    }

    fun bind(bc: BeaconConsumer) = mBeaconManager.bind(bc)
    fun unbind(bc: BeaconConsumer) = mBeaconManager.unbind(bc)

    override fun getApplicationContext(): Context = parentContext.applicationContext
    override fun unbindService(p0: ServiceConnection?) {
        unbind(parentContext as BeaconConsumer)
        parentContext.unbindService(p0)
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean {
        bind(parentContext as BeaconConsumer)
        return parentContext.bindService(p0, p1, p2)
    }

}
